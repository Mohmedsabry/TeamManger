package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.data.local.entity.ChatHistoryEntity
import com.example.data.local.entity.MemberEntity
import com.example.data.local.entity.TaskEntity
import com.example.data.local.entity.TeamEntity

@Dao
interface Dao {
    @Query("delete from member")
    suspend fun deleteAllMembers()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeam(team: TeamEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeams(teams: List<TeamEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMember(member: MemberEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMembers(members: List<MemberEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<TaskEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)

    @Query("SELECT * FROM team")
    suspend fun getAllTeams(): List<TeamEntity>

    @Query("SELECT * FROM member where email=:email")
    suspend fun getMember(email: String): MemberEntity?

    @Query("SELECT * FROM task where due=:email")
    suspend fun getAllTasks(email: String): List<TaskEntity>

    @Query("SELECT * FROM task where id=:teamId")
    suspend fun getAllTasksForTeam(teamId: String): List<TaskEntity>

    @Query("update task set status=:statue where due=:due")
    suspend fun updateTask(due: String, statue: String)

    @Update
    suspend fun updateTeam(team: TeamEntity)

    @Update
    suspend fun updateMember(member: MemberEntity)

    @Query("DELETE FROM team where id=:teamId")
    suspend fun deleteTeam(teamId: String)

    @Query("DELETE FROM member where email=:email")
    suspend fun deleteMember(email: String)

    @Query("DELETE FROM task where id=:taskId")
    suspend fun deleteTask(taskId: String)

    @Query("select * from member")
    suspend fun getAllMembers(): List<MemberEntity>

    @Update
    suspend fun updateTeams(teams: List<TeamEntity>)

    @Update
    suspend fun updateTasks(tasks: List<TaskEntity>)

    @Query("select * from team where id =:teamId")
    suspend fun getTeam(teamId: String): List<TeamEntity>

    @Upsert
    suspend fun addChat(
        chatHistoryEntity: ChatHistoryEntity
    )

    @Query("select * from ChatHistoryEntity where sender = :sender")
    suspend fun getChats(sender: String): List<ChatHistoryEntity>

    @Query("update ChatHistoryEntity set lastMessage=:message where sender=:sender and receiver=:receiver")
    suspend fun updateLastMessage(message: String, sender: String, receiver: String)
}