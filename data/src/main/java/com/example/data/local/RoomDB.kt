package com.example.data.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.local.converter.MemberConverter
import com.example.data.local.converter.TaskConverter
import com.example.data.local.converter.TeamConverter
import com.example.data.local.entity.ChatHistoryEntity
import com.example.data.local.entity.MemberEntity
import com.example.data.local.entity.TaskEntity
import com.example.data.local.entity.TeamEntity

@Database(
    entities = [TeamEntity::class, MemberEntity::class, TaskEntity::class, ChatHistoryEntity::class],
    version = 3,
    autoMigrations = [AutoMigration(from = 1, to = 2), AutoMigration(2, 3)]
)
@TypeConverters(
    TeamConverter::class,
    MemberConverter::class,
    TaskConverter::class
)
abstract class RoomDB : RoomDatabase() {
    abstract val dao: Dao
}