package com.example.temp.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun SearchBarView(modifier: Modifier = Modifier, navController: NavController) {
    val searchText = remember { mutableStateOf("") }
    Column (modifier = modifier
        .fillMaxWidth()
        .padding(16.dp))
    {
        OutlinedTextField(
            value = searchText.value,
            onValueChange = { searchText.value = it },
            label = {Text("Search")},
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Search")
            },
            trailingIcon = {
                IconButton(onClick = {
                    if(searchText.value.isNotEmpty()){
                        navController.navigate("search/${searchText.value}")
                    }
                }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Go")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}