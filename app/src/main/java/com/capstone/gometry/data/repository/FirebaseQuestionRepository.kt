package com.capstone.gometry.data.repository

import com.capstone.gometry.data.model.Question
import com.capstone.gometry.utils.Response

interface FirebaseQuestionRepository {
    fun getQuestionByGeometry(geometryId: String, response: (Response<List<Question>>) -> Unit)

    fun getQuestionByLevel(levelId: String, response: (Response<List<Question>>) -> Unit)
}