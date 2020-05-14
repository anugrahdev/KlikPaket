package com.anugrahdev.app.ikurir.data.network

import com.anugrahdev.app.ikurir.data.models.cities.CitiesResponse
import com.anugrahdev.app.ikurir.data.models.districts.DistrictsResponse
import com.anugrahdev.app.ikurir.data.models.shippingcost.ShippingResponse
import com.anugrahdev.app.ikurir.data.models.waybill.WaybillResponse
import com.anugrahdev.app.ikurir.utils.Constant.Companion.authorization
import com.anugrahdev.app.ikurir.utils.Constant.Companion.base_url
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface ApiService {

    @GET("cities")
    suspend fun getCities(
        @Query("q") query:String,
        @Header("Authorization") auth:String = authorization
        ): Response<CitiesResponse>


    @GET("districts")
    suspend fun getDistricts(
        @Query("q") query: String,
        @Header("Authorization") auth:String = authorization
    ): Response<DistrictsResponse>

    @POST("shipping")
    suspend fun postShippingCost(
        @Query("origin") originId:Int,
        @Query("destination") destinationId: Int,
        @Query("weight") weight:Int,
        @Query("courier") courier:String,
        @Header("Authorization") auth:String = authorization
    ): Response<ShippingResponse>

    @POST("waybill")
    suspend fun postWaybill(
        @Query("waybill") waybill:String,
        @Query("courier") courier:String,
        @Header("Authorization") auth:String = authorization

    ): Response<WaybillResponse>




    companion object {
        operator fun invoke() : ApiService{

            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES) // connect timeout
                .writeTimeout(1, TimeUnit.MINUTES) // write timeout
                .readTimeout(1, TimeUnit.MINUTES) // read timeout
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)


        }
    }

}