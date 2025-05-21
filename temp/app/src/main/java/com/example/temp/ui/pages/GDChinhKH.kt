package com.example.temp.ui.pages

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.temp.components.TPBieuNgu
import com.example.temp.components.TPCacBrand
import com.example.temp.components.TPTieuDe


@Composable
fun HomePage(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        TPTieuDe(modifier) // header
        Spacer(modifier = Modifier.height(10.dp))
        TPBieuNgu(modifier.height(150.dp)) // banner
        Text(
            text = "Brands", // danh sach brands
            style = androidx.compose.ui.text.TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        )
        Spacer(modifier = Modifier.height(10.dp))
        TPCacBrand(modifier)
    }
}