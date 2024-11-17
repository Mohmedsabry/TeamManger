package com.example.di

import com.example.data.remote.manager.ChatController
import com.example.data.remote.manager.ChatControllerImpl
import com.example.data.repositories.AuthRepositoryImpl
import com.example.data.repositories.RepositoryImpl
import com.example.domain.repositories.AuthRepository
import com.example.domain.repositories.Repository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepo(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindRepo(repositoryImpl: RepositoryImpl): Repository

    @Binds
    @Singleton
    abstract fun bindChatController(chatControllerImpl: ChatControllerImpl): ChatController
}