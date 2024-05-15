package com.example.appcentnewsapp.repo

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.appcentnewsapp.model.News
import com.example.appcentnewsapp.model.SearchResponse
import com.example.appcentnewsapp.network.ApiUtils
import com.example.appcentnewsapp.network.NewsApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/*class NewsRepository {
    var newsList = MutableLiveData<List<News>?>()
    var newsApiInterface:NewsApiInterface

    init {
        newsApiInterface = ApiUtils.getApiInterface()
        newsList = MutableLiveData()
    }

    fun loadNewsList(): MutableLiveData<List<News>?> {
        return newsList
    }

    fun loadAllData() {
        newsApiInterface.getPosts().enqueue(object : Callback<SearchResponse> {
            override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
                if (response.isSuccessful) {
                    val list = response.body()?.articles
                    newsList.value = list
                }
            }
            override fun onFailure(p0: Call<SearchResponse>, p1: Throwable) {
                p1.printStackTrace()
            }
        })
    }
}*/

class NewsRepository {
    private val newsList = MutableLiveData<List<News>?>()
    private val newsApiInterface: NewsApiInterface = ApiUtils.getApiInterface()

    fun loadNewsList(): MutableLiveData<List<News>?> {
        return newsList
    }

    suspend fun loadAllData(query: String, apiKey: String) {
        try {
            val response = withContext(Dispatchers.IO) {
                newsApiInterface.getPosts(query, apiKey)
            }
            if (response.isSuccessful) {
                val list = response.body()?.articles
                newsList.postValue(list)
            } else {
                Log.e("NewsRepository", "Failed to load news: ${response.message()}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
