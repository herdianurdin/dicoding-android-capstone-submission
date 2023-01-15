package com.capstone.gometry.data

import android.content.Context
import com.capstone.gometry.data.model.Exam
import com.capstone.gometry.data.model.User
import com.capstone.gometry.data.repository.FirebaseExamRepository
import com.capstone.gometry.utils.Constants.REF_USERS
import com.capstone.gometry.utils.GenerateData.generateExams
import com.capstone.gometry.utils.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject

class ExamRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val database: FirebaseDatabase
): FirebaseExamRepository {
    override fun getUserExams(
        context: Context,
        response: (Response<List<Exam>>) -> Unit
    ) {
        val userId = auth.currentUser?.uid!!
        val reference = database.reference.child(REF_USERS).child(userId)
        reference.get()
            .addOnSuccessListener {
                var userGeometries = it.getValue(User::class.java)!!.geometries
                userGeometries = (userGeometries ?: arrayListOf()) as ArrayList<String>
                val listOfExams = generateExams(context, userGeometries)

                response.invoke(Response.Success(listOfExams))
            }
            .addOnFailureListener {
                response.invoke(Response.Failure(it.localizedMessage))
            }
    }
}