package com.example.temp.model

data class OrderModel(
    var id: String = "",
    val userId: String = "",
    val items: Map<String, Long> = mapOf(),
    val total_price: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
)