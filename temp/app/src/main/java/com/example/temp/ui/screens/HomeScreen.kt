package com.example.temp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.navigation.NavController
import com.example.temp.model.NavItem
import com.example.temp.ui.pages.CartPage
import com.example.temp.ui.pages.HomePage
import com.example.temp.ui.pages.ProfilePage


@Composable
fun HomeScreen(modifier: Modifier = Modifier, navController: NavController ) {
    // danh sach cac item trong phan bottomBar
    val navItemList = listOf(
        NavItem("Home", icon = Icons.Default.Home),
        NavItem("Cart", icon = Icons.Default.ShoppingCart),
        NavItem("Profile", icon = Icons.Default.AccountCircle)
    )


    val selectedItem = rememberSaveable {
        mutableStateOf(0)
    } //remember de luu trang thai state, tranh reset moi lan recompose
    // rememberSaveable co tac dung giong remember nhung luu du lieu khi he thong rotation
    Scaffold (
        bottomBar = {
            NavigationBar{
                navItemList.forEachIndexed{ index, item->
                    NavigationBarItem(
                        selected = index == selectedItem.value,
                        onClick = {
                            selectedItem.value = index
                        },
                        icon = {
                            Icon(imageVector = item.icon, contentDescription = item.label)
                        },
                        label = {
                            Text(text = item.label)
                        }

                    )
                }
            }
        }
    ){

        ContentScreen(modifier = modifier.padding(it),selectedItem.value)
    }
}

@Composable
fun ContentScreen(modifier: Modifier = Modifier, selectedIndex: Int) {
    when(selectedIndex){
        0 -> HomePage(modifier)
        1-> CartPage(modifier)
        2-> ProfilePage(modifier)
    }
}