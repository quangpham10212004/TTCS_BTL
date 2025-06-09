package com.example.temp

import android.R
import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.common.internal.safeparcel.SafeParcelable
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

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

    fun DeleteOrder(orderId: String, context: Context){
        val orderDoc = Firebase.firestore.collection("orders").document(orderId)
            .get().addOnCompleteListener {
                if(it.isSuccessful){
                    val updatedData = it.result
                    if(updatedData.exists()){
                        Firebase.firestore.collection("orders").document(orderId).delete().addOnCompleteListener {
                            showToast(context, "Xóa đơn hàng thành công")
                        }
                    }

                }

            }
    }

    fun SetAdmin(userId: String, context: Context, onRoleUpdated: (String) -> Unit){
        val userDoc = Firebase.firestore.collection("users").document(userId)
            userDoc.get().addOnCompleteListener {
                if(it.isSuccessful){
                    val doc = it.result
                    if(doc != null && doc.exists()) {
                        val currentRole = doc.getString("role") ?:"client"
                        val newRole = if(currentRole == "admin") "client" else "admin"
                        userDoc.update("role", newRole).addOnCompleteListener { task ->
                            if(task.isSuccessful){
                                showToast(context,"Successfully Set ${currentRole} to ${newRole}")
                                onRoleUpdated(newRole)
                            }
                            else{
                                showToast(context,"Failed to Set ${currentRole} to ${newRole}")
                            }
                        }
                    }
                    else{
                        showToast(context,"Tài khoản không tồn tại")
                    }
                }
                else{
                    showToast(context,"Something went wrong")
                }
            }
    }

    fun DeleteItem(laptopId: String, context: Context, onSuccess : ()->Unit= {}){
        Firebase.firestore.collection("data").document("stock").collection("laptops").document(laptopId)
            .delete().addOnCompleteListener {
                if(it.isSuccessful){
                    showToast(context,"Successfully Removed From Stock")
                }
                else{
                    showToast(context,"Failed to Remove From Stock")
                }
            }
    }

    fun AddNew(newLaptop: Map<String, Any>, context: Context, onComplete: (() -> Unit)? = null) {
        val laptops = Firebase.firestore.collection("data")
            .document("stock")
            .collection("laptops")

        val newDocRef = laptops.document()
        val laptopWithId = newLaptop.toMutableMap().apply {
            this["id"] = newDocRef.id
        }

        newDocRef.set(laptopWithId)
            .addOnSuccessListener {
                showToast(context, "Đã thêm sản phẩm mới thành công")
                onComplete?.invoke()
            }
            .addOnFailureListener {
                showToast(context, "Thêm sản phẩm thất bại: ${it.message}")
            }
    }


    fun UpdateLaptop(
        id: String,
        updatedFields: Map<String, Any>,
        context: Context
    ) {
        val db = Firebase.firestore
        val laptopRef = db.collection("data")
            .document("stock")
            .collection("laptops")
            .document(id)

        laptopRef.update(updatedFields)
            .addOnSuccessListener {
                showToast(context, "Đã cập nhật laptop thành công")
            }
            .addOnFailureListener {
                showToast(context, "Cập nhật thất bại: ${it.message}")
            }
    }

}