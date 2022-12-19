package com.axuca.spacexfan.util

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun showSnackBar(view: View, text: String) {
    Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show()
}