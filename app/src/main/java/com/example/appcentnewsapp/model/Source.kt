package com.example.appcentnewsapp.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Source (
    @SerializedName("id") var id: String? = null,
    @SerializedName("name") var name: String? = null
)