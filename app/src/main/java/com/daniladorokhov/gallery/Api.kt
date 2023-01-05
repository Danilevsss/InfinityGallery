package com.daniladorokhov.gallery

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

const val CLIENT_ID = ""

interface Api {
    @GET("photos")
    fun listRepos(@Query("page") page: Int, @Query("client_id") id: String = CLIENT_ID): Call<List<Gallery>>
}
