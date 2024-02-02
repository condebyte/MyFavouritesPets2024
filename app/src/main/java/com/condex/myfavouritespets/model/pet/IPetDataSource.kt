package com.condex.myfavouritespets.model.pet

import android.content.Context

interface IPetDataSource {

    fun addPet(context:Context, pet: Pet)

    fun updatePet(context: Context, id: Int, pet: Pet)

    fun borrarPet(context: Context, id: Int)

    fun get(context: Context, id:Int):Pet

    fun listPet(context:Context):List<Pet>
}