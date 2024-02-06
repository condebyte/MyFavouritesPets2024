package com.condex.myfavouritespets.model.pet

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class PetDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "PetsDatabase.db"

        private const val SQL_CREATE_ENTRIES =
            "CREATE TABLE ${PetContract.TABLE_NAME} (" +
                    "${PetContract.COLUMN_ID} INTEGER PRIMARY KEY," +
                    "${PetContract.COLUMN_NAME} TEXT," +
                    "${PetContract.COLUMN_FUR} TEXT," +
                    "${PetContract.COLUMN_CLASS} TEXT," +
                    "${PetContract.COLUMN_PHOTO} TEXT," +
                    "${PetContract.COLUMN_LOVE_LEVEL} TEXT," +
                    "${PetContract.COLUMN_WEB} TEXT," +
                    "${PetContract.COLUMN_FAVORITE} INTEGER)"

        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${PetContract.TABLE_NAME}"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }




}