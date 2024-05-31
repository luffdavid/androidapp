package com.david.productandroidapp.view


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.david.productandroidapp.R
import com.david.productandroidapp.model.CurrentWeatherResponse
import com.david.productandroidapp.viewmodel.MainViewModel
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel

    private lateinit var etCityName: EditText
    private lateinit var imgCondition: ImageView
    private lateinit var tvResult: TextView
    private lateinit var btnSend: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel = MainViewModel()
        subscribe()

        etCityName = findViewById(R.id.et_city_name)
        imgCondition = findViewById(R.id.img_condition)
        tvResult = findViewById(R.id.tv_result)
        btnSend = findViewById(R.id.btn_send_request)

        // Add on click button to the send button
        btnSend.setOnClickListener {
            // Text field validation
            if (etCityName.text.isNullOrEmpty() or etCityName.text.isNullOrBlank()) {
                etCityName.error = "Field can't be null"
            } else {
                // Get weather data
                mainViewModel.getWeatherData(etCityName.text.toString())
            }
        }
    }

    private fun subscribe() {
        mainViewModel.isLoading.observe(this) { isLoading ->
            // Set the result text to Loading
            if (isLoading) tvResult.text = resources.getString(R.string.loading)
        }

        mainViewModel.isError.observe(this) { isError ->
            // Hide display image and set the result text to the error message
            if (isError) {
                imgCondition.visibility = View.GONE
                tvResult.text = mainViewModel.errorMessage
            }
        }

        mainViewModel.weatherData.observe(this) { weatherData ->
            // Display weather data to the UI
            setResultText(weatherData)
        }
    }

    private fun setResultText(weatherData: CurrentWeatherResponse) {
        val resultText = StringBuilder("Result:\n")

        weatherData.location.let { location ->
            resultText.append("Name: ${location?.name}\n")
            resultText.append("Region: ${location?.region}\n")
            resultText.append("Country: ${location?.country}\n")
            resultText.append("Timezone ID: ${location?.tzId}\n")
            resultText.append("Local Time: ${location?.localtime}\n")
        }

        weatherData.current.let { current ->
            current?.condition.let { condition ->
                resultText.append("Condition: ${condition?.text}\n")
                setResultImage(condition?.icon)
            }
            resultText.append("Celcius: ${current?.tempC}\n")
            resultText.append("Fahrenheit: ${current?.tempF}\n")
        }

        tvResult.text = resultText
    }

    private fun setResultImage(imageUrl: String?) {
        // Display image when image url is available
        imageUrl.let { url ->
            Glide.with(applicationContext)
                .load("https:$url")
                .into(imgCondition)

            imgCondition.visibility = View.VISIBLE
            return
        }

        // Hide image when image url is null
        imgCondition.visibility = View.GONE
    }
}