package com.pnx.momassignment.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pnx.momassignment.room.dao.UserDao
import com.pnx.momassignment.room.model.UserItem

@Database(entities = [UserItem::class],  version = 7)
abstract class RoomDB : RoomDatabase() {
    abstract fun userDao(): UserDao
}
