package com.example.appcentnewsapp.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.appcentnewsapp.model.News
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(news: News)

    @Delete
    suspend fun deleteNews(news: News)

    @Query("SELECT * FROM News")
    fun getAllFavoriteNews(): Flow<List<News>>
}