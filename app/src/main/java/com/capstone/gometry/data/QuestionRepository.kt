package com.capstone.gometry.data

import com.capstone.gometry.data.model.Question
import com.capstone.gometry.data.repository.FirebaseQuestionRepository
import com.capstone.gometry.utils.Constants.CHILD_GEOMETRY_ID
import com.capstone.gometry.utils.Constants.CHILD_LEVEL
import com.capstone.gometry.utils.Constants.LEVEL_KEY_BEGINNER
import com.capstone.gometry.utils.Constants.MAX_QUESTION
import com.capstone.gometry.utils.Constants.REF_QUESTIONS
import com.capstone.gometry.utils.Response
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject

class QuestionRepository @Inject constructor(
    private val database: FirebaseDatabase
): FirebaseQuestionRepository {
    override fun getQuestionByGeometry(
        geometryId: String,
        response: (Response<List<Question>>) -> Unit
    ) {
        val reference = database.getReference(REF_QUESTIONS)
        reference
            .orderByChild(CHILD_GEOMETRY_ID)
            .equalTo(geometryId)
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val questions = ArrayList<Question>()

                    if (snapshot.exists()) {
                        for (data in snapshot.children)
                            questions.add(data.getValue(Question::class.java)!!)
                        questions.filter { it.level == LEVEL_KEY_BEGINNER }
                        questions.shuffle()
                    }

                    response.invoke(Response.Success(questions.take(MAX_QUESTION)))
                }

                override fun onCancelled(error: DatabaseError) {
                    response.invoke(Response.Failure(error.message))
                }
            })
    }

    override fun getQuestionByLevel(levelId: String, response: (Response<List<Question>>) -> Unit) {
        val reference = database.getReference(REF_QUESTIONS)
        reference
            .orderByChild(CHILD_LEVEL)
            .equalTo(levelId)
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val questions = ArrayList<Question>()

                    if (snapshot.exists()) {
                        for (data in snapshot.children)
                            questions.add(data.getValue(Question::class.java)!!)
                        questions.shuffle()
                    }

                    response.invoke(Response.Success(questions.take(MAX_QUESTION)))
                }

                override fun onCancelled(error: DatabaseError) {
                    response.invoke(Response.Failure(error.message))
                }
            })
    }
}