package com.pnx.momassignment.room

import android.content.Context
import androidx.room.Room

class RoomDBClient() {
    companion object {
        private const val DB_NAME = "APP_DB"
        /**
         * 사용자 리스트 DB
         */
        fun getUserDatabase(context: Context): RoomDB {
            return Room.databaseBuilder(context, RoomDB::class.java, DB_NAME)
                .fallbackToDestructiveMigration()
                .build()
        }

    }
}
