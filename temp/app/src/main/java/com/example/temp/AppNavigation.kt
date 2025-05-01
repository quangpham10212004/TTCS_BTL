package com.example.temp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.temp.ui.pages.CategoryProductsPage
import com.example.temp.ui.screens.AuthScreen
import com.example.temp.ui.screens.HomeScreen
import com.example.temp.ui.screens.LoginScreen
import com.example.temp.ui.screens.SignupScreen
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val isLoggedIn = Firebase.auth.currentUser != null
    val firstPage = if (isLoggedIn) {
        "home"
    } else "auth"
    val navController = rememberNavController()

    GlobalNavigation.navController = navController
    NavHost(navController = navController, startDestination = firstPage ) {
        composable("auth"){ // composable co tac dung dieu huong den giao dien "route"
            AuthScreen(modifier = modifier, navController)
        }
        composable("login"){
            LoginScreen(modifier = modifier,navController)
        }
        composable("signup"){
            SignupScreen(modifier = modifier, navController)
        }

        composable(route = "home"){
            HomeScreen(modifier = modifier, navController)
        }

        composable(route = "category-products/{categoryId}") {
            var categoryId = it.arguments?.getString("categoryId")
            CategoryProductsPage( modifier,categoryId?:"")
        }
    }
}

object GlobalNavigation{
    lateinit var navController: NavHostController
}