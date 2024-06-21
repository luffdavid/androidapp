package com.david.productandroidapp.networking

import com.david.productandroidapp.model.CurrentProductResponse
import retrofit2.Call
import retrofit2.http.*


interface ApiService {

    @GET("salesProduct")
    fun getProducts(): Call<List<CurrentProductResponse>>
    @GET("salesProduct/search")
    fun getCurrentProducts(
@Query("product") product: String
    ): Call<List<CurrentProductResponse>>
}
