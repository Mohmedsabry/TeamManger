package com.example.teammanger.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface NavigationDest {
    @Serializable
    data object OnBoardingDest : NavigationDest

    @Serializable
    data object SingUpDest : NavigationDest

    @Serializable
    data object LoginDest : NavigationDest

    @Serializable
    data class HomeDest(val email: String) : NavigationDest

    @Serializable
    data object ForgetPasswordDest : NavigationDest

    @Serializable
    data class AddTeamDest(val email: String) : NavigationDest

    @Serializable
    data class ChatHistoryDest(val email: String) : NavigationDest

    @Serializable
    data class NotificationDest(val email: String) : NavigationDest

    @Serializable
    data class MyTaskDest(val email: String) : NavigationDest

    @Serializable
    data class ChatDest(val receiver: String, val image: String) : NavigationDest

    @Serializable
    data class TeamDetails(val email: String, val teamId: String) : NavigationDest

}