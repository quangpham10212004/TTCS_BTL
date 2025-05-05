package com.example.temp

import android.content.Context
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

object AppUtil { // dang chuc nang con co app
    fun showToast(context: Context, msg: String) { // show thanh tbao nho
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun AddToCart(laptopId: String, context: Context){ // them vao cart
        var userDoc = Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)
        userDoc.get().addOnCompleteListener {
            if(it.isSuccessful){
                val currentCart = it.result?.get("myCart") as? Map<String, Long> ?: emptyMap()
                val currentQuantity = currentCart[laptopId] ?: 0
                val updatedQuantity = currentQuantity + 1

                val updatedCart = mapOf("myCart.$laptopId" to updatedQuantity)
                userDoc.update(updatedCart)
                    .addOnCompleteListener {
                    if(it.isSuccessful){
                        showToast(context,"Successfully Added to Cart")
                    }
                    else{
                        showToast(context,"Failed to Add To Cart")
                    }
                }
            }
        }
    }

}

