package com.example.temp.ui.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.temp.GlobalNavigation
import com.example.temp.components.TPDonHangCard
import com.example.temp.components.TPSanPhamTrongGioHang
import com.example.temp.model.OrderModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

@Composable
fun GDQuanLyDonHang(modifier: Modifier = Modifier) {
    var itemList by remember { mutableStateOf(listOf(OrderModel())) }
    val cu_id = FirebaseAuth.getInstance().currentUser!!.uid
    DisposableEffect (Unit) {
        val lis = Firebase.firestore.collection("orders")
            .addSnapshotListener { it, exception->
                val result = it?.documents?.mapNotNull { doc ->
                    val order = doc.toObject(OrderModel::class.java)
                    order?.apply { id = doc.id }
                }
                if (result != null) {
                    itemList = result
                }
            }
        onDispose { lis.remove() }
    }
    Column(
        modifier = modifier.fillMaxSize().padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        Text(
            "Đơn hàng hiện tại",
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
        )
        HorizontalDivider()
        LazyColumn(
            modifier = Modifier.weight(1f),
        ) {
            items(itemList){ order->
                TPDonHangCard(order = order) {
                    GlobalNavigation.navController.navigate("orders/${order.id}")
                }
            }
        }


    }
}