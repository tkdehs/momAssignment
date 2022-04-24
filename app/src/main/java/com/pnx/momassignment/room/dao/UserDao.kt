package com.pnx.momassignment.room.dao

import androidx.room.*
import com.pnx.momassignment.room.model.UserItem

@Dao
interface UserDao {
    @Query("SELECT * FROM useritem")
    fun getAll(): List<UserItem>

    @Query("SELECT id FROM useritem WHERE id = :id LIMIT 1")
    fun getUserId(id: Int): Int?
    @Query("SELECT * FROM useritem WHERE id = :id LIMIT 1")
    fun getUser(id: Int): UserItem

    @Insert
    fun insertAll(userList: ArrayList<UserItem>)

    @Insert
    fun insert(vararg item: UserItem)

    @Update
    fun update(vararg item: UserItem)

    @Delete
    fun delete(vararg item: UserItem)
}