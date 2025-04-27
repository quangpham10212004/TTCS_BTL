package com.example.temp.ui.screens

import androidx.compose.foundation.Image
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

@Composable
fun SignupScreen(modifier: Modifier = Modifier, navController: NavHostController, authViewModel: AuthViewModel = viewModel()) {
    var email = remember { mutableStateOf("") }
    var name = remember { mutableStateOf("") }
    var password = remember { mutableStateOf("")}
    var context = LocalContext.current
    var isLoading = remember { mutableStateOf(false) }
    Column (
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Text (
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
            modifier = Modifier.fillMaxWidth().height(200.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = name.value,
            onValueChange = { name.value = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            isLoading.value = true
            authViewModel.signup(email.value, name.value, password.value){ success, errorMassage ->
                if(success){
                    isLoading.value = false
                    navController.navigate("home")
                    {
                        popUpTo("auth")
                        {
                            inclusive = true
                        }
                    }
                }
                else{
                    isLoading.value = false
                    AppUtil.showToast(context,errorMassage?: "Something went wrong")
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
                text = if(isLoading.value) "Creating account" else "Sign up",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                ))
        }
    }
}