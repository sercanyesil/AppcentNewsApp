package com.example.appcentnewsapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.appcentnewsapp.model.News
import com.example.appcentnewsapp.ui.theme.AppcentNewsAppTheme
import com.example.appcentnewsapp.view.FavoritesPage
import com.example.appcentnewsapp.view.HomePage
import com.example.appcentnewsapp.view.NewsDetailPage
import com.example.appcentnewsapp.view.WebViewPage

import com.google.gson.Gson


class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppcentNewsAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    NavigationDrawer()
                }
            }
        }
    }
}

@Composable
fun PageNavigation(navController: NavController) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "homepage" ){
        composable("homepage"){
            HomePage(navController = navController)
        }
        composable("news_detail_page/{news}",arguments = listOf(
            navArgument("news"){type = NavType.StringType}
        )){ backStackEntry ->
            val json = backStackEntry.arguments?.getString("news")
            val news = Gson().fromJson(json, News::class.java)
            NewsDetailPage(
                news = news,
                navController = navController,
                onBackClicked = { navController.popBackStack() }, // Navigate back when the back button is clicked
                onFavoriteClicked = { }
            )
        }
        composable("news_webview_page/{newsLink}") { backStackEntry ->
            val newsLink = backStackEntry.arguments?.getString("newsLink")

            if (!newsLink.isNullOrEmpty()) {
                WebViewPage(
                    newsLink = newsLink,
                    onBackClicked = { navController.popBackStack() }
                )
            } else {
                // Handle invalid or empty news link
            }
        }

    }
}




@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationDrawer() {
    val navController = rememberNavController()
    val selectedItem = remember { mutableIntStateOf(0) }
    val items = listOf("News", "Favorites")
    
    Scaffold(
        content = {
            if (selectedItem.intValue == 0) {
                PageNavigation(navController = navController)
            }
            if (selectedItem.intValue == 1) {
                FavoritesPage()
            }
        },
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.onPrimary) {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItem.intValue == index,
                        onClick = { selectedItem.intValue = index },
                        label = { Text(text = item)},
                        icon = {
                            when(item) {
                                "News" -> Icon(imageVector = Icons.Filled.Home, contentDescription = item)
                                "Favorites" -> Icon(imageVector = Icons.Filled.Favorite, contentDescription = item)
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.secondary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedTextColor = MaterialTheme.colorScheme.primaryContainer,
                        )
                    )
                }
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun Preview() {

}