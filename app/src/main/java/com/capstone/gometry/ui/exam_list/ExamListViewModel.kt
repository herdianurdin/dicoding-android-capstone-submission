package com.capstone.gometry.ui.exam_list

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.gometry.data.ExamRepository
import com.capstone.gometry.data.model.Exam
import com.capstone.gometry.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExamListViewModel @Inject constructor(
    private val repository: ExamRepository
): ViewModel() {
    private val _exams = MutableLiveData<Response<List<Exam>>>(Response.Loading)
    val exams: LiveData<Response<List<Exam>>> = _exams

    fun getExams(context: Context) {
        repository.getUserExams(context) { _exams.value = it }
    }
}