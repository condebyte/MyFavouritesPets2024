package com.condex.myfavouritespets.model.pet

import android.content.ContentValues
import android.content.Context

class SQLitePetDataSource(private val dbHelper: PetDbHelper) {

    fun addPet(pet: Pet) {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(PetContract.COLUMN_NAME, pet.Nombre)
            put(PetContract.COLUMN_FUR, pet.tipoPelaje)
            put(PetContract.COLUMN_CLASS, pet.clase)
            put(PetContract.COLUMN_PHOTO, pet.foto)
            put(PetContract.COLUMN_LOVE_LEVEL, pet.nivelAmorosidad.name)
            put(PetContract.COLUMN_WEB, pet.enlacePagWeb)
            put(PetContract.COLUMN_FAVORITE, if (pet.favotiro) 1 else 0)
        }

        db.insert(PetContract.TABLE_NAME, null, values)
        db.close()
    }

    fun updatePet(id: Int, pet: Pet) {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(PetContract.COLUMN_NAME, pet.Nombre)
            put(PetContract.COLUMN_FUR, pet.tipoPelaje)
            put(PetContract.COLUMN_CLASS, pet.clase)
            put(PetContract.COLUMN_PHOTO, pet.foto)
            put(PetContract.COLUMN_LOVE_LEVEL, pet.nivelAmorosidad.name)
            put(PetContract.COLUMN_WEB, pet.enlacePagWeb)
            put(PetContract.COLUMN_FAVORITE, if (pet.favotiro) 1 else 0)
        }

        db.update(PetContract.TABLE_NAME, values, "${PetContract.COLUMN_ID}=?", arrayOf(id.toString()))
        db.close()
    }

    fun deletePet(id: Int) {
        val db = dbHelper.writableDatabase
        db.delete(PetContract.TABLE_NAME, "${PetContract.COLUMN_ID}=?", arrayOf(id.toString()))
        db.close()
    }

    fun getPet(id: Int): Pet {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            PetContract.TABLE_NAME,
            null,
            "${PetContract.COLUMN_ID}=?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        if (!cursor.moveToFirst()) {
            cursor.close()
            db.close()
            throw NoSuchElementException("No Pet found with ID $id")
        }

        val pet = Pet(
            id = cursor.getInt(cursor.getColumnIndexOrThrow(PetContract.COLUMN_ID)),
            Nombre = cursor.getString(cursor.getColumnIndexOrThrow(PetContract.COLUMN_NAME)),
            tipoPelaje = cursor.getString(cursor.getColumnIndexOrThrow(PetContract.COLUMN_FUR)),
            clase = cursor.getString(cursor.getColumnIndexOrThrow(PetContract.COLUMN_CLASS)),
            foto = cursor.getString(cursor.getColumnIndexOrThrow(PetContract.COLUMN_PHOTO)),
            nivelAmorosidad = NivelAmor.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(PetContract.COLUMN_LOVE_LEVEL))),
            enlacePagWeb = cursor.getString(cursor.getColumnIndexOrThrow(PetContract.COLUMN_WEB)),
            favotiro = cursor.getInt(cursor.getColumnIndexOrThrow(PetContract.COLUMN_FAVORITE)) > 0
        )

        cursor.close()
        db.close()
        return pet
    }

    fun listPets(): List<Pet> {
        val pets = mutableListOf<Pet>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${PetContract.TABLE_NAME}", null)

        while (cursor.moveToNext()) {
            pets.add(
                Pet(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(PetContract.COLUMN_ID)),
                    Nombre = cursor.getString(cursor.getColumnIndexOrThrow(PetContract.COLUMN_NAME)),
                    tipoPelaje = cursor.getString(cursor.getColumnIndexOrThrow(PetContract.COLUMN_FUR)),
                    clase = cursor.getString(cursor.getColumnIndexOrThrow(PetContract.COLUMN_CLASS)),
                    foto = cursor.getString(cursor.getColumnIndexOrThrow(PetContract.COLUMN_PHOTO)),
                    nivelAmorosidad = NivelAmor.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(PetContract.COLUMN_LOVE_LEVEL))),
                    enlacePagWeb = cursor.getString(cursor.getColumnIndexOrThrow(PetContract.COLUMN_WEB)),
                    favotiro = cursor.getInt(cursor.getColumnIndexOrThrow(PetContract.COLUMN_FAVORITE)) > 0
                )
            )
        }

        cursor.close()
        db.close()
        return pets
    }
}