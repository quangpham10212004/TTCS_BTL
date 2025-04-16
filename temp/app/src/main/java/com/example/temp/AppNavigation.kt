package com.example.temp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.temp.ui.screens.AuthScreen
import com.example.temp.ui.screens.LoginScreen
import com.example.temp.ui.screens.SignupScreen

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "auth") {
        composable("auth"){
            AuthScreen(modifier = modifier, navController)
        }
        composable("login"){
            LoginScreen(modifier = modifier,navController)
        }
        composable("signup"){
            SignupScreen(modifier = modifier, navController)
        }
    }
}