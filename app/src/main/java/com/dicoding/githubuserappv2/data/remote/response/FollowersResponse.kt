package com.dicoding.githubuserappv2.data.remote.response

import com.google.gson.annotations.SerializedName

data class FollowersResponseItem(

    @field:SerializedName("login")
    val login: String,

    @field:SerializedName("avatar_url")
    val avatarUrl: String,
)

data class FollowersResponse(
    @field:SerializedName("followersResponse")
    val followersResponse: List<FollowersResponseItem>
)
