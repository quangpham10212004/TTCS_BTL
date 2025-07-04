package com.example.temp.ui.pages


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.temp.AppUtil
import com.example.temp.GlobalNavigation
import com.example.temp.components.TPHienBinhLuan
import com.example.temp.components.TPThemBinhLuan
import com.example.temp.model.LaptopModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.tbuonomo.viewpagerdotsindicator.compose.DotsIndicator
import com.tbuonomo.viewpagerdotsindicator.compose.model.DotGraphic
import com.tbuonomo.viewpagerdotsindicator.compose.type.ShiftIndicatorType
// page hien thong tin laptop
@Composable
fun GDThongTinLaptop(modifier: Modifier = Modifier, laptopId: String) {
    val uid = FirebaseAuth.getInstance().currentUser?.uid
    val role = remember { mutableStateOf("") }
    var laptop = remember {
        mutableStateOf(LaptopModel())
    }
    val context = LocalContext.current
    // lay thong tin laptop hien tai
    LaunchedEffect(Unit) {
        Firebase.firestore.collection("data").document("stock")
            .collection("laptops")
            .document(laptopId).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    var result = task.result.toObject(LaptopModel::class.java)
                    if(result != null) {
                        laptop.value = result
                    }
                }
            }

        Firebase.firestore.collection("users").document(uid!!).get().addOnCompleteListener {
            if (it.isSuccessful) {
                var userResult = it.result.get("role")
                if (userResult != null) {
                    role.value = userResult.toString()
                }
            }
        }
    }
    Scaffold(
        modifier = modifier.padding(8.dp),
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                horizontalArrangement = Arrangement.SpaceBetween,

            ) {
                Text(
                    text = "VND " + laptop.value.price,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(8.dp)
                )
                if(role.value != "admin"){
                    Button(
                        onClick = {
                            GlobalNavigation.navController.navigate("cart") // tro den gio hang
                        },
                        modifier = Modifier
                            .padding(8.dp)
                            .height(50.dp)
                            .width(100.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Add to cart"
                        )
                    }
                }
            }

        }
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
        ) {
            Text(
                text = laptop.value.title,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                modifier = modifier
            ) {
                val pagerState = rememberPagerState(0) { laptop.value.images.size } // anh cho laptop
                HorizontalPager(
                    state = pagerState,
                    pageSpacing = 24.dp
                )
                {
                    AsyncImage(
                        model = laptop.value.images.get(it),
                        contentDescription = "Laptop Image",
                        modifier = Modifier
                            .height(250.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
                DotsIndicator(
                    dotCount = laptop.value.images.size,
                    type = ShiftIndicatorType(
                        DotGraphic(
                            color = MaterialTheme.colorScheme.primary,
                            size = 6.dp
                        )
                    ),
                    pagerState = pagerState,
                )

            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = laptop.value.description,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Thông tin chi tiết: ",
                modifier = Modifier.padding(8.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )
            if (laptop.value.sysDetails.isNotEmpty()) {
                laptop.value.sysDetails.forEach { (key, value) ->
                    Text(
                        text = "$key: $value",
                        modifier = Modifier.padding(8.dp),
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            if(role.value != "admin"){
                Button(
                    onClick = {
                        AppUtil.AddToCart(laptopId, context)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .height(50.dp)
                ) {
                    Text("Thêm vào giỏ hàng")
                }
            }
            else{
                Button(
                    onClick = {
                        GlobalNavigation.navController.navigate("change-infor-sp/${laptopId}")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .height(50.dp)
                ) {
                    Text("Sửa thông tin mặt hàng")
                }
            }
            HorizontalDivider(thickness = 1.dp)
            TPThemBinhLuan(modifier,laptopId)
            Text(
                text = "Bình luận & đánh giá: ",
                modifier = Modifier.padding(8.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )

            TPHienBinhLuan(modifier,laptopId)
        }
    }
}