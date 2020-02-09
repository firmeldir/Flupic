package com.example.flupic.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.flupic.data.users.local.UserDao
import com.example.flupic.data.users.local.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        private const val databaseName = "muzzly-db"

        fun buildDatabase(context: Context): AppDatabase  =
            Room.databaseBuilder(context, AppDatabase::class.java, databaseName)
                .build()
    }
}