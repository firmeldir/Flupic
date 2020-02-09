package com.example.flupic.data.users.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.flupic.model.User

@Entity(tableName = "users")
data class UserEntity(

    @PrimaryKey @ColumnInfo(name = "uid")  val id: String,

    @ColumnInfo(name = "username") val username: String,

    @ColumnInfo(name = "name") val name: String,

    @ColumnInfo(name = "bio") val bio: String,

    @ColumnInfo(name = "photoUrl") val photoUrl: String,

    @ColumnInfo(name = "phoneNumber") val phoneNumber: String

    ){

    fun toUser(): User = User(id, username, name, bio, photoUrl, phoneNumber)
}

fun User.toUserEntity() : UserEntity = UserEntity(id, username, name, bio, photoUrl, phoneNumber)