package com.example.temp.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

@Composable
fun TPCacSPDuocTimKiem(modifier: Modifier = Modifier, item : LaptopModel ) {
    val context= LocalContext.current
    Card (
        modifier = modifier.padding(8.dp)
            .clickable{
                GlobalNavigation.navController.navigate("laptop-detail/"+item.id) // navigate sang trang hien thi lap tuong ung

            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(8.dp)
    ){
        Column(
            modifier = Modifier
                .padding(16.dp),
        ) {
            AsyncImage(
                model = item.images.firstOrNull(),
                contentDescription = item.title,
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth()
            )
            Text(
                item.title,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally

                ){
                    Text(
                        text = item.status,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                    )
                    Text(
                        text = "VND " + item.price,
                        fontSize = 12.sp,
                    )
                    Button(
                        onClick = {
                            AppUtil.AddToCart(item.id, context ) // add to cart
                        },
                        modifier = Modifier.padding(8.dp)
                            .height(50.dp))
                    {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Add to cart button",
                        )
                    }
                }

            }
        }
    }
}