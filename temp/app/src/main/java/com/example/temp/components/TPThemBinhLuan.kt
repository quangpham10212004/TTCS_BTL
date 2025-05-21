package com.example.temp.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.temp.AppUtil
import com.example.temp.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TPThemBinhLuan(
    modifier: Modifier = Modifier,
    laptopId: String,
    onCommentSent: (() -> Unit)? = null
) {
    var showCommentField by remember { mutableStateOf(false) }
    var commentText by remember { mutableStateOf("") }
    val context = LocalContext.current
    var userName by remember { mutableStateOf("") }
    LaunchedEffect (Unit){ // lay ten userName hiện tại
        Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser!!.uid)
            .get().addOnCompleteListener {
                if(it.isSuccessful){
                    val result = it.result.toObject(UserModel::class.java)
                    userName = result?.name ?: "Ẩn danh"
                }
            }
    }

    Column(modifier = modifier) {
        Button(
            onClick = { showCommentField = !showCommentField },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .height(50.dp)
        ) {
            Text("Comment")
        }

        if (showCommentField) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                OutlinedTextField(
                    value = commentText,
                    onValueChange = { commentText = it },
                    label = { Text("Nhập bình luận của bạn") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        if (commentText.isBlank()) {
                            AppUtil.showToast(context, "Vui lòng nhập bình luận.")
                            return@Button
                        }

                        val comment = hashMapOf(
                            "userName" to userName,
                            "content" to commentText.trim(),
                            "timestamp" to System.currentTimeMillis()
                        )

                        Firebase.firestore.collection("data").document("stock")
                            .collection("laptops").document(laptopId)
                            .collection("comments")
                            .add(comment)
                            .addOnSuccessListener {
                                commentText = ""
                                showCommentField = false
                                AppUtil.showToast(context, "Gửi bình luận thành công!")
                                onCommentSent?.invoke() // gọi callback
                            }
                            .addOnFailureListener {
                                AppUtil.showToast(context, "Gửi thất bại: ${it.message}")
                            }
                    }
                ) {
                    Text("Gửi")
                }
            }
        }
    }
}
