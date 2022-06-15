package com.dicoding.githubuserappv2.data.remote.retrofit

import com.dicoding.githubuserappv2.data.remote.response.DetailResponse
import com.dicoding.githubuserappv2.data.remote.response.FollowersResponseItem
import com.dicoding.githubuserappv2.data.remote.response.FollowingResponseItem
import com.dicoding.githubuserappv2.data.remote.response.UserResponse
import retrofit2.Call
import retrofit2.http.*


interface ApiService {
    @Headers("Authorization: token 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")
    @GET
    suspend fun getUsers(@Url url: String): UserResponse

    @Headers("Authorization: token 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")
    @GET
    suspend fun getDetailUser(@Url user: String): DetailResponse

    @Headers("Authorization: token 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")
    @GET
    suspend fun getFollowersUser(@Url followers: String): List<FollowersResponseItem>

    @Headers("Authorization: token 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")
    @GET
    suspend fun getFollowingUser(@Url following: String): List<FollowingResponseItem>
}