package com.example.temp.components

import android.view.Display
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.temp.model.CategoryModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import java.util.Locale

@Composable
fun CategoriesView(modifier: Modifier = Modifier) {
    val categoryList = remember {
        mutableStateOf<List<CategoryModel>>(emptyList())
    }
    LaunchedEffect(key1 = Unit) {
        Firebase.firestore.collection("data").document("stock")
            .collection("categories")
            .get().addOnCompleteListener {
                if(it.isSuccessful){
                    val result = it.result.documents.mapNotNull {
                        document -> document.toObject(CategoryModel::class.java)
                    }
                    categoryList.value = result
                }
            }
    }
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy (20.dp) ,
    ){
        items (categoryList.value){ item  ->
            CategoryItem(category = item)
        }
    }
}

@Composable
fun CategoryItem(category : CategoryModel){
    Card(
        modifier = Modifier.size(90.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            AsyncImage(
                model = category.imageUrl,
                contentDescription = category.name,
                modifier = Modifier.size(50.dp)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = category.name,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                ),
                textAlign = TextAlign.Center,
            )

        }
    }
}