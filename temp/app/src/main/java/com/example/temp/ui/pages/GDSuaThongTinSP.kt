package com.example.temp.ui.pages

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.temp.AppUtil
import com.example.temp.GlobalNavigation
import com.example.temp.model.LaptopModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun GDSuaThongTinSP(
    modifier: Modifier = Modifier,
    laptopId: String
) {
    var laptop by remember { mutableStateOf(LaptopModel())}
    Firebase.firestore.collection("data").document("stock").collection("laptops").document(laptopId)
        .get().addOnCompleteListener {
            if(it.isSuccessful){
                val result = it.result.toObject(LaptopModel::class.java)
                if(result!=null){
                    laptop = result
                }
            }
        }
    val title = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }
    val price = remember { mutableStateOf("") }
    val status = remember { mutableStateOf("") }
    val category = remember { mutableStateOf("") }

    val cpu = remember { mutableStateOf("") }
    val gpu = remember { mutableStateOf("") }
    val ram = remember { mutableStateOf("") }
    val monitor = remember { mutableStateOf("") }
    val pin = remember { mutableStateOf("") }
    val context = LocalContext.current
    LazyColumn(modifier = modifier.padding(16.dp)) {
        item{
            OutlinedTextField(
                value = title.value,
                onValueChange = { title.value = it },
                label = { Text("Tên sản phẩm") },
                placeholder = {
                    Text(laptop.title, color = Color.Gray)
                },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item{
            OutlinedTextField(
                value = description.value,
                onValueChange = { description.value = it },
                label = { Text("Mô tả") },
                placeholder = { Text(laptop.description, color = Color.Gray) },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = price.value,
                onValueChange = { price.value = it },
                label = { Text("Giá") },
                placeholder = { Text(laptop.price, color = Color.Gray) },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item{
            OutlinedTextField(
                value = status.value,
                onValueChange = { status.value = it },
                label = { Text("Trạng thái") },
                placeholder = { Text(laptop.status, color = Color.Gray) },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = category.value,
                onValueChange = { category.value = it },
                label = { Text("Loại sản phẩm") },
                placeholder = { Text(laptop.category, color = Color.Gray) },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item{
            Spacer(modifier = Modifier.height(8.dp))

            Text("Thông số kỹ thuật", fontWeight = FontWeight.Bold)

            OutlinedTextField(
                value = cpu.value,
                onValueChange = { cpu.value = it },
                label = { Text("CPU") },
                placeholder = { Text(laptop.sysDetails["CPU"] ?: "", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item{
            OutlinedTextField(
                value = gpu.value,
                onValueChange = { gpu.value = it },
                label = { Text("GPU") },
                placeholder = { Text(laptop.sysDetails["GPU"] ?: "", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = ram.value,
                onValueChange = { ram.value = it },
                label = { Text("RAM") },
                placeholder = { Text(laptop.sysDetails["RAM"] ?: "", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item{
            OutlinedTextField(
                value = monitor.value,
                onValueChange = { monitor.value = it },
                label = { Text("Màn hình") },
                placeholder = { Text(laptop.sysDetails["Monitor"] ?: "", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = pin.value,
                onValueChange = { pin.value = it },
                label = { Text("Pin") },
                placeholder = { Text(laptop.sysDetails["Pin"] ?: "", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item{
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                val updatedData = mutableMapOf<String, Any>()

                if (title.value.isNotBlank()) updatedData["title"] = title.value
                if (description.value.isNotBlank()) updatedData["description"] = description.value
                if (price.value.isNotBlank()) updatedData["price"] = price.value
                if (status.value.isNotBlank()) updatedData["status"] = status.value
                if (category.value.isNotBlank()) updatedData["category"] = category.value

                val updatedSys = mutableMapOf<String, String>()
                if (cpu.value.isNotBlank()) updatedSys["CPU"] = cpu.value
                if (gpu.value.isNotBlank()) updatedSys["GPU"] = gpu.value
                if (ram.value.isNotBlank()) updatedSys["RAM"] = ram.value
                if (monitor.value.isNotBlank()) updatedSys["Monitor"] = monitor.value
                if (pin.value.isNotBlank()) updatedSys["Pin"] = pin.value

                if (updatedSys.isNotEmpty()) {
                    updatedData["sysDetails"] = updatedSys
                }

                AppUtil.UpdateLaptop(laptopId, updatedData, context)

                GlobalNavigation.navController.popBackStack()
            }) {
                Text("Cập nhật sản phẩm")
            }
        }
    }
}
