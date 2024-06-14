package com.david.productandroidapp.view


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.david.productandroidapp.R
import com.david.productandroidapp.model.CurrentProductResponse
import com.david.productandroidapp.viewmodel.MainViewModel
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel

    private lateinit var etCityName: EditText
    private lateinit var imgCondition: ImageView
    private lateinit var tvResult: TextView
    private lateinit var btnSend: Button
    private lateinit var btnNavigate: Button // Hinzugefügt
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel = MainViewModel()
        subscribe()

        etCityName = findViewById(R.id.et_city_name)
        imgCondition = findViewById(R.id.img_condition)
        tvResult = findViewById(R.id.tv_result)
        btnSend = findViewById(R.id.btn_send_request)
        btnNavigate = findViewById(R.id.btn_navigate) // Hinzugefügt
        // Add on click button to the send button
        btnSend.setOnClickListener {
            // Text field validation
            if (etCityName.text.isNullOrEmpty() or etCityName.text.isNullOrBlank()) {
                etCityName.error = "Field can't be null"
            } else {
                // Get weather data
                mainViewModel.getProductData(etCityName.text.toString())
            }
        }
        // OnClickListener für den btnNavigate
        btnNavigate.setOnClickListener {
            val intent = Intent(this, DetailActivity::class.java)
            startActivity(intent)
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

        mainViewModel.productData.observe(this) { weatherData ->
            // Display weather data to the UI
            setResultText(weatherData)
        }
    }

    private fun setResultText(weatherData: CurrentProductResponse) {
        val resultText = StringBuilder("Result:\n")

        weatherData.let { product ->
            resultText.append("Name: ${product?.name}\n")

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