package com.example.data.repositories

import android.content.Context
import android.net.Uri
import com.example.data.local.RoomDB
import com.example.data.mapper.toEntity
import com.example.data.mapper.toUser
import com.example.data.remote.AuthApi
import com.example.data.remote.dto.RequestPasswordDto
import com.example.data.remote.dto.UserDto
import com.example.domain.errors.NetWorkError
import com.example.domain.model.User
import com.example.domain.repositories.AuthRepository
import com.example.domain.util.Result
import com.example.util.Constants.NO_IMAGE
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okio.IOException
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    @ApplicationContext private val context: Context,
    private val db: RoomDB
) : AuthRepository {
    private val dao = db.dao
    override suspend fun signUp(
        name: String,
        email: String,
        password: String,
        age: Double,
        phoneNumber: String,
        gender: String,
        img: String
    ): Result<Unit, NetWorkError> {
        return withContext(Dispatchers.IO) {
            try {
                val user = UserDto(
                    username = name,
                    email = email,
                    password = password,
                    age = age,
                    phoneNumber = phoneNumber,
                    gender = gender,
                    image = img
                )
                val encode = Json.encodeToString(user)
                if (img == NO_IMAGE) {
                    authApi.signup(
                        MultipartBody.Part.createFormData("user", encode)
                    )
                } else {
                    val file = File("${context.cacheDir}/$name.img")
                    val input = context.contentResolver.openInputStream(Uri.parse(img))
                    val outPut = FileOutputStream(file)
                    input?.copyTo(outPut)
                    input?.close()
                    outPut.close()
                    authApi.signup(
                        user = MultipartBody.Part.createFormData("user", encode),
                        image = MultipartBody.Part.createFormData(
                            name = "image",
                            filename = file.name,
                            body = file.asRequestBody()
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

    override suspend fun signIn(
        email: String,
        password: String
    ): Result<User, NetWorkError> {
        return withContext(Dispatchers.IO) {
            try {
                val user = authApi.login(email, password)
                val member = dao.getMember(user.email)
                member.let {
                    dao.insertMember(user.toEntity())
                }
                Result.Success(user.toUser())
            } catch (e: IOException) {
                e.printStackTrace()
                Result.Failure(NetWorkError.NO_INTERNET_CONNECTION)
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Failure(NetWorkError.UNKnown)
            }
        }
    }

    override suspend fun forgetPassword(
        email: String
    ): Result<Unit, NetWorkError> {
        return withContext(Dispatchers.IO) {
            try {
                authApi.forgetPassword(email)
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

    override suspend fun checkCode(
        code: String,
        email: String
    ): Result<Unit, NetWorkError> {
        return withContext(Dispatchers.IO) {
            try {
                authApi.checkCode(RequestPasswordDto(email, code))
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

    override suspend fun resetPassword(
        password: String,
        email: String
    ): Result<User, NetWorkError> {
        return withContext(Dispatchers.IO) {
            try {
                val user = authApi.updatePassword(email, password)
                dao.updateMember(user.toEntity())
                Result.Success(user.toUser())
            } catch (e: IOException) {
                e.printStackTrace()
                Result.Failure(NetWorkError.NO_INTERNET_CONNECTION)
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Failure(NetWorkError.UNKnown)
            }
        }
    }
}