package com.example.temp.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.navigation.NavHostController
import com.example.temp.AppUtil
import com.example.temp.model.LaptopModel
import com.example.temp.model.OrderModel
import com.example.temp.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import java.text.NumberFormat
import java.util.Locale
// checkout khi co y dinh thanh toan
@Composable
fun GDThanhToan(modifier: Modifier = Modifier, navController: NavHostController) {
    var userModel = remember{
        mutableStateOf(UserModel())
    }
    val laptopList = remember { mutableStateListOf(LaptopModel()) }
    val PreTotal = remember { mutableStateOf(0f) } // gia goc
    val discount = remember { mutableStateOf(0f) } // giam gia
    val FinalTotal = remember { mutableStateOf(0f) } // final gia
    fun calculateTotal() {
        laptopList.forEach { laptop ->
            if(laptop.price.isNotEmpty()){
                val quantity = userModel.value.myCart[laptop.id] ?: 0
                PreTotal.value += laptop.price.toFloat() * quantity

            }
        }
        discount.value = AppUtil.getDiscount()/100
        discount.value *= PreTotal.value
        FinalTotal.value = PreTotal.value -  discount.value
    }
    LaunchedEffect(Unit){
        Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser!!.uid)
            .get().addOnCompleteListener {
                if(it.isSuccessful){
                    val result = it.result.toObject(UserModel::class.java)
                    if(result != null){
                        userModel.value = result

                        Firebase.firestore.collection("data")
                            .document("stock").collection("laptops")
                            .whereIn("id", userModel.value.myCart.keys.toList())
                            .get().addOnCompleteListener { task ->
                                if(task.isSuccessful){
                                    val resultLaptops = task.result.toObjects(LaptopModel::class.java)
                                    if(resultLaptops != null){
                                        laptopList.addAll(resultLaptops)
                                        calculateTotal()
                                    }

                                }
                            }
                    }
                }
            }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,)

    {
        Spacer(modifier = Modifier.height(64.dp))
        Text(
            text = "Checkout",
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        )
        HorizontalDivider()
        InThongTin("Giá gốc", NumberFormat.getCurrencyInstance(Locale.US).format(PreTotal.value/26000))
        InThongTin("Khuyến mãi", NumberFormat.getCurrencyInstance(Locale.US).format(discount.value/26000))
        InThongTin("Tổng giá" , NumberFormat.getCurrencyInstance(Locale.US).format(FinalTotal.value/26000) )
        HorizontalDivider()
        Column (Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally){
            Text(
                text = "Số tiền bạn cần phải trả: ",
            )
            Text(
                "VND "+ NumberFormat.getCurrencyInstance(Locale("vi","VN")).format(FinalTotal.value),
                fontWeight = FontWeight.Bold,
            )

        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    navController.popBackStack()
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Quay lại")
            }

            Button(
                onClick = {
                    val userId = FirebaseAuth.getInstance().currentUser!!.uid
                    val newOrder = OrderModel(
                        userId = userId,
                        items = userModel.value.myCart,
                        total_price = FinalTotal.value.toLong()
                    )

                    val db = Firebase.firestore
                        db.collection("orders").add(newOrder)
                            .addOnCompleteListener {
                                db.collection("users")
                                    .document(userId).update("myCart", mapOf<String, Long>())
                                    .addOnCompleteListener {
                                        navController.popBackStack()
                                    }
                            }

                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Xác nhận đơn")
            }
        }

    }

}
@Composable
fun InThongTin(title: String, value: String) {
    Row(modifier = Modifier
        .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text (text= title, style = TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        ))
        Text(text = value, style = TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        ))
    }
    Spacer(modifier = Modifier.height(16.dp))
}