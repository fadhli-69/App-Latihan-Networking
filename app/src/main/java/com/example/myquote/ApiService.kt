package com.example.myquote.network

import com.example.myquote.Quote
import retrofit2.http.GET

interface ApiService {
    @GET("list")
    suspend fun getListQuotes(): List<Quote>
}
