package com.capstone.gometry.data

import com.capstone.gometry.data.model.User
import com.capstone.gometry.data.repository.FirebaseUserRepository
import com.capstone.gometry.utils.Constants.REF_USERS
import com.capstone.gometry.utils.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val database: FirebaseDatabase
): FirebaseUserRepository {
    override fun addUser(user: User, response: (Response<Pair<User, String>>) -> Unit) {
        val reference = database.reference.child(REF_USERS).push()
        reference.setValue(user)
            .addOnSuccessListener {
                response.invoke(
                    Response.Success(Pair(user, "User has been created successfully!"))
                )
            }
            .addOnFailureListener {
                response.invoke(
                    Response.Failure(it.localizedMessage)
                )
            }
    }

    override fun getUsers(response: (Response<List<User>>) -> Unit) {
        val reference = database.reference.child(REF_USERS)
        reference.get()
            .addOnSuccessListener { snapshot ->
                val listOfUsers = ArrayList<User>()
                for (data in snapshot.children) {
                    val user = data.getValue(User::class.java)!!
                    listOfUsers.add(user)
                }
                listOfUsers.sortByDescending { it.point }

                response.invoke(Response.Success(listOfUsers))
            }
            .addOnFailureListener {
                response.invoke(Response.Failure(it.localizedMessage))
            }
    }

    override fun getCurrentUser(response: (Response<User?>) -> Unit) {
        val userId = auth.currentUser?.uid!!
        val reference = database.reference.child(REF_USERS).child(userId)
        reference.get()
            .addOnSuccessListener {
                val user = it.getValue(User::class.java)
                response.invoke(Response.Success(user))
            }
            .addOnFailureListener {
                response.invoke(Response.Failure(it.localizedMessage))
            }
    }

    override fun updateCurrentUser(
        updatedData: Map<String, Any>,
        response: (Response<String>) -> Unit
    ) {
        val userId = auth.currentUser?.uid!!
        val reference = database.getReference(REF_USERS).child(userId)
        reference
            .updateChildren(updatedData)
            .addOnSuccessListener {
                response.invoke(Response.Success("Data updated successfully!"))
            }
            .addOnFailureListener {
                response.invoke(Response.Failure(it.localizedMessage))
            }
    }

}