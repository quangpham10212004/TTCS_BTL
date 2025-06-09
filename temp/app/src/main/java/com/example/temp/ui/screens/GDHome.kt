package com.example.temp.ui.screens


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.temp.model.NavItem
import com.example.temp.model.UserModel
import com.example.temp.ui.pages.GDDonHang
import com.example.temp.ui.pages.GDGioHang
import com.example.temp.ui.pages.GDQuanLyDonHang
import com.example.temp.ui.pages.GDQuanLyTaiKhoan
import com.example.temp.ui.pages.GDThongTinCaNhan
import com.example.temp.ui.pages.HomePage
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore


@Composable
fun GDHome(modifier: Modifier = Modifier, navController: NavController) {
    val navItemList = listOf(
        NavItem("Home", icon = Icons.Default.Home),
        NavItem("Cart", icon = Icons.Default.ShoppingCart),
        NavItem("Orders", icon = Icons.Default.DateRange),
        NavItem("Profile", icon = Icons.Default.AccountCircle),
    )

    val navItemListAdmin = listOf(
        NavItem("Home", icon = Icons.Default.Home),
        NavItem("Orders", icon = Icons.Default.ShoppingCart),
        NavItem("Account", icon = Icons.Default.AccountBox),
        NavItem("Profile", icon = Icons.Default.AccountCircle),
    )

    val user = remember { mutableStateOf<UserModel?>(null) }
    val isLoading = remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            Firebase.firestore.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener { doc ->
                    user.value = doc.toObject(UserModel::class.java)
                    isLoading.value = false
                }
                .addOnFailureListener {
                    isLoading.value = false
                }
        } else {
            isLoading.value = false
        }
    }

    val selectedItem = rememberSaveable { mutableStateOf(0) }

    if (isLoading.value || user.value == null) {

        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,) {
                CircularProgressIndicator()
                Text(text = "Please wait...")
            }
        }
        return
    }

    val role = user.value!!.role

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navList = if (role == "admin") navItemListAdmin else navItemList
                navList.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = index == selectedItem.value,
                        onClick = { selectedItem.value = index },
                        icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                        label = { Text(item.label) }
                    )
                }
            }
        }
    ) {
        val contentModifier = modifier.padding(it)
        if (role == "admin") {
            ContentScreenForAdmin(modifier = contentModifier, selectedItem.value)
        } else {
            ContentScreen(modifier = contentModifier, selectedItem.value)
        }
    }
}

@Composable
fun ContentScreen(modifier: Modifier = Modifier, selectedIndex: Int) {
    when(selectedIndex){
        0 -> HomePage(modifier)
        1 -> GDGioHang(modifier)
        2 -> GDDonHang(modifier)
        3 -> GDThongTinCaNhan(modifier)

    }
}
@Composable
fun ContentScreenForAdmin(modifier: Modifier = Modifier, selectedIndex: Int){
    when(selectedIndex){
        0 -> HomePage(modifier)
        1 -> GDQuanLyDonHang(modifier)
        2 -> GDQuanLyTaiKhoan(modifier)
        3-> GDThongTinCaNhan(modifier)
    }
}