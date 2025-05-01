package com.example.temp.ui.pages

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.temp.components.ProductLaptopsView
import com.example.temp.model.LaptopModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun CategoryProductsPage(modifier: Modifier = Modifier,categoryId: String ) {
    val laptopList = remember {
        mutableStateOf<List<LaptopModel>>(emptyList())
    }
    LaunchedEffect (key1 = Unit) {
        Firebase.firestore.collection("data")
            .document("stock").collection("laptops")
            .whereEqualTo("category", categoryId.uppercase()) // cmno lo tao categoryId la lowercase con category trong laptop la uppercase
            .get().addOnCompleteListener {
                if (it.isSuccessful) {
                    val resultList = it.result.documents.mapNotNull { document ->
                        document.toObject(LaptopModel::class.java)
                    }
                    laptopList.value = resultList
                }
            }
    }
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)) {
        items (laptopList.value.chunked (2)) { chunkedItems ->
            Row{
                chunkedItems.forEach { item ->
                    ProductLaptopsView(modifier= Modifier.weight(1f), item = item)
                }
                if(chunkedItems.size == 1){
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }

}
