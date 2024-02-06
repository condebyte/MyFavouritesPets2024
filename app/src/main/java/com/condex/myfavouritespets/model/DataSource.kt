package com.condex.myfavouritespets.model

import android.content.Context
import com.condex.myfavouritespets.model.pet.PetDbHelper
import com.condex.myfavouritespets.model.pet.SQLitePetDataSource

object DataSource {

    fun petDataSource(context: Context): SQLitePetDataSource {
        // Asegúrate de pasar una instancia de PetDbHelper a SQLitePetDataSource.
        // Puede ser necesario mantener una instancia de PetDbHelper aquí o crear una nueva cada vez.
        return SQLitePetDataSource(PetDbHelper(context))
    }
}