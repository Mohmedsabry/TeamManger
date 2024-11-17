package com.example.di

import android.content.Context
import androidx.room.Room
import com.example.data.local.RoomDB
import com.example.data.remote.AuthApi
import com.example.data.remote.MainAPi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.WebSockets
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideAuthApi(): AuthApi = Retrofit.Builder()
        .client(
            OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)  // Connection timeout
                .readTimeout(30, TimeUnit.SECONDS)     // Read timeout
                .writeTimeout(15, TimeUnit.SECONDS)    // Write timeout
                .build()
        )
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(MainAPi.BASE_URL)
        .build()
        .create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideMainApi(): MainAPi = Retrofit.Builder()
        .baseUrl(MainAPi.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(MainAPi::class.java)

    @Provides
    @Singleton
    fun provideRoomDB(@ApplicationContext context: Context): RoomDB = Room.databaseBuilder(
        context = context,
        klass = RoomDB::class.java,
        name = "room_db"
    ).build()

    @Provides
    @Singleton
    fun provideDao(roomDB: RoomDB) = roomDB.dao

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient =
        HttpClient(CIO) {
            install(ContentNegotiation)
            install(WebSockets)
        }
}