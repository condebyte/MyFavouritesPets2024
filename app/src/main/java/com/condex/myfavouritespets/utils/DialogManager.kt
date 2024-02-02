package com.condex.myfavouritespets.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog

object DialogManager {
    fun alertDialog(context: Context, title: String, message: String) {
        val builder = AlertDialog.Builder(context)
        // Se crea el AlertDialog.
        builder.apply {
            // Se asigna un título.
            setTitle(title)
            // Se asgina el cuerpo del mensaje.
            setMessage(message)
            // Se define el comportamiento de los botones.
            setPositiveButton( android.R.string.ok) { _, _ ->

            }

        }
        // Se muestra el AlertDialog.
        builder.show()
    }
}