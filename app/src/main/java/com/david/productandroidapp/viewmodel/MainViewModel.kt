package com.david.productandroidapp.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.david.productandroidapp.model.CurrentWeatherResponse
import com.david.productandroidapp.networking.ApiConfig
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response

class MainViewModel() : ViewModel() {

    private val _weatherData = MutableLiveData<CurrentWeatherResponse>()
    val weatherData: LiveData<CurrentWeatherResponse> get() = _weatherData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> get() = _isError

    var errorMessage: String = ""
        private set

    fun getWeatherData(city: String) {

        _isLoading.value = true
        _isError.value = false

        val client = ApiConfig.getApiService().getCurrentWeather(city= city)

        // Send API request using Retrofit
        client.enqueue(object : Callback<CurrentWeatherResponse> {

            override fun onResponse(
                call: Call<CurrentWeatherResponse>,
                response: Response<CurrentWeatherResponse>
            ) {
                val responseBody = response.body()
                if (!response.isSuccessful || responseBody == null) {
                    onError("Data Processing Error")
                    return
                }

                _isLoading.value = false
                _weatherData.postValue(responseBody)
            }

            override fun onFailure(call: Call<CurrentWeatherResponse>, t: Throwable) {
                onError(t.message)
                t.printStackTrace()
            }

        })
    }

    private fun onError(inputMessage: String?) {

        val message = if (inputMessage.isNullOrBlank() or inputMessage.isNullOrEmpty()) "Unknown Error"
        else inputMessage

        errorMessage = StringBuilder("ERROR: ")
            .append("$message some data may not displayed properly").toString()

        _isError.value = true
        _isLoading.value = false
    }

}