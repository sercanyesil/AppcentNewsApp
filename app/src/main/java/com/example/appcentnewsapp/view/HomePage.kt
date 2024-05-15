package com.example.appcentnewsapp.view

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.appcentnewsapp.model.News

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.intl.Locale
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData


import com.example.appcentnewsapp.viewmodel.HomePageViewModel
import com.google.gson.Gson
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.Job
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomePage(navController: NavController) {
    val viewModel:HomePageViewModel = viewModel()
    val newsList = viewModel.newsList.observeAsState(listOf())


    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val textField = remember { mutableStateOf(viewModel.getCurrentQuery()) } // Retrieve the current query from ViewModel
    val isSearching = remember { mutableStateOf(false) }


    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Appcent NewsApp")
                })
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier.padding(paddingValues)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextField(
                        value = textField.value,
                        onValueChange = { newValue ->
                            textField.value = newValue
                            viewModel.searchNews(newValue)
                        },
                        label = { Text(text = "Type a text")},
                        modifier = Modifier
                            .weight(1f)
                            .padding(all = 10.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        trailingIcon = {
                            val icon = if (isSearching.value || textField.value.isNotEmpty()) Icons.Default.Clear else Icons.Default.Search
                            IconButton(
                                onClick = {
                                    if (isSearching.value || textField.value.isNotEmpty()) {
                                        textField.value = ""
                                        isSearching.value = false
                                    } else {
                                        isSearching.value = true
                                    }
                                }
                            ) {
                                Icon(icon, contentDescription = if (isSearching.value) "Close" else "Search")
                            }
                        }
                    )
                }
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    items(
                        count = newsList.value?.size ?: 0, // Use safe access to avoid NullPointerException
                        itemContent = { index ->
                            val news = newsList.value?.get(index)
                            news?.let { newsItem ->
                                Card(
                                    modifier = Modifier
                                        .padding(all = 8.dp)
                                        .fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .clickable {
                                                val newsJson = Gson().toJson(news)
                                                navController.navigate("news_detail_page/${Uri.encode(newsJson)}")
                                            }
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .padding(all = 5.dp)
                                                .weight(1f)
                                        ) {
                                            Text(text = newsItem.title ?: "",
                                                fontWeight = FontWeight.Bold,
                                                maxLines = 2,
                                                modifier = Modifier.padding(bottom = 4.dp))
                                            Text(text = newsItem.source?.name ?: "",
                                                fontWeight = FontWeight.Light,
                                                fontSize = 12.sp,
                                                modifier = Modifier.padding(bottom = 4.dp))
                                            Text(
                                                text = newsItem.content ?: "",
                                                overflow = TextOverflow.Ellipsis,
                                                fontSize = 14.sp,
                                                maxLines = 4,
                                            )
                                        }
                                        Column(
                                            modifier = Modifier.padding(start = 16.dp)
                                        ) {
                                            // to show our date in a formatted way.
                                            val rawDate = newsItem.publishedAt ?: ""
                                            val truncatedDate = if (rawDate.length >= 10) rawDate.substring(0, 10) else rawDate

                                            Text(
                                                text = truncatedDate,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Medium,
                                                modifier = Modifier.padding(all = 5.dp)
                                            )
                                            GlideImage(
                                                imageModel = { newsItem.urlToImage ?: "" },
                                                modifier = Modifier.size(150.dp).padding(all = 8.dp).clip(RoundedCornerShape(8.dp))
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
    )
}