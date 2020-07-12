package com.anugrahdev.app.klikPaket.data.network

import com.anugrahdev.app.klikPaket.MyApplication
import com.anugrahdev.app.klikPaket.R
import com.anugrahdev.app.klikPaket.utils.ApiException
import retrofit2.Response

abstract class SafeApiRequest {
    suspend fun <T : Any> apiRequest(call: suspend () -> Response<T>): T {
        val response = call.invoke()
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            when (response.code()) {
                500 -> {
                    throw ApiException(
                        MyApplication.context.resources.getString(R.string.error_500)
                    )
                }
                else -> {
                    throw ApiException(response.message())
                }
            }
        }
    }
}

