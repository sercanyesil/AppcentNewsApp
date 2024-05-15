package com.example.appcentnewsapp.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.appcentnewsapp.model.News


import com.skydoves.landscapist.glide.GlideImage

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsDetailPage(news: News, onBackClicked: () -> Unit, onFavoriteClicked: (News) -> Unit, navController: NavController) {
    
    var isFavorite by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "News Detail") },
                navigationIcon = {
                    IconButton(onClick = { onBackClicked() }) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { shareNewsLink(context = navController.context, newsLink = news.url ?: "") }) {
                        Icon(Icons.Filled.Share, contentDescription = "Share")
                    }
                    IconButton(onClick = {
                        isFavorite = !isFavorite

                    }) {
                        if (isFavorite)
                            Icon(Icons.Filled.Favorite, contentDescription = "Favorite")
                        else
                        Icon(Icons.Filled.FavoriteBorder, contentDescription = "BorderFavorite")
                    }
                }
            )
        }
    ) { innerPadding ->
        Spacer(modifier = Modifier.height(innerPadding.calculateTopPadding()))
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        )  {
            GlideImage(
                imageModel = { news.urlToImage ?: "" },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 8.dp)
                    .size(250.dp)
                    .clip(
                        RoundedCornerShape(8.dp)
                    ))
            Text(text = news.title ?: "No title available", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(all = 8.dp))
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(all = 8.dp), horizontalArrangement = Arrangement.SpaceAround) {
                Row {
                    Icon(Icons.Filled.AccountCircle, contentDescription = "Author")
                    Text(text = news.source?.name ?: "No author available", fontSize = 16.sp)
                }
                Row {
                    val rawDate = news.publishedAt ?: ""
                    val truncatedDate = if (rawDate.length >= 10) rawDate.substring(0, 10) else rawDate
                    Icon(Icons.Filled.DateRange, contentDescription = "Date")
                    Text(text = truncatedDate, fontSize = 16.sp)
                }
            }
            Text(text = news.content ?: "No content available", fontSize = 16.sp, modifier = Modifier.padding(all = 8.dp))
            Button(onClick = { val encodedLink = Uri.encode(news.url ?: "")
                navController.navigate("news_webview_page/$encodedLink") },
                modifier = Modifier.run { padding(top = 100.dp ).align(Alignment.CenterHorizontally) }) {
                Text(text = "News Source")
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SetJavaScriptEnabled")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebViewPage(
    newsLink: String,
    onBackClicked: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "News Source") },
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) {innerPadding ->
        AndroidView(factory = { context ->
            WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                webViewClient = WebViewClient()
                loadUrl(newsLink)
                settings.javaScriptEnabled = true
            }
        }, update = { webView ->
            webView.loadUrl(newsLink)
        })
    }
}

fun shareNewsLink(context: Context, newsLink: String) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, newsLink)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    context.startActivity(shareIntent)
}

@Composable
fun FavoritesPage2(news: News) {

}