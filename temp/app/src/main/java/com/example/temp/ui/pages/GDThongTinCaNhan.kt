package com.example.temp.ui.pages

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.temp.GlobalNavigation.navController
import com.example.temp.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun GDThongTinCaNhan(modifier: Modifier = Modifier) {
    val curUser = remember { mutableStateOf(UserModel()) }

    LaunchedEffect(Unit) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            Firebase.firestore.collection("users").document(uid).get().addOnSuccessListener {
                it.toObject(UserModel::class.java)?.let { user ->
                    curUser.value = user
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Thông tin cá nhân",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(thickness = 1.dp)

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            InfoField(label = "Email:", value = curUser.value.email)
            InfoField(label = "Họ và tên:", value = curUser.value.name)
            InfoField(label = "Địa chỉ:", value = curUser.value.address)
            InfoField(label = "Số điện thoại:", value = curUser.value.phone)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                navController.navigate("change-infor")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Chỉnh sửa thông tin")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                FirebaseAuth.getInstance().signOut()
                navController.navigate("auth")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text("Đăng xuất")
        }
    }
}

@Composable
fun InfoField(label: String, value: String?) {
    Column {
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = value ?: "(Chưa có thông tin)",
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal
        )
    }
}
