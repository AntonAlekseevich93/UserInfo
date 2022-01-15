package com.example.userinfo.db

import androidx.room.*
import com.example.userinfo.db.entity.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user ORDER BY firstName")
    fun getAllUsers():List<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertListUsers(listUser:List<User>)

    @Update
    fun updateUser(user: User)

    @Query("DELETE FROM user WHERE id = :id")
    fun deleteUser(id: Int)

    @Query("SELECT COUNT (id) FROM user")
    fun getCountUser():Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)

    @Query("SELECT * FROM user WHERE id = :id")
    fun getUser(id:Int):User

}