package com.example.temp.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.temp.AppUtil
import com.example.temp.GlobalNavigation
import com.example.temp.model.LaptopModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
// danh sach cac item trong cart duoc invoke CartPage
@Composable
fun CartItemView(modifier: Modifier = Modifier, laptopId: String, quantity : Long) {
    var laptop =  remember {
        mutableStateOf(LaptopModel())
    }
    // lay laptop hien co
    LaunchedEffect(Unit) {
        Firebase.firestore.collection("data").document("stock")
            .collection("laptops").document(laptopId)
            .get().addOnCompleteListener {
                if(it.isSuccessful){
                   val result = it.result?.toObject(LaptopModel::class.java)
                    if (result != null) {
                        laptop.value = result
                    }
                }
            }
    }

    val context= LocalContext.current
    Card (
        modifier = modifier.padding(8.dp)
            .clickable{
                GlobalNavigation.navController.navigate("laptop-detail/"+laptop.value.id) // navigate sang trang hien thi lap tuong ung

            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(8.dp)
    ){
        Row(
            modifier = Modifier
                .padding(8.dp)

        ) {
            AsyncImage(
                model = laptop.value.images.firstOrNull(),
                contentDescription = laptop.value.title,
                modifier = Modifier
                    .height(100.dp)
                    .width(100.dp)
            )

            Column(
                modifier = Modifier.padding(8.dp).weight(1f)
            ){
                Text(
                    laptop.value.title,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = "VND " + laptop.value.price,
                    fontSize = 12.sp,
                )
                // 2 nut + - so luong sp
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                ){
                    IconButton(onClick = {
                        AppUtil.DecreaseNumItem(laptopId, context)
                    }){
                        Text(
                            "-",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    Text(
                        "$quantity",
                        fontSize = 18.sp,
                        )
                    IconButton(onClick = {
                        AppUtil.IncreaseNumItem(laptopId, context)
                    }){
                        Text(
                            "+",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }


            IconButton(onClick = {
                AppUtil.DecreaseNumItem(laptopId, context,wantToDelete = true)
            }){
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete button",

                )
            }


        }
    }
}