package com.example.temp.ui.pages

import android.widget.Space
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.temp.model.LaptopModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.tbuonomo.viewpagerdotsindicator.compose.DotsIndicator
import com.tbuonomo.viewpagerdotsindicator.compose.model.DotGraphic
import com.tbuonomo.viewpagerdotsindicator.compose.type.ShiftIndicatorType
import java.time.format.TextStyle

@Composable
fun LaptopDetailPage(modifier: Modifier = Modifier, laptopId: String) {
    var laptop = remember {
        mutableStateOf(LaptopModel())
    }

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
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
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
            val pagerState = rememberPagerState(0){ laptop.value.images.size }
            HorizontalPager(
                state = pagerState,
                pageSpacing = 24.dp)
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
                type = ShiftIndicatorType(DotGraphic(
                    color = MaterialTheme.colorScheme.primary,
                    size = 6.dp
                )),
                pagerState = pagerState,
            )

        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = laptop.value.description,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ){
            Text(
                text = "VND " + laptop.value.price,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(8.dp)
            )

        }
        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth()
                .padding(8.dp).height(50.dp))
        {
            Text(
                text= "Add to my cart"
            )
        }
        if(laptop.value.sysDetails.isNotEmpty()) {
            laptop.value.sysDetails.forEach { (key, value) ->
                Text(
                    text = "$key: $value",
                    modifier = Modifier.padding(8.dp),
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}