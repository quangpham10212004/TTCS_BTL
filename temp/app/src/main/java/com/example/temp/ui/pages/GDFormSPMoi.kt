package com.example.temp.ui.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.temp.AppUtil
import com.example.temp.GlobalNavigation

@Composable
fun GDFormSPMoi(modifier: Modifier = Modifier) {
    val category = remember { mutableStateOf("") }
    val title = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }
    val price = remember { mutableStateOf("") }
    val status = remember { mutableStateOf("") }

    val cpu = remember { mutableStateOf("") }
    val gpu = remember { mutableStateOf("") }
    val ram = remember { mutableStateOf("") }
    val monitor = remember { mutableStateOf("") }
    val pin = remember { mutableStateOf("") }

    val currentImageUrl = remember { mutableStateOf("") }
    val imageUrls = remember { mutableStateListOf<String>() }
    val context = LocalContext.current
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(bottom = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Spacer(modifier = Modifier.height(32.dp))
            Text("Thêm laptop mới", fontSize = 20.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(thickness = 1.dp)
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            OutlinedTextField(value = category.value, onValueChange = { category.value = it },
                label = { Text("Hãng ") }, modifier = Modifier.fillMaxWidth())

            OutlinedTextField(value = title.value, onValueChange = { title.value = it },
                label = { Text("Tên sản phẩm") }, modifier = Modifier.fillMaxWidth())

            OutlinedTextField(value = description.value, onValueChange = { description.value = it },
                label = { Text("Mô tả") }, modifier = Modifier.fillMaxWidth().height(120.dp),
                maxLines = 6)

            OutlinedTextField(value = price.value, onValueChange = { price.value = it },
                label = { Text("Giá ") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth())

            OutlinedTextField(value = status.value, onValueChange = { status.value = it },
                label = { Text("Trạng thái ") }, modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(12.dp))
            Text("Thông số kỹ thuật", fontWeight = FontWeight.SemiBold)

            OutlinedTextField(value = cpu.value, onValueChange = { cpu.value = it },
                label = { Text("CPU") }, modifier = Modifier.fillMaxWidth())

            OutlinedTextField(value = gpu.value, onValueChange = { gpu.value = it },
                label = { Text("GPU") }, modifier = Modifier.fillMaxWidth())

            OutlinedTextField(value = ram.value, onValueChange = { ram.value = it },
                label = { Text("RAM") }, modifier = Modifier.fillMaxWidth())

            OutlinedTextField(value = monitor.value, onValueChange = { monitor.value = it },
                label = { Text("Màn hình") }, modifier = Modifier.fillMaxWidth())

            OutlinedTextField(value = pin.value, onValueChange = { pin.value = it },
                label = { Text("Pin") }, modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(12.dp))
            Text("Thêm URL ảnh", fontWeight = FontWeight.SemiBold)
        }

        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = currentImageUrl.value,
                    onValueChange = { currentImageUrl.value = it },
                    label = { Text("Nhập URL ảnh") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    if (currentImageUrl.value.isNotBlank()) {
                        imageUrls.add(currentImageUrl.value.trim())
                        currentImageUrl.value = ""
                    }
                }) {
                    Text("Thêm")
                }
            }
        }

        if (imageUrls.isNotEmpty()) {
            items(imageUrls.size) { index ->
                Text(
                    text = "${index + 1}. ${imageUrls[index]}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val newLaptop = mapOf(
                        "category" to category.value,
                        "title" to title.value,
                        "description" to description.value,
                        "price" to price.value,
                        "status" to status.value,
                        "images" to imageUrls.toList(),
                        "sysDetails" to mapOf(
                            "CPU" to cpu.value,
                            "GPU" to gpu.value,
                            "RAM" to ram.value,
                            "Monitor" to monitor.value,
                            "Pin" to pin.value
                        )
                    )
                    AppUtil.AddNew(newLaptop, context )
                    GlobalNavigation.navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Thêm sản phẩm")
            }
        }
    }

}
