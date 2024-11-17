package com.example.domain.repositories

import com.example.domain.errors.GlobalError
import com.example.domain.errors.NetWorkError
import com.example.domain.model.ChatHistory
import com.example.domain.model.Member
import com.example.domain.model.Message
import com.example.domain.model.Notification
import com.example.domain.model.Request
import com.example.domain.model.Task
import com.example.domain.model.Team
import com.example.domain.util.RequestStatue
import com.example.domain.util.Result

interface Repository {
    suspend fun addTask(task: Task): Result<Unit, NetWorkError>
    suspend fun deleteTask(task: Task): Result<Unit, NetWorkError>
    suspend fun addTeam(team: Team): Result<Unit, NetWorkError>
    suspend fun getTeam(teamId: String): Result<Team, NetWorkError>
    suspend fun deleteTeam(teamId: String): Result<Unit, NetWorkError>
    suspend fun updateTaskStatue(
        taskFinisher: String,
        statue: Boolean
    ): Result<Unit, NetWorkError>

    suspend fun getAllMember(): Result<List<Member>, NetWorkError>

    suspend fun getAllMemberForTeam(teamId: String): Result<List<Member>, NetWorkError>
    suspend fun getAllTeamsForMember(memberId: String): Result<List<Team>, NetWorkError>
    suspend fun getAllTasksForMember(email: String): Result<List<Task>, NetWorkError>
    suspend fun getAllTasksForTeam(teamId: String): Result<List<Task>, NetWorkError>
    suspend fun addMemberToTeam(
        teamId: String,
        memberEmail: String,
    ): Result<Unit, NetWorkError>

    suspend fun deleteMemberFromTeam(
        teamId: String,
        memberEmail: String
    ): Result<Unit, NetWorkError>

    suspend fun getAllRequestsForMember(
        memberEmail: String
    ): Result<List<Request>, NetWorkError>

    suspend fun updateRequestStatue(
        statue: RequestStatue,
        id: String
    ): Result<Unit, NetWorkError>

    suspend fun getNotification(
        who: String
    ): Result<List<Notification>, NetWorkError>

    suspend fun clearNotification(who: String): Result<Unit, NetWorkError>
    suspend fun getChatMessages(
        receiver: String,
    ): Result<List<Message>, NetWorkError>

    suspend fun getAllChatHistory(
    ): Result<List<ChatHistory>, GlobalError>
}