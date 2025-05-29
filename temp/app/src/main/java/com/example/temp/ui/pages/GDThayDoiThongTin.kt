package com.example.temp.ui.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.temp.AppUtil
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

@Composable
fun GDThayDoiThongTin(modifier: Modifier = Modifier, navController: NavController) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val db = Firebase.firestore

    val name = remember { mutableStateOf("") }
    val address = remember { mutableStateOf("") }
    val phoneNumber = remember { mutableStateOf("") }

    val currentPassword = remember { mutableStateOf("") }
    val newPassword = remember { mutableStateOf("") }

    val currentUserData = remember { mutableStateOf<Map<String, Any>?>(null) }

    LaunchedEffect(Unit) {
        val uid = auth.currentUser?.uid
        if (uid != null) {
            db.collection("users").document(uid).get()
                .addOnSuccessListener { snapshot ->
                    if (snapshot.exists()) {
                        currentUserData.value = snapshot.data
                    }
                }
                .addOnFailureListener {
                    AppUtil.showToast(context, "Không thể lấy dữ liệu người dùng: ${it.message}")
                }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Text("Thay đổi thông tin cá nhân", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(12.dp))
        HorizontalDivider(thickness = 1.dp)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name.value,
            onValueChange = { name.value = it },
            label = { Text("Tên mới") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = address.value,
            onValueChange = { address.value = it },
            label = { Text("Địa chỉ mới") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = phoneNumber.value,
            onValueChange = { phoneNumber.value = it },
            label = { Text("Số điện thoại mới") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = currentPassword.value,
            onValueChange = { currentPassword.value = it },
            label = { Text("Mật khẩu hiện tại") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = newPassword.value,
            onValueChange = { newPassword.value = it },
            label = { Text("Mật khẩu mới") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = {
                val uid = auth.currentUser?.uid
                if (uid != null) {
                    val updates = mutableMapOf<String, Any>()
                    val oldData = currentUserData.value ?: emptyMap()

                    if (name.value.isNotBlank()) updates["name"] = name.value
                    else oldData["name"]?.let { updates["name"] = it }

                    if (address.value.isNotBlank()) updates["address"] = address.value
                    else oldData["address"]?.let { updates["address"] = it }

                    if (phoneNumber.value.isNotBlank()) updates["phone"] = phoneNumber.value
                    else oldData["phone"]?.let { updates["phone"] = it }

                    db.collection("users").document(uid).update(updates)
                        .addOnSuccessListener {
                            AppUtil.showToast(context, "Cập nhật thông tin thành công")
                        }
                        .addOnFailureListener {
                            AppUtil.showToast(context, "Lỗi cập nhật thông tin: ${it.message}")
                        }

                    val currentUser = auth.currentUser!!
                    val email = currentUser.email ?: ""
                    if (currentPassword.value.isNotBlank() && newPassword.value.isNotBlank()) {
                        val credential = EmailAuthProvider.getCredential(email, currentPassword.value)
                        currentUser.reauthenticate(credential)
                            .addOnSuccessListener {
                                currentUser.updatePassword(newPassword.value)
                                    .addOnSuccessListener {
                                        AppUtil.showToast(context, "Cập nhật mật khẩu thành công")
                                        navController.popBackStack()
                                    }
                                    .addOnFailureListener {
                                        AppUtil.showToast(context, "Lỗi khi đổi mật khẩu: ${it.message}")
                                    }
                            }
                            .addOnFailureListener {
                                AppUtil.showToast(context, "Sai mật khẩu hiện tại")
                            }
                    } else if (newPassword.value.isNotBlank() && currentPassword.value.isBlank()) {
                        AppUtil.showToast(context, "Vui lòng nhập mật khẩu hiện tại")
                    } else if (currentPassword.value.isNotBlank() && newPassword.value.isBlank()) {
                        AppUtil.showToast(context, "Vui lòng nhập mật khẩu mới")
                    }else{
                        navController.popBackStack()
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Cập nhật")
        }
    }
}
