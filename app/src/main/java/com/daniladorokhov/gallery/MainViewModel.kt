package com.daniladorokhov.gallery

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainViewModel: ViewModel() {
    private var _galleryList = mutableStateListOf<Gallery>()
    val galleryList: List<Gallery> get() = _galleryList
    private var _loadInProgress = mutableStateOf<Boolean>(false)
    val loadInProgress get() = _loadInProgress

    private var retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://api.unsplash.com/")
        .build()
    private var api = retrofit.create(Api::class.java)
    private var page = 1

    fun updateData(){
        _loadInProgress.value = true
        api.listRepos(page).enqueue(object: Callback<List<Gallery>>{
            override fun onResponse(call: Call<List<Gallery>>, response: Response<List<Gallery>>) {
                try {
                    for (item in response.body()!!) {
                        _galleryList.add(item)
                    }
                    _loadInProgress.value = false
                    page += 1
                }catch (e: Exception) {

                }
            }

            override fun onFailure(call: Call<List<Gallery>>, t: Throwable) {

            }
        })
    }

    fun pressLike(id: Int){
        _galleryList[id].liked.value = !_galleryList[id].liked.value
    }
}