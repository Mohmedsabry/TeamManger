package com.example.data.remote

import com.example.data.remote.dto.MemberDto
import com.example.data.remote.dto.MessageDto
import com.example.data.remote.dto.NotificationDto
import com.example.data.remote.dto.RequestDto
import com.example.data.remote.dto.TaskDto
import com.example.data.remote.dto.TeamDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface MainAPi {

    @GET("/members")
    suspend fun getAllMembers(): List<MemberDto>

    @GET("/member")
    suspend fun getMember(
        @Query("email") email: String
    ): MemberDto

    @POST("/member")
    suspend fun addMemberToTeam(
        @Query("email") email: String,
        @Query("teamId") teamId: String
    )

    @POST("/task")
    suspend fun addTask(
        @Body task: TaskDto
    )

    @POST("/task")
    suspend fun deleteTask(
        @Body task: TaskDto
    )

    @PUT("/task")
    suspend fun finishTask(
        @Query("due") email: String,
        @Query("statue") statue: Boolean
    )

    @GET("/team")
    suspend fun getTeam(
        @Query("teamId") teamId: String
    ): TeamDto

    @POST("/team")
    suspend fun addTeam(
        @Body team: TeamDto
    ): Map<String, List<String>>

    @DELETE("/team")
    suspend fun deleteTeam(
        @Query("teamId") teamId: String
    )

    @GET("/team/{teamId}")
    suspend fun getAllMemberForTeam(
        @Path("teamId") teamId: String,
    ): List<MemberDto>

    @GET("/members/{memberId}")
    suspend fun getAllTeamsForMember(
        @Path("memberId") memberId: String
    ): List<TeamDto>

    @GET("/task/{email}")
    suspend fun getAllTasksForMember(
        @Path("email") email: String
    ): List<TaskDto>

    @GET("/task/{teamId}")
    suspend fun getAllTasksForTeam(
        @Path("teamId") teamId: String
    ): List<TaskDto>

    @POST("/join")
    suspend fun joinTeam(
        @Query("teamId") teamId: String,
        @Query("memberEmail") memberEmail: String,
        @Query("admin") admin: String
    )

    @DELETE("/teamMember")
    suspend fun deleteTeamMember(
        @Query("teamId") teamId: String,
        @Query("email") memberEmail: String
    )

    @GET("/request")
    suspend fun getAllRequestsForMember(
        @Query("email") email: String
    ): List<RequestDto>

    @PUT("/request/{id}")
    suspend fun updateRequest(
        @Path("id") id: String,
        @Query("statue") statue: String // rejected || accepted
    )

    @GET("/notification")
    suspend fun getAllNotification(
        @Query("email") email: String
    ): List<NotificationDto>

    @DELETE("/notification")
    suspend fun deleteNotifications(
        @Query("email") email: String
    )

    @GET("messages")
    suspend fun getAllChatMessages(
        @Query("sender") sender: String,
        @Query("receiver") receiver: String
    ): List<MessageDto>

    companion object {
        const val BASE_URL = "http://10.0.2.2:8080/"
    }
}