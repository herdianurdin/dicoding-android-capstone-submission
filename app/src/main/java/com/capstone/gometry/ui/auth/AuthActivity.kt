package com.capstone.gometry.ui.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.capstone.gometry.R
import com.capstone.gometry.databinding.ActivityAuthBinding
import com.capstone.gometry.ui.main.MainActivity
import com.capstone.gometry.utils.Firebase.getGoogleSignInClient
import com.capstone.gometry.utils.MessageUtility.showToast
import com.capstone.gometry.utils.Response
import com.capstone.gometry.utils.viewBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityAuthBinding::inflate)
    private val viewModel: AuthViewModel by viewModels()

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var btnSignInWithGoogle: MaterialButton

    private var resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) { showErrorOccurred() }
        else btnSignInWithGoogle.isEnabled = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

        googleSignInClient = getGoogleSignInClient(this)
        firebaseAuth = Firebase.auth

        btnSignInWithGoogle = binding.btnSignInWithGoogle
        btnSignInWithGoogle.setOnClickListener { signInWithGoogle() }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        lifecycleScope.launchWhenResumed {
            val credential = GoogleAuthProvider.getCredential(idToken, null)

            viewModel.loginWithCredential(credential)
            viewModel.loginResponse.observe(this@AuthActivity) { response ->
                when (response) {
                    is Response.Loading -> {}
                    is Response.Failure -> showErrorOccurred()
                    is Response.Success -> {
                        showToast(this@AuthActivity, getString(R.string.success_sign_in))
                        Intent(this@AuthActivity, MainActivity::class.java).also {
                            startActivity(it)
                            finish()
                        }
                    }
                }
            }

        }
    }

    private fun signInWithGoogle() {
        btnSignInWithGoogle.isEnabled = false
        val signInIntent = googleSignInClient.signInIntent
        resultLauncher.launch(signInIntent)
    }

    private fun showErrorOccurred() {
        showToast(this@AuthActivity, getString(R.string.error_sign_in))
        btnSignInWithGoogle.isEnabled = true
    }
}