package com.dicoding.githubuserappv2.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    @ColumnInfo(name = "username")
    val username: String,

    @ColumnInfo(name = "avatar")
    var avatar: String,

    @ColumnInfo(name = "isFavorite")
    var isFavorite: Boolean? = null

): Parcelable