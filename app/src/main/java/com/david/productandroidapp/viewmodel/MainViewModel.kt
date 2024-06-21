package com.david.productandroidapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.david.productandroidapp.model.CurrentProductResponse
import com.david.productandroidapp.networking.ApiConfig
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _productData = MutableLiveData<List<CurrentProductResponse>>()
    val productData: LiveData<List<CurrentProductResponse>> get() = _productData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> get() = _isError

    var errorMessage: String = ""
        private set

    fun getProductData(product: String) {
        _isLoading.value = true
        _isError.value = false

        val client = ApiConfig.getApiService().getCurrentProducts(product)

        // Send API request using Retrofit
        client.enqueue(object : Callback<List<CurrentProductResponse>> {

            override fun onResponse(
                call: Call<List<CurrentProductResponse>>,
                response: Response<List<CurrentProductResponse>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { responseBody ->
                        _isLoading.value = false
                        _productData.postValue(responseBody)
                    } ?: onError("Empty response body")
                } else {
                    onError("Data Processing Error")
                }
            }

            override fun onFailure(call: Call<List<CurrentProductResponse>>, t: Throwable) {
                onError(t.message ?: "Unknown error")
            }
        })
    }

    fun onError(errorMessage: String) {
        _isLoading.value = false
        _isError.value = true
        this.errorMessage = errorMessage
        // Handle the error message appropriately
    }
}
