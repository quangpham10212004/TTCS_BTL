package com.example.temp.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.temp.AppUtil
import com.example.temp.GlobalNavigation
import com.example.temp.model.OrderModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Date

@Composable
fun TPDonHangCard(order: OrderModel, onClick: () -> Unit) {
    val role = remember { mutableStateOf("") }
    val context = LocalContext.current
    LaunchedEffect (Unit) {
        Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser!!.uid).get().addOnCompleteListener {
                if (it.isSuccessful) {
                    val result = it.result.get("role")?:""
                    if (result!=null) {
                        role.value = result.toString()
                    }
                }
            }
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        val sdf = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm")
        Row( verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "Đơn hàng lúc: ${sdf.format(Date(order.timestamp))}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = "Tổng tiền: ${order.total_price} VND",
                    fontSize = 14.sp
                )
                Text(
                    text = "Số mặt hàng: ${order.items.size}",
                    fontSize = 14.sp
                )
            }
            if(role.value == "admin"){
                IconButton(onClick = {
                    AppUtil.DeleteOrder(order.id, context)
                }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete button",
                        )
                }
            }
        }
    }
}