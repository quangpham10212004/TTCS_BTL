package com.example.temp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.temp.components.TPThanhTimKiem
import com.example.temp.ui.pages.GDCacSPDuocTimKiem
import com.example.temp.ui.pages.GDGioHang
import com.example.temp.ui.pages.GDSPTungBrand
import com.example.temp.ui.pages.GDThanhToan
import com.example.temp.ui.pages.GDThongTinLaptop


import com.example.temp.ui.screens.GDAuth
import com.example.temp.ui.screens.GDDangKy
import com.example.temp.ui.screens.GDDangNhap
import com.example.temp.ui.screens.GDHome

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
            GDAuth(modifier = modifier, navController)
        }
        composable("login"){ //
            GDDangNhap(modifier = modifier,navController)
        }
        composable("signup"){
            GDDangKy(modifier = modifier, navController)
        }

        composable(route = "home"){
            GDHome(modifier = modifier, navController)
        }

        composable(route = "category-products/{categoryId}") { // template category-products/{categoryId}
            var categoryId = it.arguments?.getString("categoryId")
            GDSPTungBrand( modifier,categoryId?:"")
        }

        composable(route = "laptop-detail/{laptopId}") {
            var laptopId = it.arguments?.getString("laptopId")
            GDThongTinLaptop( modifier,laptopId?:"")
        }

        composable (route = "checkout") {
            GDThanhToan(modifier, navController)
        }

        composable ("search") {
            TPThanhTimKiem(modifier, navController)
        }
        composable (route = "search/{searchText}") {
            var searchText = it.arguments?.getString("searchText")
            GDCacSPDuocTimKiem(modifier,searchText?:"")
        }

        composable ("cart"){
            GDGioHang(modifier)
        }
    }
}

object GlobalNavigation{
    lateinit var navController: NavHostController
}