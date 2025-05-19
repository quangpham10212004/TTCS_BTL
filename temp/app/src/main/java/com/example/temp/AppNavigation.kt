package com.example.temp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.temp.components.SearchBarView

import com.example.temp.ui.pages.BrandProductsPage
import com.example.temp.ui.pages.CheckoutPage

import com.example.temp.ui.pages.LaptopDetailPage
import com.example.temp.ui.pages.SearchItemsPage
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
        composable("login"){ //
            LoginScreen(modifier = modifier,navController)
        }
        composable("signup"){
            SignupScreen(modifier = modifier, navController)
        }

        composable(route = "home"){
            HomeScreen(modifier = modifier, navController)
        }

        composable(route = "category-products/{categoryId}") { // template category-products/{categoryId}
            var categoryId = it.arguments?.getString("categoryId")
            BrandProductsPage( modifier,categoryId?:"")
        }

        composable(route = "laptop-detail/{laptopId}") {
            var laptopId = it.arguments?.getString("laptopId")
            LaptopDetailPage( modifier,laptopId?:"")
        }

        composable (route = "checkout") {
            CheckoutPage(modifier, navController)
        }

        composable ("search") {
            SearchBarView(modifier, navController)
        }
        composable (route = "search/{searchText}") {
            var searchText = it.arguments?.getString("searchText")
            SearchItemsPage(modifier,searchText?:"")
        }
    }
}

object GlobalNavigation{
    lateinit var navController: NavHostController
}