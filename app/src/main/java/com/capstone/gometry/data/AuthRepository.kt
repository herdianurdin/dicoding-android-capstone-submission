package com.capstone.gometry.data

import android.content.Context
import com.capstone.gometry.data.model.User
import com.capstone.gometry.data.repository.FirebaseAuthRepository
import com.capstone.gometry.utils.Constants.REF_USERS
import com.capstone.gometry.utils.Firebase.getGoogleSignInClient
import com.capstone.gometry.utils.Response
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val database: FirebaseDatabase
): FirebaseAuthRepository {
    override fun loginWithCredential(
        credential: AuthCredential,
        response: (Response<String>) -> Unit
    ) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val currentUser = auth.currentUser
                    val reference = database.getReference(REF_USERS).child(currentUser?.uid!!)
                    reference.get()
                        .addOnSuccessListener { user ->
                            if (user.value == null) {
                                val newUser = User(
                                    id = currentUser.uid,
                                    displayName = currentUser.displayName,
                                    email = currentUser.email,
                                    photoUrl = currentUser.photoUrl.toString()
                                )
                                reference.setValue(newUser)
                            }

                            response.invoke(Response.Success("Login successfully!"))
                        }
                        .addOnFailureListener {
                            response.invoke(Response.Failure("Login failed!"))
                        }
                }
                else response.invoke(Response.Failure("Login failed!"))
            }
            .addOnFailureListener {
                response.invoke(Response.Failure("Login failed!"))
            }
    }

    override fun logout(context: Context, response: () -> Unit) {
        val googleSignInClient = getGoogleSignInClient(context)
        googleSignInClient.signOut()
        auth.signOut()
        response.invoke()
    }
}