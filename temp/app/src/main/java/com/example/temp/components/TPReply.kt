package com.example.temp.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusTargetModifierNode
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.temp.model.CommentModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date
@Composable
fun TPReply(modifier: Modifier = Modifier,
            laptopId: String,
            commentID: String,
            userName: String) {
    var replies by remember { mutableStateOf<List<CommentModel>>(emptyList()) } // ds reply
    var showReplies by remember { mutableStateOf(false) } // hien reply
    var showAllReplies by remember { mutableStateOf(false) } // hien all reply
    var replyContent by remember { mutableStateOf("") } // content

    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    LaunchedEffect (Unit){
        Firebase.firestore.collection("data").document("stock")
            .collection("laptops").document(laptopId)
            .collection("comments")
            .document(commentID).collection("replies")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, exception ->
                if(exception != null || snapshot?.isEmpty == true) {
                    return@addSnapshotListener
                }
                replies = snapshot?.documents!!.mapNotNull { doc->
                    doc.toObject(CommentModel::class.java)?.copy(reply_Id = commentID)
                }
            }

    }

    // xem & thu gon phan hoi
    if (replies.isNotEmpty()) {
        TextButton(onClick = {
            showReplies = !showReplies
            if (!showReplies) showAllReplies = false
        }) {
            Text(if (showReplies) "Thu gọn phản hồi" else "Xem phản hồi (${replies.size})")
        }
    }

    if(showReplies){
        Column (modifier = modifier.padding(start = 16.dp)) {
            val repliesToShow = if(showAllReplies) replies else replies.take(4)

            repliesToShow.forEach {  reply ->
                Column (modifier = Modifier.padding(vertical = 4.dp)) {
                    Row{
                        Icon(
                            Icons.Default.PlayArrow,
                            contentDescription = null,

                        )
                        Text(
                            "${reply.userName} replied:",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            sdf.format(Date(reply.timestamp)),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Light,
                        )
                    }
                    Text(reply.content, fontSize = 14.sp)
                }
            }
        }
        if (!showAllReplies && replies.size > 4) {
            TextButton(onClick = { showAllReplies = true }) {
                Text("Xem thêm phản hồi ...")
            }
        }
    }
    // them phan hoi
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ){
        TextField(
            value = replyContent,
            onValueChange = {replyContent = it},
            modifier = Modifier.weight(1f),
            placeholder = {Text("Viết phản hồi...")},
            singleLine = true,
            shape = RoundedCornerShape(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button (onClick = {
            if(replyContent.isNotEmpty()){
                val newReply = hashMapOf(
                    "userName" to userName,
                    "content" to replyContent.trim(),
                    "timestamp" to System.currentTimeMillis(),
                )
                Firebase.firestore.collection("data").document("stock")
                    .collection("laptops").document(laptopId)
                    .collection("comments").document(commentID)
                    .collection("replies")
                    .add(newReply)
                replyContent = ""
            }
        }){
            Text("Gửi")
        }
    }



}