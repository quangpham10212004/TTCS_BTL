package com.example.temp.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.temp.AppUtil
import com.example.temp.GlobalNavigation
import com.example.temp.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
@Composable
fun TPShowNguoiDung(modifier: Modifier = Modifier, userId: String) {
    val userModel = remember { mutableStateOf(UserModel()) }
    val role = remember {mutableStateOf("")}
    LaunchedEffect(userId) {
        Firebase.firestore.collection("users").document(userId).get().addOnCompleteListener {
            if (it.isSuccessful) {
                val result = it.result.toObject(UserModel::class.java)
                if (result != null) {
                    userModel.value = result
                }
            }
        }
    }


    val context = LocalContext.current
    val currentId = FirebaseAuth.getInstance().currentUser!!.uid
    val backgroundColor = if (userModel.value.role== "admin") {
        MaterialTheme.colorScheme.error
    } else {
        MaterialTheme.colorScheme.primaryContainer // xanh
    }
    Card(
        modifier = Modifier
            .padding(vertical = 4.dp) // khoảng cách giữa các card nhỏ gọn
            .widthIn(max = 400.dp) // tránh quá rộng
            .fillMaxWidth(0.95f), // chiếm 95% chiều rộng, đẹp hơn
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(4.dp) // nhẹ nhàng hơn
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Text(
                    text = userModel.value.name,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 16.sp
                )
                Text(
                    text = userModel.value.email,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            if(currentId != userId){
                IconButton(onClick = {
                    AppUtil.SetAdmin(userId, context ){ newRole->
                        userModel.value= userModel.value.copy(role = newRole)
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Set Admin",
                    )
                }
            }
            else{
                Text(
                    "You"
                    )
            }
        }
    }

}