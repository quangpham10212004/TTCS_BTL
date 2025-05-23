package com.example.temp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
// ham ma hinh dang nhap
@Composable
fun GDDangNhap(modifier: Modifier = Modifier,
                navController: NavHostController,
                authViewModel: AuthViewModel = viewModel()) {
    var email = remember { mutableStateOf("") }
    var password = remember { mutableStateOf("") }
    var isLoading = remember { mutableStateOf(false) }
    var context = LocalContext.current
    Column (
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Text (
            text = "Enter your details to login",
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
            text = "Fill the form below to login",
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
            )
        )

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(                              // nhap email
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(                              //nhap password
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            isLoading.value = true;
            authViewModel.login(email.value, password.value){ success, errorMessage ->
                if(success) {
                    isLoading.value = false;
                    navController.navigate("home") {
                        popUpTo("auth") { inclusive = true }
                    }
                } else{
                    isLoading.value = false;
                    AppUtil.showToast(context, errorMessage?: "Something went wrong")
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
                text = if(isLoading.value) "Logging in" else "Login",
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