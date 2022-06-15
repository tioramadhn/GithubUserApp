package com.dicoding.githubuserappv2.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.dicoding.githubuserappv2.data.local.entity.UserEntity
import com.dicoding.githubuserappv2.data.local.room.UserDao
import com.dicoding.githubuserappv2.data.remote.response.DetailResponse
import com.dicoding.githubuserappv2.data.remote.response.FollowersResponseItem
import com.dicoding.githubuserappv2.data.remote.response.FollowingResponseItem
import com.dicoding.githubuserappv2.data.remote.response.ItemsItem
import com.dicoding.githubuserappv2.data.remote.retrofit.ApiService

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userDao: UserDao,
) {
    fun findUsers(user: String): LiveData<Status<List<ItemsItem>>> = liveData {
        emit(Status.Loading)
        try {
            val response = apiService.getUsers("search/users?q=$user")
            val users = response.items
            if (users.isEmpty()) {
                emit(Status.NotFound)
            } else {
                val userList = users.map { data ->
                    ItemsItem(
                        data.login,
                        data.avatarUrl,
                    )
                }
                emit(Status.Success(userList))
            }
        } catch (e: Exception) {
            Log.d("UserRepository", "findUsers: ${e.message.toString()} ")
            emit(Status.Error(e.message.toString()))
        }
    }

    fun getDetailUser(user: String): LiveData<Status<DetailResponse>> = liveData {
        emit(Status.Loading)
        try {
            val response = apiService.getDetailUser("users/$user")
            emit(Status.Success(response))
        } catch (e: Exception) {
            Log.d("UserRepository", "getDetailUser: ${e.message.toString()} ")
            emit(Status.Error(e.message.toString()))
        }
    }

    fun getFollowersUser(user: String): LiveData<Status<List<FollowersResponseItem>>> = liveData {
        emit(Status.Loading)
        try {
            val response = apiService.getFollowersUser("users/${user}/followers")
            if(response.isEmpty()){
                emit(Status.NotFound)
            }else{
                emit(Status.Success(response))
            }
        } catch (e: Exception) {
            Log.d("UserRepository", "getFollowersUser: ${e.message.toString()} ")
            emit(Status.Error(e.message.toString()))
        }
    }

    fun getFollowingUser(user: String): LiveData<Status<List<FollowingResponseItem>>> = liveData {
        emit(Status.Loading)
        try {
            val response = apiService.getFollowingUser("users/${user}/following")
            if(response.isEmpty()){
                emit(Status.NotFound)
            }else{
                emit(Status.Success(response))
            }
        } catch (e: Exception) {
            Log.d("UserRepository", "getFollowingUser: ${e.message.toString()} ")
            emit(Status.Error(e.message.toString()))
        }
    }

    suspend fun setFavoriteUser(user: UserEntity, isFav: Boolean){
        if(isFav){
            userDao.insertOneUser(user)
        }else{
            userDao.deleteUser(user.username)
        }
    }

    fun getFavUsers(): LiveData<List<UserEntity>>{
        return userDao.getUsers()
    }

    fun isFavorite(user: String): LiveData<Status<Boolean>> = liveData {
        emit(Status.Success(userDao.isFavorite(user)))
    }

    fun getUser(user: String): LiveData<Status<UserEntity>> = liveData {
        emit(Status.Success(userDao.getUserByUserName(user)))
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            userDao: UserDao,
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userDao)
            }.also { instance = it }
    }
}