package com.example.appcentnewsapp.network

import com.example.appcentnewsapp.model.SearchResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface NewsApiInterface {
    @GET("everything/")
    suspend fun getPosts(
        @Query("q") query: String,
        @Query("apiKey") apiKey: String
    ): Response<SearchResponse>
}