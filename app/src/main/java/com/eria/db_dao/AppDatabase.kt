package com.eria.db_dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.eria.db_dao.model.Address

@Database(entities = [Address::class], version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun addressDao(): AddressDao
}