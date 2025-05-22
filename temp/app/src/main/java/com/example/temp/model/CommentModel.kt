package com.example.temp.model

data class CommentModel(
    val userName : String = "",
    val content : String = "",
    val timestamp : Long = 0,
    val reply_Id : String ? = null,
)
