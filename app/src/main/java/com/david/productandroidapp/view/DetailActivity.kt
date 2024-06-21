package com.david.productandroidapp.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.david.productandroidapp.R
import com.david.productandroidapp.model.CurrentProductResponse
import com.david.productandroidapp.networking.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductAdapter
    private var productList: MutableList<CurrentProductResponse> = mutableListOf()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ProductAdapter(productList)
        recyclerView.adapter = adapter

        fetchProducts()
    }

    private fun fetchProducts() {
        ApiConfig.getApiService().getProducts().enqueue(object : Callback<List<CurrentProductResponse>> {
            override fun onResponse(call: Call<List<CurrentProductResponse>>, response: Response<List<CurrentProductResponse>>) {
                if (response.isSuccessful) {
                    response.body()?.let { products ->
                        productList.clear()
                        productList.addAll(products)
                        adapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(call: Call<List<CurrentProductResponse>>, t: Throwable) {
                Log.e("DetailActivity", "Failed to fetch products", t)
            }
        })
    }
}
