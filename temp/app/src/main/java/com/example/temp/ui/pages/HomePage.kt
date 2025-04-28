package com.example.temp.ui.pages

import android.preference.PreferenceActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.temp.components.BannerView
import com.example.temp.components.HeaderView

@Composable
fun HomePage(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        HeaderView(modifier)
        Spacer(modifier = Modifier.height(3.dp))
        BannerView(modifier)
    }
}