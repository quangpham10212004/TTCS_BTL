package com.example.temp.ui.pages

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.temp.GlobalNavigation
import com.example.temp.components.TPSanPhamTrongGioHang
import com.example.temp.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
// trang cho gio hang
@Composable
fun GDGioHang(modifier: Modifier = Modifier) {
    val cur_user = remember {
        mutableStateOf(UserModel())
    }

    DisposableEffect (Unit){ // dung disposable vi no co onDispose de kill listener cua firebase moi khi minh recompose, dung LaunchedEffect bth se bi loi
        var lis = Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser!!.uid)
            .addSnapshotListener { it, exception -> // goi moi khi doc co su thay doi (real-time) ko nhu get().addonlistencomplete,
                if (it!= null){
                    var result = it.toObject(UserModel::class.java)
                    if (result != null) {
                        cur_user.value = result
                    }
                }
            }
        onDispose { lis.remove() } // loai bo firebase listener
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        Text(
            "Cart",
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
        )
        HorizontalDivider()
        LazyColumn(
            modifier = Modifier.weight(1f),
        ) {
            items(cur_user.value.myCart.toList(), key = {it.first}) { (laptopId, quantity) ->
                TPSanPhamTrongGioHang(laptopId = laptopId, quantity = quantity);
            }
        }

        if(cur_user.value.myCart.size > 0){
            Button(
                onClick = {
                    GlobalNavigation.navController.navigate("checkout")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
            ) {
                Text("Check out")
            }
        }
        else{
            Column (modifier = Modifier.fillMaxSize().padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {
                Text("Giỏ hàng trống")
            }
        }

    }
}