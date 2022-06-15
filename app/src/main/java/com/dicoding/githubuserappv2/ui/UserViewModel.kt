package com.dicoding.githubuserappv2.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.githubuserappv2.data.UserRepository
import com.dicoding.githubuserappv2.data.local.entity.UserEntity
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getFindUser(user: String) = userRepository.findUsers(user)

    fun getDetailUser(user: String) = userRepository.getDetailUser(user)

    fun getFollowers(user: String) = userRepository.getFollowersUser(user)

    fun getFollowing(user: String) = userRepository.getFollowingUser(user)

    fun setFavUser(user: UserEntity) {
        viewModelScope.launch {
            userRepository.setFavoriteUser(user, true)
        }
    }

    fun removeFavUser(user: UserEntity) {
        viewModelScope.launch {
            userRepository.setFavoriteUser(user, false)
        }
    }
    fun isFavorite(user: String) = userRepository.isFavorite(user)

    fun getFavoriteUsers() = userRepository.getFavUsers()

    fun getUser(user: String) = userRepository.getUser(user)

}