@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.teammanger.presentation.navigation

import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.teammanger.presentation.add_team.AddTeamEvent
import com.example.teammanger.presentation.add_team.AddTeamScreen
import com.example.teammanger.presentation.add_team.AddTeamVM
import com.example.teammanger.presentation.chat.ChatScreen
import com.example.teammanger.presentation.chat.ChatVm
import com.example.teammanger.presentation.chat_history.ChatHistoryEvents
import com.example.teammanger.presentation.chat_history.ChatHistoryScreen
import com.example.teammanger.presentation.chat_history.ChatHistoryVM
import com.example.teammanger.presentation.component.BottomSheet
import com.example.teammanger.presentation.component.MembersComponent
import com.example.teammanger.presentation.forgetpassword.ForgetPasswordScreen
import com.example.teammanger.presentation.forgetpassword.ForgetPasswordViewModel
import com.example.teammanger.presentation.home.HomeScreen
import com.example.teammanger.presentation.home.HomeVM
import com.example.teammanger.presentation.login.LoginScreen
import com.example.teammanger.presentation.login.LoginViewModel
import com.example.teammanger.presentation.my_tasks.MyTaskVM
import com.example.teammanger.presentation.my_tasks.MyTasksScreen
import com.example.teammanger.presentation.notification.NotificationScreen
import com.example.teammanger.presentation.notification.NotificationVM
import com.example.teammanger.presentation.onboarding.OnBoardingScreen
import com.example.teammanger.presentation.signup.SignUpScreen
import com.example.teammanger.presentation.signup.SignUpViewModel
import com.example.teammanger.presentation.team_details.TeamDetailsEvent
import com.example.teammanger.presentation.team_details.TeamDetailsScreen
import com.example.teammanger.presentation.team_details.TeamDetailsVM
import com.example.teammanger.presentation.ui_model.NavigationItem
import com.example.teammanger.util.getMemberEmail
import com.example.teammanger.util.getOnBoardingStatue
import com.example.teammanger.util.onBoardingDone
import com.example.teammanger.util.saveMember
import kotlinx.coroutines.delay

