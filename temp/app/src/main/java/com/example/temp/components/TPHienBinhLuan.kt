package com.example.temp.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.temp.model.CommentModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TPHienBinhLuan(
    modifier: Modifier = Modifier,
    laptopId: String,
    ) {
    var comments by remember { mutableStateOf(listOf<CommentModel>()) }
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())


    LaunchedEffect (Unit){
        Firebase.firestore.collection("data").document("stock")
            .collection("laptops").document(laptopId)
            .collection("comments").orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, exception ->
                if(exception != null || snapshot == null) {
                    return@addSnapshotListener
                }
                val result = snapshot.documents.mapNotNull { comment ->  comment.toObject(CommentModel::class.java) }
                comments = result
            }
    }
    Column(
        modifier = modifier.fillMaxWidth().padding(8.dp),

    ){
        comments.forEach { comment ->
            Row {
                Text(comment.userName, modifier = Modifier.weight(1F),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,)
                Text(sdf.format(Date(comment.timestamp)),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Light)
            }
            Spacer(modifier = Modifier.height(3.dp))
            Text(comment.content, fontSize = 16.sp)
            HorizontalDivider()
        }
    }

}