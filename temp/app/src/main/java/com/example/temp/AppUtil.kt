package com.example.temp

import android.content.Context
import android.widget.Toast

object AppUtil {
    fun showToast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}