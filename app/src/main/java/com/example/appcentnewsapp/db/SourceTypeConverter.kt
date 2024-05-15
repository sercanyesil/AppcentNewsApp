package com.example.appcentnewsapp.db

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.appcentnewsapp.model.Source
import com.google.gson.Gson

@ProvidedTypeConverter
class SourceTypeConverter {
    @TypeConverter
    fun fromSource(source: Source): String {
        return Gson().toJson(source)
    }

    @TypeConverter
    fun toSource(source: String): Source {
        return Gson().fromJson(source, Source::class.java)
    }
}