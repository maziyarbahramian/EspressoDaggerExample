package com.mazibahrami.example.ui

import android.app.Activity
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.mazibahrami.example.R

fun Activity.displayToastMessage(message: String, length: Int) {
    Toast.makeText(this, message, length).show()
}

fun Activity.displayErrorDialog(
    errorMessage: String?,
    callback: ErrorDialogCallback
): MaterialDialog {
    return MaterialDialog(this)
        .show {
            title(R.string.text_error)
            message(text = errorMessage)
            positiveButton(R.string.text_ok) {
                callback.clearError()
                dismiss()
            }
            cancelOnTouchOutside(false)
        }
}

interface ErrorDialogCallback {

    fun clearError()
}















