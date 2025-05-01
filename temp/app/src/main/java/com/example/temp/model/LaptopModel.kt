package com.example.temp.model

data class LaptopModel(
    val id : String = "",
    val title : String = "",
    val description : String = "",
    val category : String = "",
    val price : String = "",
    val status : String = "",
    val images : List<String> = emptyList(),
)
