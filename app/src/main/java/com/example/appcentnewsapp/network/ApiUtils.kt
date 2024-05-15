package com.example.appcentnewsapp.network


class ApiUtils {
    companion object{
        val BASE_URL = "https://newsapi.org/v2/"

        fun getApiInterface():NewsApiInterface{
            return RetrofitClient.getClient(BASE_URL).create(NewsApiInterface::class.java)
        }
    }
}