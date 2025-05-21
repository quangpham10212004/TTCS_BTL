package com.example.temp.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.tbuonomo.viewpagerdotsindicator.compose.DotsIndicator
import com.tbuonomo.viewpagerdotsindicator.compose.model.DotGraphic
import com.tbuonomo.viewpagerdotsindicator.compose.type.ShiftIndicatorType

// Banner ow dau trang
@Composable
fun TPBieuNgu(modifier: Modifier = Modifier) {
    var bannerList = remember{
        mutableStateOf<List<String>>(emptyList())
    }
    LaunchedEffect(Unit) {
        Firebase.firestore.collection("data")
            .document("urls")
            .get().addOnCompleteListener(){
                bannerList.value = it.result.get("urls") as List<String>
            }
    }

    Column(
        modifier = modifier
    ) {
        val pagerState = rememberPagerState(0){ bannerList.value.size }
        HorizontalPager(
            state = pagerState,
            pageSpacing = 24.dp)
        {
            AsyncImage(
                model = bannerList.value.get(it),
                contentDescription = "Banner Image",
                modifier = Modifier.fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
            )
        }

        Spacer(modifier = Modifier.height(10.dp))
        DotsIndicator(
            dotCount = bannerList.value.size,
            type = ShiftIndicatorType(DotGraphic(
                color = MaterialTheme.colorScheme.primary,
                size = 6.dp
            )),
            pagerState = pagerState,
        )

    }

}