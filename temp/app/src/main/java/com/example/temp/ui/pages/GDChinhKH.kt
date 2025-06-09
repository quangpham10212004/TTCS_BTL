package com.example.temp.ui.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.temp.GlobalNavigation
import com.example.temp.components.TPBieuNgu
import com.example.temp.components.TPCacBrand
import com.example.temp.components.TPSanPhamTungBrand
import com.example.temp.components.TPTieuDe
import com.example.temp.model.LaptopModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import kotlin.collections.chunked
import kotlin.collections.forEach

@Composable


fun HomePage(modifier: Modifier = Modifier) {

    val role = mutableStateOf("")
    var laptopList by remember{ mutableStateOf(listOf<LaptopModel>()) }
    LaunchedEffect(Unit){
        Firebase.firestore.collection("data").document("stock")
            .collection("laptops").get().addOnCompleteListener {
                if(it.isSuccessful){
                    val result = it.result.documents.mapNotNull { doc->
                        doc.toObject(LaptopModel::class.java)
                    }
                    if(result.isNotEmpty()){
                        laptopList = result
                    }
                }
            }
        Firebase.firestore.collection("users").document(FirebaseAuth.getInstance().currentUser?.uid!!)
            .get().addOnCompleteListener {
                if (it.isSuccessful){
                    val result = it.result.getString("role")
                    role.value = result.toString()
                }
            }
    }
    fun reloadLaptops() {
        Firebase.firestore.collection("data").document("stock")
            .collection("laptops").get().addOnCompleteListener {
                if (it.isSuccessful) {
                    val result = it.result.documents.mapNotNull { doc ->
                        doc.toObject(LaptopModel::class.java)
                    }
                    laptopList = result
                }
            }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        item{
            TPTieuDe(modifier) // header
            Spacer(modifier = Modifier.height(8.dp))
            TPBieuNgu(modifier.height(150.dp)) // banner
            if(role.value =="admin"){
                OutlinedButton(onClick = {
                    GlobalNavigation.navController.navigate("new")
                }, modifier = Modifier.fillMaxWidth()) {
                    Text("Thêm sản phẩm")
                }
            }
            Text(
                text = "Brands", // danh sach brands
                style = androidx.compose.ui.text.TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            )
            Spacer(modifier = Modifier.height(10.dp))
            TPCacBrand(modifier)
            Spacer(modifier = Modifier.height(10.dp))
        }
        items (laptopList.chunked (2)) { chunkedItems ->
            Row{
                chunkedItems.forEach { item ->
                    TPSanPhamTungBrand(modifier= Modifier.weight(1f), item = item, onDeletedItem = {reloadLaptops()
                        laptopList = laptopList.filterNot { it.id == item.id }})
                }
                if(chunkedItems.size == 1){
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}



