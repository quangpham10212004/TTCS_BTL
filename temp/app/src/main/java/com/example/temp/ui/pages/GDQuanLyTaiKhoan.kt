package com.example.temp.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.temp.components.TPSanPhamTrongGioHang
import com.example.temp.components.TPShowNguoiDung
import com.example.temp.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.firestore

@Composable
fun GDQuanLyTaiKhoan(modifier: Modifier = Modifier) {
    val userList = remember { mutableStateOf(listOf(UserModel())) }
    val db= Firebase.firestore

    val isLoading = remember { mutableStateOf(true) }


    LaunchedEffect (Unit){
        db.collection("users").get().addOnCompleteListener {
            if(it.isSuccessful){
                val result = it.result.documents.mapNotNull {
                    val temp = it.toObject(UserModel::class.java)!!
                temp?.apply { id = it.id }}
                if(result.size>0){
                    userList.value= result
                }
            }
            isLoading.value = false

        }
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        Text(
            "Danh sách người dùng hệ thống",
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
        )
        HorizontalDivider()
        if (isLoading.value) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator() // icon loading xoay, cai nay hay quen vl

                Text("Đang tải dữ liệu...", modifier = Modifier.padding(top = 8.dp))
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(userList.value) { user ->
                    TPShowNguoiDung(userId = user.id.toString())
                }
            }
        }
    }
}