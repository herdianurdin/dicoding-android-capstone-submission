package com.capstone.gometry.data

import android.content.Context
import com.capstone.gometry.data.model.Geometry
import com.capstone.gometry.data.model.User
import com.capstone.gometry.data.repository.FirebaseGeometryRepository
import com.capstone.gometry.utils.Constants.REF_USERS
import com.capstone.gometry.utils.GenerateData.generateGeometries
import com.capstone.gometry.utils.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject

class GeometryRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val database: FirebaseDatabase
): FirebaseGeometryRepository {
    override fun getUserGeometries(
        context: Context,
        response: (Response<List<Geometry>>) -> Unit
    ) {
        val userId = auth.currentUser?.uid!!
        val reference = database.reference.child(REF_USERS).child(userId)
        reference.get()
            .addOnSuccessListener {
                val user = it.getValue(User::class.java)!!
                val listOfGeometry: List<Geometry> = generateGeometries(context, user.geometries)
                response.invoke(Response.Success(listOfGeometry))
            }
            .addOnFailureListener {
                response.invoke(Response.Failure(it.localizedMessage))
            }
    }
}