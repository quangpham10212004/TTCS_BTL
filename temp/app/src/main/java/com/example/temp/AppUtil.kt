package com.example.temp

import android.R
import android.content.Context
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.common.internal.safeparcel.SafeParcelable
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
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

    fun DecreaseNumItem(laptopId: String, context: Context, wantToDelete: Boolean = false){ // xoa 1 sp khoi cart
        var userDoc = Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)
        userDoc.get().addOnCompleteListener {
            if(it.isSuccessful){
                val currentCart = it.result?.get("myCart") as? Map<String, Long> ?: emptyMap()
                val currentQuantity = currentCart[laptopId] ?: 0
                val updatedQuantity = currentQuantity - 1

                val updatedCart =
                    if(updatedQuantity < 1L || wantToDelete )
                        mapOf("myCart.$laptopId" to FieldValue.delete())
                    else mapOf("myCart.$laptopId" to updatedQuantity)
                userDoc.update(updatedCart)
                    .addOnCompleteListener {
                        if(it.isSuccessful){
                            showToast(context,"Successfully Removed From Cart")
                        }
                        else{
                            showToast(context,"Failed to Remove From Cart")
                        }
                    }
            }
        }
    }

    fun IncreaseNumItem(laptopId: String, context: Context){ // them 1 vao cart
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
                            showToast(context,"Successfully Icreased Item In Cart")
                        }
                        else{
                            showToast(context,"Failed to Increase Item In Cart")
                        }
                    }
            }
        }
    }

    fun getDiscount(): Float {
        return 10.0f
    }

}

