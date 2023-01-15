package com.capstone.gometry.ui.profile

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.gometry.data.AuthRepository
import com.capstone.gometry.data.UserRepository
import com.capstone.gometry.data.model.User
import com.capstone.gometry.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
): ViewModel() {
    private val _user = MutableLiveData<Response<User?>>()
    val user: LiveData<Response<User?>> = _user

    fun getCurrentUser() {
        _user.value = Response.Loading
        userRepository.getCurrentUser { _user.value = it }
    }

    fun logout(context: Context, result: () -> Unit) {
        authRepository.logout(context, result)
    }
}