package com.pnx.momassignment.room

import android.content.Context
import androidx.room.Room

class RoomDBClient() {
//                val db = Room.databaseBuilder(
//                    requireContext(), ErrorDatabase::class.java,"databasate-name")
//                    .fallbackToDestructiveMigration()
//                    .build()

//    val db = Room.databaseBuilder(context, ErrorDatabase::class.java, "APP_DB").build()

    companion object {
        private const val DB_NAME = "APP_DB"
        /**
         * 에러 코드 메시지 DB
         */
        fun getUserDatabase(context: Context): RoomDB {
            return Room.databaseBuilder(context, RoomDB::class.java, DB_NAME)
                .fallbackToDestructiveMigration()
//                .allowMainThreadQueries()
                .build()
        }

        /**
         * 에러 코드 메시지 DB
         */
//        fun getStoreDatabase(context: Context): RoomDBStore {
//            return Room.databaseBuilder(context, RoomDBStore::class.java, DB_NAME)
//                .fallbackToDestructiveMigration()
////                .allowMainThreadQueries()
//                .build()
//        }
    }
}
