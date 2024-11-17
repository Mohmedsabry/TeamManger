package com.example.teammanger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.teammanger.presentation.navigation.NavigationRoutes
import com.example.teammanger.ui.theme.TeamMangerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TeamMangerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navHostController = rememberNavController()
                    NavigationRoutes(navHostController,innerPadding)
                }
            }
        }
    }
}