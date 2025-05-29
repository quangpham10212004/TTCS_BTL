package com.example.temp.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.temp.AppUtil
import com.example.temp.model.LaptopModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun TPThanhTimKiem(modifier: Modifier = Modifier, navController: NavController) {
    val searchText = remember { mutableStateOf("") } // text input
    val suggestions = remember { mutableStateOf(listOf(LaptopModel())) } // goi y

    val context = LocalContext.current

    LaunchedEffect (searchText.value){
        val query = searchText.value.trim()
        if (query.isNotEmpty()) {
            Firebase.firestore.collection("data")
                .document("stock").collection("laptops")
                .get()
                .addOnSuccessListener { result ->
                    val filtered = result.documents.mapNotNull { it.toObject(LaptopModel::class.java) }
                        .filter { it.title.contains(query, ignoreCase = true) }
                        .take(5) // Giới hạn 5 gợi ý
                    suggestions.value = filtered
                }
        } else {
            suggestions.value = emptyList()
        }
    }

    Column (modifier = modifier
        .fillMaxWidth()
        .padding(16.dp))
    {
        OutlinedTextField(
            value = searchText.value,
            onValueChange = { searchText.value = it },
            label = {Text("Search for your item")},
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Search")
            },
            trailingIcon = {
                IconButton(onClick = {
                    if(searchText.value == ""){
                        AppUtil.showToast(context = context, "Please enter your key words")
                    }
                    if(suggestions.value.isEmpty()){
                        AppUtil.showToast(context = context, "No results for your search")
                    }
                    else if(searchText.value.isNotEmpty()){
                        navController.navigate("search/${searchText.value}")
                    }
                }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Go")
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        )
        if(searchText.value.isNotEmpty()){ // khi khong tim ra ket qua nao
            if(suggestions.value.isEmpty()){
                Text(
                    text = "No results found",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
            }
            else {
                suggestions.value.forEach { laptop -> // goi y ben duoi
                    Card (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                            .clickable {
                                navController.navigate("laptop-detail/${laptop.id}")
                            }
                            .height(90.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        Row(modifier = Modifier.padding(8.dp)) {
                            AsyncImage(
                                model = laptop.images.firstOrNull(),
                                contentDescription = laptop.title,
                                modifier = Modifier
                                    .height(50.dp)
                                    .aspectRatio(1f)
                            )
                            Column (
                                modifier = Modifier
                                    .padding(start = 12.dp)
                                    .weight(1f)
                            ){
                                Text(
                                    text = laptop.title,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontSize = 14.sp,
                                )
                                Text(
                                    text = "${laptop.price} ₫",
                                    color = MaterialTheme.colorScheme.primary,
                                    fontSize = 14.sp
                                )
                            }

                        }
                    }
                    HorizontalDivider()
                }
            }
        }

    }
}