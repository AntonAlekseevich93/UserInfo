package com.example.userinfo.db

import androidx.room.*
import com.example.userinfo.db.entity.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAllUsers():List<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertListUsers(listUser:List<User>)

    @Update
    fun updateUser(user: User)

    @Delete
    fun deleteUser(user: User)

    @Query("SELECT COUNT (id) FROM user")
    fun getCountUser():Int

    @Insert
    fun insert(user: User)


}