package com.example.temp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.temp.R
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.temp.AppUtil
import com.example.temp.viewmodel.AuthViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
// ham cho form dang ki
@Composable
fun GDDangKy(modifier: Modifier = Modifier, navController: NavHostController, authViewModel: AuthViewModel = viewModel()) {
    var email = remember { mutableStateOf("") }
    var name = remember { mutableStateOf("") }
    var address = remember { mutableStateOf("") }
    var phone = remember { mutableStateOf("") }
    var password = remember { mutableStateOf("")}
    var context = LocalContext.current
    var isLoading = remember { mutableStateOf(false) }
    // khoi tao cac thong tin cua user
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = "Signup your account",
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle(
                    fontSize = 30.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Fill the form below to create an account",
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center,
                )
            )
            Image(
                painter = painterResource(id = R.drawable.signup_logo),
                contentDescription = "Signup Background",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            OutlinedTextField( // dong nhap email
                value = email.value,
                onValueChange = { email.value = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = name.value, // dong nhap ten
                onValueChange = { name.value = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = address.value, // dong nhap dia chi
                onValueChange = { address.value = it },
                label = { Text("Address") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = phone.value, // dong nhap sdt
                onValueChange = { phone.value = it },
                label = { Text("Phone number") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField( // dong nhap password
                value = password.value,
                onValueChange = { password.value = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Button( // click nut sign up
                onClick = {
                    isLoading.value = true
                    authViewModel.signup(email.value, name.value, password.value) { success, errorMassage ->
                        if (success) {
                            isLoading.value = false
                            var userDoc = Firebase.firestore.collection("users")
                                .document(FirebaseAuth.getInstance().currentUser?.uid!!)
                            userDoc.get().addOnCompleteListener { // get nguoi dung vua duoc dnag ki
                                if (it.isSuccessful) {
                                    // set address va phone default ve string rong
                                    val  old_address = it.result?.get("address").toString()?:""
                                    val old_phone = it.result?.get("phone").toString()?:""
                                    val updates = mutableMapOf<String, Any>() // map trong kotlin immutable
                                    // update 2 file address va phone thanh 2 cai da nhap o tren
                                    if (address.value != old_address) {
                                        updates["address"] = address.value
                                    }
                                    if (phone.value != old_phone) {
                                        updates["phone"] = phone.value
                                    }
                                    if(updates.isNotEmpty()) { // update vao trong userDoc (data cho users)
                                        userDoc.update(updates).addOnCompleteListener { task->
                                            if(task.isSuccessful) {
                                                navController.navigate("home") {
                                                    popUpTo("auth") { inclusive = true }
                                                }
                                            }
                                            else
                                                AppUtil.showToast(context, "Something went wrong")
                                        }
                                    }
                                }
                            }

                            navController.navigate("home") {
                                popUpTo("auth") { inclusive = true }
                            }

                        } else {
                            isLoading.value = false
                            AppUtil.showToast(context, errorMassage ?: "Something went wrong")
                        }
                    }
                },
                enabled = !isLoading.value,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp)
                    .height(60.dp)
            ) {
                Text(
                    text = if (isLoading.value) "Creating account" else "Sign up",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                    )
                )
            }
        }
    }

}