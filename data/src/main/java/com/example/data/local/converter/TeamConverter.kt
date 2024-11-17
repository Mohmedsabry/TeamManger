package com.example.data.local.converter

import androidx.room.TypeConverter
import com.google.gson.Gson

class TeamConverter {
    @TypeConverter
    fun fromTeamList(teams: List<String>): String {
        return Gson().toJson(teams)
    }

    @TypeConverter
    fun toTeamList(json: String): List<String> {
        return Gson().fromJson(json, Array<String>::class.java).toList()
    }

}