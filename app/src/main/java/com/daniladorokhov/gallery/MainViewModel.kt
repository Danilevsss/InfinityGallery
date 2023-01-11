package com.daniladorokhov.gallery

import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel(var lifecycleOwner: LifecycleOwner): ViewModel() {
    
    var galleryRepository = GalleryRepository()
    var authRepository = AuthRepository()
    private var _galleryList = mutableStateListOf<Gallery>()
    val galleryList: List<Gallery> get() = _galleryList
    private var _loadInProgress = mutableStateOf<Boolean>(false)
    val loadInProgress:State<Boolean> get() = _loadInProgress
    private var page = 1
    private var _isLoggedIn = mutableStateOf(false)
    val isLoggedIn:State<Boolean> get() = _isLoggedIn
    private var _isLoggedInLive = MutableLiveData(false)
    val isLoggedInLive:LiveData<Boolean> get() = _isLoggedInLive
    private var _registerError = mutableStateOf<Boolean>(false)
    val registerError:State<Boolean> get() = _registerError
    private var _registerErrorMessage = mutableStateOf<String>("")
    val registerErrorMessage get() = _registerErrorMessage


    fun updateData(){
        _loadInProgress.value = true
        galleryRepository.loadData(page).observe(lifecycleOwner) {
            _galleryList.addAll(it)
            _loadInProgress.value = false
            page += 1
        }
    }

    fun pressLike(id: Int){
        _galleryList[id].liked.value = !_galleryList[id].liked.value
    }

    fun checkLogin(){
        var user = authRepository.getCurrentUser()
        _isLoggedIn.value = user != null
    }

    fun register(email: String, password: String){
        authRepository.register(email, password).observe(lifecycleOwner){
            if (it.first != null){
                _isLoggedIn.value = true
                _isLoggedInLive.value = true
            }else{
                _registerError.value = true
                _registerErrorMessage.value = it.second?:""
            }
        }
    }

    fun logIn(email: String, password: String){
        authRepository.logIn(email, password).observe(lifecycleOwner){
            if (it.first != null){
                _isLoggedIn.value = true
                _isLoggedInLive.value = true
            }else{
                _registerError.value = true
                _registerErrorMessage.value = it.second?:""
            }
        }
    }

    fun logOut(){
        authRepository.logOut()
        checkLogin()
    }

    fun hideRegisterError(){
        _registerError.value = false
    }
}