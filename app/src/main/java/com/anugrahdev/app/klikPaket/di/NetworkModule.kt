package com.anugrahdev.app.klikPaket.di

import android.content.Context
import com.anugrahdev.app.klikPaket.data.network.ApiService
import com.anugrahdev.app.klikPaket.data.network.NetworkInterceptor
import com.anugrahdev.app.klikPaket.utils.Constant
import com.facebook.stetho.Stetho
import com.facebook.stetho.okhttp3.StethoInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideApiService(
        @ApplicationContext context: Context
    ): ApiService {
        val logging = HttpLoggingInterceptor()
        val networkInterceptor = NetworkInterceptor(context)
        logging.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(90, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .addInterceptor(networkInterceptor)
            .addNetworkInterceptor(StethoInterceptor())
            .connectTimeout(90, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(Constant.base_url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

}