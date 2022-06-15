package com.dicoding.githubuserappv2.data.remote.response

import com.google.gson.annotations.SerializedName

data class FollowingResponse(

    @field:SerializedName("FollowingResponse")
    val followingResponse: List<FollowingResponseItem>
)

data class FollowingResponseItem(

    @field:SerializedName("login")
    val login: String,

    @field:SerializedName("avatar_url")
    val avatarUrl: String,
)
