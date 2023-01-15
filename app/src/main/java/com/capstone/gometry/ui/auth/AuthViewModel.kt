package com.capstone.gometry.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.gometry.data.AuthRepository
import com.capstone.gometry.utils.Response
import com.google.firebase.auth.AuthCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {
    private val _loginResponse = MutableLiveData<Response<String>>()
    val loginResponse: LiveData<Response<String>> = _loginResponse

    fun loginWithCredential(credential: AuthCredential) {
        _loginResponse.value = Response.Loading
        authRepository.loginWithCredential(credential) { _loginResponse.value = it }
    }
}