package com.dicoding.githubuserappv2.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dicoding.githubuserappv2.data.local.entity.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getUsers(): LiveData<List<UserEntity>>

    @Query("SELECT * FROM users WHERE username = :username")
    fun getUserByUserName(username: String): UserEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOneUser(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertManyUser(news: List<UserEntity>)

    @Query("DELETE FROM users WHERE username = :username")
    suspend fun deleteUser(username: String)

    @Query("DELETE FROM users")
    suspend fun deleteAll()

    @Query("SELECT EXISTS(SELECT * FROM users WHERE username = :username)")
    suspend fun isFavorite(username: String): Boolean
}