package com.daniladorokhov.gallery

import androidx.compose.runtime.mutableStateOf
import com.google.gson.annotations.SerializedName

class Gallery{
    @SerializedName("id")
    lateinit var id: String
    @SerializedName("description")
    var description: String? = null
    @SerializedName("urls")
    lateinit var urls: Urls
    @SerializedName("user")
    lateinit var user: User

    var liked = mutableStateOf(false)

    class Urls {
        @SerializedName("regular")
        lateinit var regular: String
    }
    class User{
        @SerializedName("name")
        lateinit var name: String
    }
    override fun toString(): String {
        return id
    }
}
