package com.capstone.gometry.data.repository

import android.content.Context
import com.capstone.gometry.data.model.Exam
import com.capstone.gometry.utils.Response

interface FirebaseExamRepository {
    fun getUserExams(
        context: Context,
        response: (Response<List<Exam>>) -> Unit
    )
}