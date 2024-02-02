package com.condex.myfavouritespets.model.pet

import android.content.Context
import com.condex.myfavouritespets.utils.FileManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

class FilePetDataSource(private val fileName:String) :IPetDataSource {

    private fun savePets(context: Context, pets: List<Pet>) {
        val file1 = FileManager.getFile(context, fileName)
        try {
            file1.writeText(Gson().toJson(pets))
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun loadPets(context: Context): List<Pet>{
        val file1 = FileManager.getFile(context, fileName)
        return try {
            val json = file1.readText()

            val typeToken = object : TypeToken<List<Pet>>() {}.type
            Gson().fromJson(json, typeToken)


        } catch (e: Exception) {
            mutableListOf()
        }
    }

    override fun addPet(context: Context, pet: Pet) {
        val pets = loadPets(context).toMutableList()
        pets.add(pet)
        savePets(context, pets)
    }

    override fun updatePet(context: Context, id: Int, pet: Pet) {
        val pets = loadPets(context).toMutableList()
        val index = pets.indexOfFirst { it.id == id }
        if (index != -1) {
            pets[index] = pet
            savePets(context,pets)
        }
    }

    override fun borrarPet(context: Context, id: Int) {
        val pets = loadPets(context).toMutableList()
        val index = pets.indexOfFirst { it.id == id }
        if(index != -1){
            pets.removeAt(index)
            savePets(context, pets)
        }
    }

    override fun get(context: Context, id: Int): Pet {
        val pets = loadPets(context)
        return pets.firstOrNull { it.id == id }
            ?: throw NoSuchElementException("Pet not found")
    }

    override fun listPet(context: Context): List<Pet> {
        return loadPets(context)
    }
}