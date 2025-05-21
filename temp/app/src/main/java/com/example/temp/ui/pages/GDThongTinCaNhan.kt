package com.example.temp.ui.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.temp.GlobalNavigation.navController
import com.example.temp.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

@Composable
fun GDThongTinCaNhan(modifier: Modifier = Modifier) {
    val curUser = remember { mutableStateOf(UserModel()) }

    Firebase.firestore.collection("users") // lay user hien tai
        .document(FirebaseAuth.getInstance().currentUser?.uid!!)
        .get().addOnCompleteListener {
            val result = it.result.toObject(UserModel::class.java)
            if(result != null) {
                curUser.value = result
            }
        }
    val address = curUser.value.address
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        Text(
            "Your personal information",
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
        )
        HorizontalDivider()
        Column(modifier=Modifier
            .weight(1f)
            .fillMaxWidth(),
            horizontalAlignment = Alignment.Start)
        {
            Text(
                text = "Your email:",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${curUser.value?.email}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Thin,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Your name:",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${curUser.value?.name}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Thin,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Your address:",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${curUser.value?.address}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Thin,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Your phone number:",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${curUser.value?.phone}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Thin,
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        Button (
            onClick = {
                navController.navigate("change-infor")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
        ){
            Text("Change information")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { // button logout
            FirebaseAuth.getInstance().signOut()
            navController.navigate("auth")
        },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp))
        {
            Text("Logout")
        }
    }
}