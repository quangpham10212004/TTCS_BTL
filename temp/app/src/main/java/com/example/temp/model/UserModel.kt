package com.example.temp.model

data class UserModel(
    var id : String? = null,
    var name:String =  "",
    var email:String = "",
    var userID:String = "",
    var myCart: Map<String, Long> = mapOf(), // ko gan thanh empty map no bat dang nhap phai nhap ca map :))
    var address : String = "",
    var phone: String = "",
    var role : String = "client"
)
