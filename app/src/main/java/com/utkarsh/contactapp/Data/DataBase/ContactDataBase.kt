package com.utkarsh.contactapp.Data.DataBase

import androidx.room.Database
import androidx.room.RoomDatabase
@Database(entities = [Contact::class], version = 1, exportSchema = true)
abstract class ContactDataBase : RoomDatabase() {
    abstract fun getDao(): Dao

}