package com.example.temp.ui.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import com.example.temp.components.SearchItemsView
import com.example.temp.model.LaptopModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlin.collections.forEach

@Composable
fun SearchItemsPage(modifier: Modifier = Modifier, searchText: String) {
    val laptopList = remember { // list san pham duoc tim
        mutableStateOf(listOf(LaptopModel()))
    }
    val filteredList = remember { // list san pham duoc tim
        mutableStateOf(listOf(LaptopModel()))
    }

    LaunchedEffect (Unit){
        Firebase.firestore.collection("data")
            .document("stock").collection("laptops")
            .get().addOnCompleteListener {
                val result = it.result.documents.mapNotNull { document ->
                    document.toObject(LaptopModel::class.java)
                }
                if(result.isNotEmpty()) {
                    laptopList.value = result
                    filteredList.value = result.filter{ item->
                        item.title?.contains(searchText, ignoreCase =  true) == true
                    }
                }
            }
    }
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)) {
        items (filteredList.value.chunked (2)) { chunkedItems ->
            Row{
                chunkedItems.forEach { item ->
                    SearchItemsView(modifier= Modifier.weight(1f), item = item)
                }
                if(chunkedItems.size == 1){
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }


}