package com.example.appcentnewsapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appcentnewsapp.model.News
import com.example.appcentnewsapp.repo.NewsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch


class HomePageViewModel : ViewModel() {
    var newsList = MutableLiveData<List<News>?>()
    private val newsRepo = NewsRepository()
    private var currentQuery = "" // Store the current search query

    private val searchFlow = MutableSharedFlow<String>()

    private var searchJob: Job? = null

    init {
        observeSearchFlow()
        loadNews()
    }

    private fun observeSearchFlow() {
        viewModelScope.launch {
            searchFlow
                .debounce(500) // Adjust the debounce time as needed (in milliseconds)
                .collect { query ->
                    loadNews(query)
                }
        }
    }

    private fun loadNews(query: String = "") {
        viewModelScope.launch {
            newsList.value = newsRepo.loadNewsList().value
            if (query.isNotEmpty()) {
                // Implement your API here
                newsRepo.loadAllData(query, "YOUR API KEY")
                currentQuery = query // Save the current search query
            }
        }
    }

    fun searchNews(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            searchFlow.emit(query)
        }
    }

    fun getCurrentQuery(): String {
        return currentQuery
    }
}


