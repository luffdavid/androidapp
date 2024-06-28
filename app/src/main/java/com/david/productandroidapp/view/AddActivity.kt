package com.david.productandroidapp.view

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.david.productandroidapp.R
import com.david.productandroidapp.model.CurrentProductResponse
import com.david.productandroidapp.networking.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        supportActionBar?.title = "Add a product"

        val nameEditText = findViewById<EditText>(R.id.nameEditText)
        val productGroupIdEditText = findViewById<EditText>(R.id.productGroupIdEditText)
        val priceEditText = findViewById<EditText>(R.id.priceEditText)
        val stockEditText = findViewById<EditText>(R.id.stockEditText)
        val addProductButton = findViewById<Button>(R.id.addProductButton)

        addProductButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val productGroupId = productGroupIdEditText.text.toString().toIntOrNull()
            val price = priceEditText.text.toString().toDoubleOrNull()
            val stock = stockEditText.text.toString().toIntOrNull()

            if (name.isNotEmpty() && productGroupId != null && price != null && stock != null) {
                val newProduct = CurrentProductResponse(
                    name = name,
                    productGroupId = productGroupId,
                    price = price,
                    stock = stock
                )

                val apiService = ApiConfig.getApiService()
                val call = apiService.createProduct(newProduct)
                call.enqueue(object : Callback<CurrentProductResponse> {
                    override fun onResponse(call: Call<CurrentProductResponse>, response: Response<CurrentProductResponse>) {
                        if (response.isSuccessful) {
                            val createdProduct = response.body()
                            Log.d("AddActivity", "Produkt hinzugefügt: $createdProduct")
                            Toast.makeText(this@AddActivity, "Produkt erfolgreich hinzugefügt", Toast.LENGTH_SHORT).show()
                            // Hier kannst du weitere Aktionen hinzufügen, wie z.B. zur Hauptaktivität zurückkehren
                        } else {
                            Log.e("AddActivity", "Fehler beim Hinzufügen des Produkts: ${response.errorBody()?.string()}")
                            Toast.makeText(this@AddActivity, "Fehler beim Hinzufügen des Produkts", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<CurrentProductResponse>, t: Throwable) {
                        Log.e("AddActivity", "Fehler beim API-Aufruf", t)
                        Toast.makeText(this@AddActivity, "Fehler beim API-Aufruf", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Log.e("AddActivity", "Bitte alle Felder korrekt ausfüllen")
                Toast.makeText(this@AddActivity, "Bitte alle Felder korrekt ausfüllen", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
