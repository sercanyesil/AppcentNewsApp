package com.example.appcentnewsapp.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponse (
    @SerializedName("status") var status: String?= null,
    @SerializedName("totalResults") var totalResults : Int?= null,
    @SerializedName("articles") var articles: ArrayList<News> = arrayListOf()
)