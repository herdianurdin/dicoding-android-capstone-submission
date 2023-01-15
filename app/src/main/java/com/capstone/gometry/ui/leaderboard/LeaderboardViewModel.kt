package com.capstone.gometry.ui.leaderboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.gometry.data.UserRepository
import com.capstone.gometry.data.model.User
import com.capstone.gometry.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val repository: UserRepository
): ViewModel() {
    private val _users = MutableLiveData<Response<List<User>>>()
    val users: LiveData<Response<List<User>>> = _users

    fun getUsers() {
        _users.value = Response.Loading
        repository.getUsers { _users.value = it }
    }
}