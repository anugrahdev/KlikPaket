package com.anugrahdev.app.klikPaket.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.anugrahdev.app.klikPaket.utils.NoConnectivityException
import okhttp3.Interceptor
import okhttp3.Response

class NetworkInterceptor(context: Context) : Interceptor{
    private val applicationContext = context.applicationContext
    override fun intercept(chain: Interceptor.Chain): Response {
        if  (!isInternetAvailable()){
            throw NoConnectivityException("No Internet Connection")
        }
        return chain.proceed(chain.request())
    }

    private fun isInternetAvailable():Boolean{

        var result = false
        val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            connectivityManager?.let {
                it.getNetworkCapabilities(connectivityManager.activeNetwork)?.apply {
                    result  = when {
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ->true
                        else->false
                    }
                }
            }

        return result

    }
}