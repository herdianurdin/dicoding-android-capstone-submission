package com.capstone.gometry.ui.exam

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.gometry.data.QuestionRepository
import com.capstone.gometry.data.UserRepository
import com.capstone.gometry.data.model.Question
import com.capstone.gometry.data.model.User
import com.capstone.gometry.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExamViewModel @Inject constructor(
    private val questionRepository: QuestionRepository,
    private val userRepository: UserRepository
): ViewModel() {
    private val _questions = MutableLiveData<Response<List<Question>>>()
    val questions: LiveData<Response<List<Question>>> = _questions

    private val _user = MutableLiveData<Response<User?>>()
    val user: LiveData<Response<User?>> = _user

    private val _userUpdate = MutableLiveData<Response<String>>()
    val userUpdate: LiveData<Response<String>> = _userUpdate

    fun getQuestionByLevel(levelId: String) {
        _questions.value = Response.Loading
        questionRepository.getQuestionByLevel(levelId) { _questions.value = it }
    }

    fun getCurrentUser() {
        _user.value = Response.Loading
        userRepository.getCurrentUser { _user.value = it }
    }

    fun updateCurrentUser(updatedData: Map<String, Any>) {
        _userUpdate.value = Response.Loading
        userRepository.updateCurrentUser(updatedData) { _userUpdate.value = it }
    }
}