package com.daniladorokhov.gallery

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AuthRepository {
    private var auth = Firebase.auth
    private var user = auth.currentUser


    fun getCurrentUser(): FirebaseUser?{
        return user
    }

    fun register(email: String, password: String): LiveData<Pair<FirebaseUser?, String?>>{
        var userLiveData = MutableLiveData<Pair<FirebaseUser?, String?>>()
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                user = auth.currentUser
                userLiveData.value = Pair(auth.currentUser, "")
            } else {
                userLiveData.value = Pair(auth.currentUser, task.exception?.message.toString())
            }
        }
        return userLiveData
    }


    fun logIn(email: String, password: String): LiveData<Pair<FirebaseUser?, String?>>{
        var userLiveData = MutableLiveData<Pair<FirebaseUser?, String?>>()
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                user = auth.currentUser
                userLiveData.value = Pair(auth.currentUser, "")
            } else {
                userLiveData.value = Pair(auth.currentUser, task.exception?.message.toString())
            }
        }
        return userLiveData
    }

    fun logOut(){
        auth.signOut()
        user = null
    }
}