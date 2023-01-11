package com.daniladorokhov.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GalleryRepository {
    private var retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://api.unsplash.com/")
        .build()
    private var api = retrofit.create(Api::class.java)

    fun loadData(page: Int/*, onComplete: () -> Unit*/): LiveData<List<Gallery>>{
        var listLive = MutableLiveData<List<Gallery>>()
        var list = arrayListOf<Gallery>()
        api.listRepos(page).enqueue(object: Callback<List<Gallery>> {
            override fun onResponse(call: Call<List<Gallery>>, response: Response<List<Gallery>>) {
                try {
                    for (item in response.body()!!) {
                        list.add(item)
                    }
                    listLive.value = list
                }catch (e: Exception) {

                }
            }

            override fun onFailure(call: Call<List<Gallery>>, t: Throwable) {

            }
        })
        return listLive
    }
}