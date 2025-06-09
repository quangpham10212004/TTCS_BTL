package com.example.temp.ui.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.temp.GlobalNavigation
import com.example.temp.components.TPSanPhamTrongGioHang
import com.example.temp.model.OrderModel
import com.example.temp.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

@Composable
fun GDDonHangChiTiet(modifier: Modifier = Modifier, orderId: String) {
    val user = remember { mutableStateOf(UserModel()) }
    val items = remember { mutableStateOf(OrderModel()) }
    LaunchedEffect(orderId) {
        Firebase.firestore.collection("orders")
            .document(orderId).get().addOnCompleteListener {
                val result = it.result.toObject(OrderModel::class.java)
                if(result!=null){
                    items.value = result
                    val userId = items.value.userId
                    Firebase.firestore.collection("users")
                        .document(userId).get().addOnCompleteListener {
                            val userResult = it.result.toObject(UserModel::class.java)
                            if(userResult!=null){
                                user.value = userResult

                            }
                        }
                }
            }


    }


    Column(
        modifier = modifier.fillMaxSize().padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        Text(
            "Thông tin đơn hàng",
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
        )
        HorizontalDivider()
        Column (
            modifier = modifier.fillMaxWidth()
                .padding(8.dp),
        ){
            Text("Vận chuyển đến: ", style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            ))
            if(user.value.address!=null){
                Text("${user.value.address}")
            }
            else{
                Text("Hãy cập nhật địa chỉ của bạn",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Red
                    ))
            }
            Text("Số điện thoại liên hệ: ",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                ))
            if(user.value.phone!=null){
                Text("${user.value.phone}")
            }
            else{
                Text("Hãy cập nhật số điện thoại của bạn của bạn",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Red
                    ))
            }
            Text("Tên người nhận hàng: ",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                ))
            Text("${user.value.name}")
            HorizontalDivider()
            Text("Danh sách sản phẩm:",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                ))
        }
        LazyColumn(
            modifier = Modifier.weight(1f),
        ) {
            items(items.value.items.toList(), key = {it.first}) { (laptopId, quantity) ->
                TPSanPhamTrongGioHang(laptopId = laptopId, quantity = quantity, inCart = false);
            }
        }
    }
}