@Composable
fun NavigationRoutes(
    navHostController: NavHostController,
    paddingValues: PaddingValues
) {
    val context = LocalContext.current
    val isLogin = context.getMemberEmail() != null
    val skipBoarding = context.getOnBoardingStatue()
    val startDest = if (skipBoarding) {
        if (isLogin) NavigationDest.HomeDest(
            context.getMemberEmail() ?: ""
        ) else NavigationDest.LoginDest
    } else {
        NavigationDest.OnBoardingDest
    }
    NavHost(navHostController, startDestination = startDest) {
        composable<NavigationDest.OnBoardingDest> {
            OnBoardingScreen(
                onSignUp = {
                    context.onBoardingDone()
                    navHostController.navigate(NavigationDest.SingUpDest) {
                        launchSingleTop = true
                        popUpTo<NavigationDest.OnBoardingDest> {
                            inclusive = true
                        }
                    }
                },
                onLogin = {
                    context.onBoardingDone()
                    navHostController.navigate(NavigationDest.LoginDest) {
                        launchSingleTop = true
                        popUpTo<NavigationDest.OnBoardingDest> {
                            inclusive = true
                        }
                    }
                },
                modifier = Modifier.padding(paddingValues)
            )
        }
        composable<NavigationDest.SingUpDest> {
            val vm = hiltViewModel<SignUpViewModel>()
            val image by vm.uri.collectAsState()
            val isEmailError by vm.emailErrors.collectAsState()
            val isPasswordError by vm.passwordErrors.collectAsState()
            val isPhoneError by vm.phoneErrors.collectAsState()
            val state = vm.state
            LaunchedEffect(state.email) {
                vm.checkEmail()
            }
            LaunchedEffect(state.password) {
                vm.checkPassword()
            }
            LaunchedEffect(state.phoneNumber) {
                vm.checkPhone()
            }
            LaunchedEffect(state.signedUp) {
                if (state.signedUp) {
                    navHostController.navigate(NavigationDest.LoginDest) {
                        launchSingleTop
                        popUpTo<NavigationDest.SingUpDest> {
                            inclusive = true
                        }
                    }
                }
            }
            LaunchedEffect(state.error) {
                state.error?.let {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                }
            }
            SignUpScreen(
                modifier = Modifier.padding(paddingValues),
                state = state,
                imageUri = image,
                isEmailError = isEmailError,
                isPasswordError = isPasswordError,
                isPhoneError = isPhoneError,
                onEvent = { event ->
                    vm.onEvent(event)
                }
            )
        }
        composable<NavigationDest.LoginDest> {
            val vm = hiltViewModel<LoginViewModel>()
            val state = vm.state
            LaunchedEffect(state.user != null) {
                state.user?.let {
                    context.saveMember(it.email)
                    navHostController.navigate(NavigationDest.HomeDest(it.email)) {
                        launchSingleTop = true
                        popUpTo<NavigationDest.LoginDest> {
                            inclusive = true
                        }
                    }
                }
            }
            LoginScreen(
                state = state,
                onEvent = vm::onEvent,
                onSignUpClicked = {
                    navHostController.navigate(NavigationDest.SingUpDest) {
                        launchSingleTop = true
                    }
                },
                onForgetPasswordClicked = {
                    navHostController.navigate(NavigationDest.ForgetPasswordDest) {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable<NavigationDest.HomeDest> {
            val homeVm = hiltViewModel<HomeVM>()
            val state = homeVm.state
            HomeScreen(
                state,
                onTeamClicked = {
                    navHostController.navigate(
                        NavigationDest.TeamDetails(
                            state.member, it.id
                        )
                    ) {
                        launchSingleTop = true
                    }
                },
                onNavigationClicked = {
                    when (it) {
                        NavigationItem.HOME -> {}
                        NavigationItem.CHAT -> {
                            navHostController.navigate(
                                NavigationDest.ChatHistoryDest(state.member)
                            ) {
                                launchSingleTop = true
                            }
                        }

                        NavigationItem.NOTIFICATION -> {
                            navHostController.navigate(NavigationDest.NotificationDest(state.member)) {
                                launchSingleTop = true
                            }
                        }

                        NavigationItem.TASK -> {
                            navHostController.navigate(
                                NavigationDest.MyTaskDest(state.member)
                            ) {
                                launchSingleTop = true
                            }
                        }
                    }
                },
                onAddTeam = {
                    navHostController.navigate(NavigationDest.AddTeamDest(state.member)) {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable<NavigationDest.ForgetPasswordDest> {
            val fvm = hiltViewModel<ForgetPasswordViewModel>()
            val state = fvm.state
            LaunchedEffect(state.user) {
                state.user?.let {
                    context.saveMember(state.email)
                    delay(500L)
                    navHostController.navigate(NavigationDest.HomeDest(state.email)) {
                        launchSingleTop
                        popUpTo<NavigationDest.LoginDest> { inclusive = true }
                    }
                }
            }
            ForgetPasswordScreen(
                state = state,
                onEvents = fvm::onEvent
            )
        }
        composable<NavigationDest.AddTeamDest> {
            val vm = hiltViewModel<AddTeamVM>()
            val state = vm.state
            val successFullAdd by vm.addedTeam.collectAsState()
            val due by vm.due.collectAsState()
            val taskName by vm.taskName.collectAsState()
            val teamName by vm.teamName.collectAsState()
            val desc by vm.teamDesc.collectAsState()
            LaunchedEffect(successFullAdd) {
                println(successFullAdd)
                if (successFullAdd) {
                    navHostController.popBackStack()
                }
            }
            AddTeamScreen(
                state = state,
                taskName = taskName,
                due = due,
                name = teamName,
                description = desc
            ) { event ->
                when (event) {
                    AddTeamEvent.OnNavigationBack -> navHostController.popBackStack()
                    else -> vm.onEvent(event)
                }
            }
        }
        composable<NavigationDest.ChatDest> {
            val vm = hiltViewModel<ChatVm>()
            val state = vm.state
            ChatScreen(
                state = state,
                onEvent = vm::onEvent,
                connect = vm::connect,
                disConnect = vm::disConnect
            )
        }
        composable<NavigationDest.ChatHistoryDest> {
            val vm = hiltViewModel<ChatHistoryVM>()
            val state = vm.state
            ChatHistoryScreen(state) {
                when (it) {
                    is ChatHistoryEvents.OnChatClicked -> {
                        val email = it.chatHistory.receiver.email
                        val image = it.chatHistory.img
                        navHostController.navigate(
                            NavigationDest.ChatDest(
                                receiver = email,
                                image = image
                            )
                        ) {
                            launchSingleTop = true
                            popUpTo<NavigationDest.ChatHistoryDest> {
                                inclusive = true
                            }
                        }
                    }

                    else -> vm.event(it)
                }
            }
        }
        composable<NavigationDest.NotificationDest> {
            val vm = hiltViewModel<NotificationVM>()
            val state = vm.state
            NotificationScreen(state, onEvent = vm::event)
        }
        composable<NavigationDest.MyTaskDest> {
            val vm = hiltViewModel<MyTaskVM>()
            val state = vm.state
            MyTasksScreen(state, vm::event)
        }
        composable<NavigationDest.TeamDetails> {
            val vm = hiltViewModel<TeamDetailsVM>()
            val state = vm.state
            val showMembers by vm.showAddMember.collectAsState()
            val showAddTasks by vm.showAddTask.collectAsState()
            val desc by vm.desc.collectAsState()
            val bottom = rememberModalBottomSheetState(true)
            LaunchedEffect(showAddTasks) {
                if (showAddTasks) {
                    bottom.show()
                } else {
                    bottom.hide()
                }
            }
            if (showMembers) {
                MembersComponent(
                    members = state.members - state.team.members.toSet(),
                    selectedMembers = state.team.members,
                    onCheckedClicked = { isChecked, member ->
                        vm.onEvent(TeamDetailsEvent.OnAddMember(isChecked, member))
                    },
                    onDismiss = { vm.onEvent(TeamDetailsEvent.Dismiss) }
                )
            }
            BottomSheet(
                desc = desc,
                emails = state.members.map { it.email + vm.splitter + it.name },
                sheetState = bottom,
                onDescChange = { vm.onEvent(TeamDetailsEvent.OnDescChange(it)) },
                onDateChange = { vm.onEvent(TeamDetailsEvent.OnDateChange(it)) },
                onDueChange = { vm.onEvent(TeamDetailsEvent.OnDeuChange(it)) },
                onAddTask = { vm.onEvent(TeamDetailsEvent.OnAddTask) }
            )
            TeamDetailsScreen(state, vm::onEvent)
        }
    }
}