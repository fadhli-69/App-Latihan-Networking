package com.example.myquote

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myquote.databinding.ActivityListQuotesBinding
import com.example.myquote.network.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ListQuotesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListQuotesBinding

    // Inisialisasi Retrofit
    private val apiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://quote-api.dicoding.dev/")  // Ganti dengan base URL API kamu
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListQuotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up RecyclerView
        binding.listQuotes.layoutManager = LinearLayoutManager(this)

        // Panggil fungsi untuk mendapatkan daftar kutipan
        getListQuotes()
    }

    private fun getListQuotes() {
        binding.progressBar.visibility = View.VISIBLE

        // Menggunakan Coroutine untuk memanggil API secara asinkron
        CoroutineScope(Dispatchers.Main).launch {
            try {
                // Panggil API menggunakan suspend function
                val quotes = apiService.getListQuotes()

                // Proses respons dan update RecyclerView
                val listQuote = quotes.map { "\n${it.en}\n — ${it.author}\n" }
                val adapter = QuoteAdapter(ArrayList(listQuote))
                binding.listQuotes.adapter = adapter
            } catch (e: Exception) {
                Toast.makeText(this@ListQuotesActivity, e.message ?: "Unknown error", Toast.LENGTH_SHORT).show()
            } finally {
                binding.progressBar.visibility = View.INVISIBLE
            }
        }
    }
}
