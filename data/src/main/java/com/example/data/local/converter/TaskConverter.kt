package com.example.data.local.converter

import androidx.room.TypeConverter
import com.example.data.local.entity.TaskEntity
import com.google.gson.Gson

class TaskConverter {
    @TypeConverter
    fun fromTaskList(tasks: List<TaskEntity>): String {
        return Gson().toJson(tasks)
    }

    @TypeConverter
    fun toTaskList(json: String): List<TaskEntity> {
        return Gson().fromJson(json, Array<TaskEntity>::class.java).toList()
    }

}