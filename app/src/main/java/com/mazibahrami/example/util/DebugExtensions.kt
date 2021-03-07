package com.mazibahrami.example.util

import android.util.Log
import com.mazibahrami.example.util.Constants.DEBUG
import com.mazibahrami.example.util.Constants.TAG

fun printLogD(className: String?, message: String ) {
    if (DEBUG) {
        Log.d(TAG, "$className: $message")
    }
}