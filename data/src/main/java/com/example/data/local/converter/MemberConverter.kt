package com.example.data.local.converter

import androidx.room.TypeConverter
import com.example.data.local.entity.MemberEntity
import com.google.gson.Gson

class MemberConverter {
    @TypeConverter
    fun fromMemberList(members: List<MemberEntity>): String {
        return Gson().toJson(members)
    }

    @TypeConverter
    fun toMemberList(json: String): List<MemberEntity> {
        return Gson().fromJson(json, Array<MemberEntity>::class.java).toList()
    }

}