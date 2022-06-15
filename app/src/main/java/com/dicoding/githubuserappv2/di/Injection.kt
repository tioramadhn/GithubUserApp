package com.dicoding.githubuserappv2.di

import android.content.Context
import com.dicoding.githubuserappv2.data.UserRepository
import com.dicoding.githubuserappv2.data.local.room.UserDatabase
import com.dicoding.githubuserappv2.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val apiService = ApiConfig.getApiService()
        val database = UserDatabase.getInstance(context)
        val dao = database.userDao()
        return UserRepository.getInstance(apiService, dao)
    }
}