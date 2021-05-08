package com.example.spataroapp.data.local

import androidx.room.*
import com.example.spataroapp.data.entities.User
import com.example.spataroapp.data.entities.UserProfile

@Dao
interface AppDataBaseDao {

    //Insert users
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg users: User)

    //update users
    @Update
    fun update(vararg users: User)

    //get all the users in the DB
    @Transaction
    @Query("SELECT * FROM User")
    fun getUsersWithPlaylists(): List<UserProfile>

    //delete all users
    @Query("DELETE FROM User")
    fun deleteAllUsers()
}