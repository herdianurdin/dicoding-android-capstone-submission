package com.capstone.gometry.data.repository

import android.content.Context
import com.capstone.gometry.utils.Response
import com.google.firebase.auth.AuthCredential

interface FirebaseAuthRepository {
    fun loginWithCredential(credential: AuthCredential, response: (Response<String>) -> Unit)

    fun logout(context: Context, response: () -> Unit)
}