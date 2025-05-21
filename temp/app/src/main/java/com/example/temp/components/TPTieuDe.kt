package com.example.temp.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.temp.GlobalNavigation
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

// header duoc invoke o HomeSreen
@Composable
fun TPTieuDe(modifier: Modifier = Modifier) {
    var name = remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser!!.uid)
            .get().addOnCompleteListener {
                if(it.isSuccessful){
                    name.value = it.result.get("name").toString()
                }
            }
    }
    Column {
        Text(
            text = "Hello",
        )
        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ){
            Text(
                text = name.value,
                style = androidx.compose.ui.text.TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )

            )
            IconButton(onClick = {
                GlobalNavigation.navController.navigate("search")
            }) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
            }
        }
    }
}