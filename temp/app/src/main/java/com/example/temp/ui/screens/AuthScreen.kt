package com.example.temp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.temp.AppNavigation
import com.example.temp.R

@Composable
fun AuthScreen(modifier: Modifier = Modifier, navController: NavHostController) {
    Column (
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp)
            ,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Image(
            painter = painterResource(id = R.drawable.auth_bg),
            contentDescription = "Auth Background",
            modifier = Modifier.fillMaxWidth().height(200.dp),
        )
        Text(
            text = "Start finding your favorite laptop now",
            style = TextStyle(
                fontSize = 30.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
            )
        )
        Button(
            onClick =  { navController.navigate("login") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
                .height(60.dp)
        ) {
            Text(
                text = "Login",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                ))
        }
        OutlinedButton(
            onClick =  {navController.navigate("signup")},
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
                .height(60.dp)
        ) {
            Text(
                text = "Signup",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                ))
        }

    }
}