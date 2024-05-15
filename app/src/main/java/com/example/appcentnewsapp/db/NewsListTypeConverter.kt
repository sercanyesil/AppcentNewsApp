package com.example.appcentnewsapp.db

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.appcentnewsapp.model.News
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@ProvidedTypeConverter
class NewsListTypeConverter {
    @TypeConverter
    fun fromNewsList(newsList: ArrayList<News>): String {
        return Gson().toJson(newsList)
    }

    @TypeConverter
    fun toNewsList(newsListJson: String): ArrayList<News> {
        val type = object : TypeToken<ArrayList<News>>() {}.type
        return Gson().fromJson(newsListJson, type)
    }
}