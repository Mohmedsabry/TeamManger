package com.example.data.repositories

import android.content.Context
import androidx.room.withTransaction
import com.example.data.local.Dao
import com.example.data.local.RoomDB
import com.example.data.mapper.toChat
import com.example.data.mapper.toDto
import com.example.data.mapper.toEntity
import com.example.data.mapper.toMember
import com.example.data.mapper.toMessage
import com.example.data.mapper.toNotification
import com.example.data.mapper.toRequest
import com.example.data.mapper.toTask
import com.example.data.mapper.toTeam
import com.example.data.remote.MainAPi
import com.example.domain.errors.GlobalError
import com.example.domain.errors.NetWorkError
import com.example.domain.model.ChatHistory
import com.example.domain.model.Member
import com.example.domain.model.Message
import com.example.domain.model.Notification
import com.example.domain.model.Request
import com.example.domain.model.Task
import com.example.domain.model.Team
import com.example.domain.repositories.Repository
import com.example.domain.util.RequestStatue
import com.example.domain.util.Result
import com.example.domain.util.TaskStatue
import com.example.util.getMemberEmail
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import okio.IOException
import java.util.Date
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val api: MainAPi,
    @ApplicationContext private val context: Context,
    private val dao: Dao,
    private val db: RoomDB
) : Repository {

    override suspend fun addTask(
        task: Task
    ): Result<Unit, NetWorkError> {
        return withContext(Dispatchers.IO) {
            try {
                db.withTransaction {
                    listOf(
                        async { dao.insertTask(task.toEntity()) },
                        // add task for team and member
                        async {
                            val member = dao.getMember(task.due)
                            member?.let {
                                dao.updateMember(it.copy(tasks = it.tasks + task.toEntity()))
                            }
                        },
                        async {
                            val team = dao.getTeam(teamId = task.teamId)
                            team.firstOrNull()?.let {
                                dao.updateTeam(
                                    it.copy(
                                        tasks = it.tasks + task.toEntity(),
                                        members = it.members.map { member ->
                                            member.copy(
                                                tasks = member.tasks + task.toEntity()
                                            )
                                        }
                                    )
                                )
                            }
                        },
                        async { api.addTask(task.toDto()) }
                    ).awaitAll()
                }
                Result.Success(Unit)
            } catch (e: IOException) {
                e.printStackTrace()
                Result.Failure(NetWorkError.NO_INTERNET_CONNECTION)
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Failure(NetWorkError.UNKnown)
            }
        }
    }

    override suspend fun deleteTask(task: Task): Result<Unit, NetWorkError> {
        return withContext(Dispatchers.IO) {
            try {
                async { api.deleteTask(task.toDto()) }.await()
                async { dao.deleteTask(task.id) }.await()
                // get member and delete that task ,same for team
                async {
                    val member = dao.getMember(task.due)
                    member?.let {
                        dao.updateMember(it.copy(tasks = it.tasks - task.toEntity()))
                    }
                }.await()
                async {
                    val team = dao.getTeam(teamId = task.teamId)
                    team.firstOrNull()?.let {
                        dao.updateTeam(
                            it.copy(
                                tasks = it.tasks - task.toEntity(),
                                members = it.members.map { member ->
                                    member.copy(
                                        tasks = member.tasks - task.toEntity()
                                    )
                                }
                            )
                        )
                    }
                }.await()
                Result.Success(Unit)
            } catch (e: IOException) {
                e.printStackTrace()
                Result.Failure(NetWorkError.NO_INTERNET_CONNECTION)
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Failure(NetWorkError.UNKnown)
            }
        }
    }

    override suspend fun addTeam(
        team: Team
    ): Result<Unit, NetWorkError> {
        return withContext(Dispatchers.IO) {
            try {
                val remoteMap = api.addTeam(team.toDto())
                val teamId = remoteMap["teamId"]?.first() ?: ""
                val tasksIds = remoteMap["tasks"] ?: listOf()
                val teamEntity = team.toEntity().copy(id = teamId)
                val tasks =
                    teamEntity.tasks.mapIndexed { index, task ->
                        task.copy(
                            teamId = teamEntity.id,
                            id = tasksIds[index]
                        )
                    }
                val members = teamEntity.members.map { member ->
                    async {
                        member.copy(
                            tasks = tasks.filter { it.due == member.email },
                            teams = member.teams + teamEntity.id
                        )
                    }
                }
                dao.insertTasks(tasks)
                dao.insertTeam(
                    teamEntity.copy(
                        members = members.awaitAll(),
                        tasks = tasks
                    )
                )
                members.forEach {
                    dao.updateMember(it.await())
                }
                val email = context.getMemberEmail() ?: ""
                if (teamEntity.admin == email) {
                    val memberDB = dao.getMember(email)
                    memberDB?.let { member ->
                        dao.updateMember(
                            member.copy(
                                teams = member.teams + teamEntity.id,
                                tasks = member.tasks + tasks.filter { it.due == email || it.creator == email }
                            )
                        )
                    }
                }
                Result.Success(Unit)
            } catch (e: IOException) {
                e.printStackTrace()
                Result.Failure(NetWorkError.NO_INTERNET_CONNECTION)
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Failure(NetWorkError.UNKnown)
            }
        }
    }

    override suspend fun deleteTeam(
        teamId: String
    ): Result<Unit, NetWorkError> {
        return withContext(Dispatchers.IO) {
            try {
                val team = dao.getTeam(teamId).firstOrNull()
                async { api.deleteTeam(teamId) }.await()
                async { dao.deleteTeam(teamId) }.await()
                team?.let { teams ->
                    teams.members.map { member ->
                        async {
                            dao.updateMember(
                                member.copy(
                                    teams = member.teams - teamId,
                                    tasks = member.tasks.filter { it.teamId != teamId }
                                )
                            )
                        }
                    }.awaitAll()
                    teams.tasks.map {
                        async {
                            dao.deleteTask(it.id)
                        }
                    }.awaitAll()
                }
                Result.Success(Unit)
            } catch (e: IOException) {
                e.printStackTrace()
                Result.Failure(NetWorkError.NO_INTERNET_CONNECTION)
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Failure(NetWorkError.UNKnown)
            }
        }
    }

    override suspend fun updateTaskStatue(
        taskFinisher: String,
        statue: Boolean
    ): Result<Unit, NetWorkError> {
        return withContext(Dispatchers.IO) {
            try {
                api.finishTask(taskFinisher, statue)
                dao.updateTask(taskFinisher, TaskStatue.DONE.name.lowercase())
                Result.Success(Unit)
            } catch (e: IOException) {
                e.printStackTrace()
                Result.Failure(NetWorkError.NO_INTERNET_CONNECTION)
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Failure(NetWorkError.UNKnown)
            }
        }
    }

    override suspend fun getAllMember(): Result<List<Member>, NetWorkError> {
        return withContext(Dispatchers.IO) {
            try {
                val members = api.getAllMembers()
                db.withTransaction {
                    dao.deleteAllMembers()
                    dao.insertMembers(members.map { it.toMember().toEntity() })
                }
                Result.Success(members.map { it.toMember() })
            } catch (e: IOException) {
                e.printStackTrace()
                println("offline")
                val offlineMembers = dao.getAllMembers()
                Result.Failure(
                    NetWorkError.NO_INTERNET_CONNECTION,
                    offlineMembers.map { it.toMember() }
                )
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Failure(NetWorkError.UNKnown)
            }
        }
    }

    override suspend fun getAllMemberForTeam(
        teamId: String
    ): Result<List<Member>, NetWorkError> {
        return withContext(Dispatchers.IO) {
            try {
                val members = api.getAllMemberForTeam(teamId)
                Result.Success(members.map { it.toMember() })
            } catch (e: IOException) {
                e.printStackTrace()
                val members = dao.getTeam(teamId).first().members
                Result.Failure(
                    NetWorkError.NO_INTERNET_CONNECTION,
                    optional = members.map { it.toMember() }
                )
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Failure(NetWorkError.UNKnown)
            }
        }
    }

    override suspend fun getAllTeamsForMember(
        memberId: String
    ): Result<List<Team>, NetWorkError> {
        return withContext(Dispatchers.IO) {
            try {
                val teams = api.getAllTeamsForMember(memberId)
                Result.Success(teams.map { it.toTeam() })
            } catch (e: IOException) {
                e.printStackTrace()
                val memberDB = dao.getMember(memberId)
                memberDB?.let { member ->
                    val teamsId = member.teams
                    val teams = teamsId.map {
                        async {
                            dao.getTeam(it).first()
                        }
                    }.awaitAll()
                    Result.Failure(
                        NetWorkError.NO_INTERNET_CONNECTION,
                        teams.map { it.toTeam() }
                    )
                }
                Result.Failure(NetWorkError.NO_INTERNET_CONNECTION)
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Failure(NetWorkError.UNKnown)
            }
        }
    }

    override suspend fun getAllTasksForMember(
        email: String
    ): Result<List<Task>, NetWorkError> {
        return withContext(Dispatchers.IO) {
            try {
                val tasks = api.getAllTasksForMember(
                    email
                )
                Result.Success(tasks.map {
                    println(Date(it.deadLine).toString())
                    it.toTask()
                })
            } catch (e: IOException) {
                e.printStackTrace()
                val tasks = dao.getAllTasks(email)
                Result.Failure(
                    NetWorkError.NO_INTERNET_CONNECTION,
                    tasks.map { it.toTask() }
                )
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Failure(NetWorkError.UNKnown)
            }
        }
    }

    override suspend fun getAllTasksForTeam(
        teamId: String
    ): Result<List<Task>, NetWorkError> {
        return withContext(Dispatchers.IO) {
            try {
                val tasks = api.getAllTasksForTeam(teamId)
                Result.Success(tasks.map { it.toTask() })
            } catch (e: IOException) {
                e.printStackTrace()
                val tasks = dao.getAllTasksForTeam(teamId)
                Result.Failure(
                    NetWorkError.NO_INTERNET_CONNECTION,
                    tasks.map { it.toTask() }
                )
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Failure(NetWorkError.UNKnown)
            }
        }
    }

    override suspend fun addMemberToTeam(
        teamId: String,
        memberEmail: String,
    ): Result<Unit, NetWorkError> {
        return withContext(Dispatchers.IO) {
            try {
                api.addMemberToTeam(email = memberEmail, teamId = teamId)
                val memberEntity = dao.getMember(memberEmail)
                val teamEntity = dao.getTeam(teamId).firstOrNull()
                memberEntity?.let { member ->
                    teamEntity?.let { team ->
                        dao.updateTeam(team.copy(members = team.members + member))
                    }
                }
                Result.Success(Unit)
            } catch (e: IOException) {
                e.printStackTrace()
                Result.Failure(NetWorkError.NO_INTERNET_CONNECTION)
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Failure(NetWorkError.UNKnown)
            }
        }
    }

    override suspend fun deleteMemberFromTeam(
        teamId: String,
        memberEmail: String
    ): Result<Unit, NetWorkError> {
        return withContext(Dispatchers.IO) {
            try {
                async { api.deleteTeamMember(teamId, memberEmail) }.await()
                val team = dao.getTeam(teamId).first()
                val memberEntity = dao.getMember(memberEmail)
                memberEntity?.let { member ->
                    dao.updateTeam(
                        team.copy(
                            members = team.members - member,
                            tasks = team.tasks - member.tasks.toSet()
                        )
                    )
                }
                Result.Success(Unit)
            } catch (e: IOException) {
                e.printStackTrace()
                Result.Failure(NetWorkError.NO_INTERNET_CONNECTION)
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Failure(NetWorkError.UNKnown)
            }
        }
    }

    override suspend fun getAllRequestsForMember(
        memberEmail: String
    ): Result<List<Request>, NetWorkError> {
        return withContext(Dispatchers.IO) {
            try {
                val requests = api.getAllRequestsForMember(memberEmail)
                Result.Success(requests.map { it.toRequest(context.getMemberEmail() ?: "") })
            } catch (e: IOException) {
                e.printStackTrace()
                Result.Failure(NetWorkError.NO_INTERNET_CONNECTION)
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Failure(NetWorkError.UNKnown)
            }
        }
    }

    override suspend fun updateRequestStatue(
        statue: RequestStatue,
        id: String
    ): Result<Unit, NetWorkError> {
        return withContext(Dispatchers.IO) {
            try {
                api.updateRequest(
                    id,
                    statue.name.lowercase()
                )
                Result.Success(Unit)
            } catch (e: IOException) {
                e.printStackTrace()
                Result.Failure(NetWorkError.NO_INTERNET_CONNECTION)
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Failure(NetWorkError.UNKnown)
            }
        }
    }

    override suspend fun getNotification(
        who: String
    ): Result<List<Notification>, NetWorkError> {
        return withContext(Dispatchers.IO) {
            try {
                val notification = api.getAllNotification(email = who)
                Result.Success(notification.map { it.toNotification() })
            } catch (e: IOException) {
                e.printStackTrace()
                Result.Failure(NetWorkError.NO_INTERNET_CONNECTION)
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Failure(NetWorkError.UNKnown)
            }
        }
    }

    override suspend fun clearNotification(
        who: String
    ): Result<Unit, NetWorkError> {
        return withContext(Dispatchers.IO) {
            try {
                api.deleteNotifications(who)
                Result.Success(Unit)
            } catch (e: IOException) {
                e.printStackTrace()
                Result.Failure(NetWorkError.NO_INTERNET_CONNECTION)
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Failure(NetWorkError.UNKnown)
            }
        }
    }

    override suspend fun getChatMessages(
        receiver: String
    ): Result<List<Message>, NetWorkError> {
        return withContext(Dispatchers.IO) {
            try {
                val sender = context.getMemberEmail() ?: ""
                val messages = api.getAllChatMessages(
                    sender, receiver
                )
                Result.Success(messages.map { it.toMessage() })
            } catch (e: IOException) {
                e.printStackTrace()
                Result.Failure(NetWorkError.NO_INTERNET_CONNECTION)
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Failure(NetWorkError.UNKnown)
            }
        }
    }

    override suspend fun getAllChatHistory(): Result<List<ChatHistory>, GlobalError> {
        return withContext(Dispatchers.IO) {
            try {
                val email = context.getMemberEmail() ?: return@withContext Result.Failure(
                    GlobalError.NO_SUCH_USER
                )
                val chats = dao.getChats(email)
                val members = chats.map {
                    api.getMember(it.receiver).toMember()
                }
                Result.Success(chats.mapIndexed { index, chatHistoryEntity ->
                    chatHistoryEntity.toChat(members[index])
                })
            } catch (e: Exception) {
                e.printStackTrace()
                val email = context.getMemberEmail() ?: return@withContext Result.Failure(
                    GlobalError.NO_SUCH_USER
                )
                val chats = dao.getChats(email)
                val members = chats.map {
                    dao.getMember(it.receiver)?.toMember() ?: Member(
                        gender = "male",
                        image = "no image",
                        name = it.receiver
                    )
                }
                Result.Failure(
                    GlobalError.UNKNOWN_ERROR,
                    optional = chats.mapIndexed { index, chatHistoryEntity ->
                        chatHistoryEntity.toChat(members[index])
                    })
            }
        }
    }

    override suspend fun getTeam(teamId: String): Result<Team, NetWorkError> {
        return withContext(Dispatchers.IO) {
            try {
                val team = api.getTeam(teamId)
                Result.Success(team.toTeam())
            } catch (e: IOException) {
                e.printStackTrace()
                val team = dao.getTeam(teamId).first()
                Result.Failure(NetWorkError.NO_INTERNET_CONNECTION, optional = team.toTeam())
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Failure(NetWorkError.UNKnown)
            }
        }
    }

}