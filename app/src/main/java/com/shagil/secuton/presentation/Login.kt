package com.shagil.secuton.presentation

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.shagil.secuton.R
import com.shagil.secuton.databinding.ActivityLoginBinding
import com.shagil.secuton.presentation.home.HomeActivity
import com.shagil.secuton.presentation.home.routineTask.RoutineTaskViewModel
import com.shagil.secuton.util.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Login : AppCompatActivity() {
    private lateinit var binding:ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private var currentUser: FirebaseUser?=null
    private val authViewModel by viewModels<AuthViewModel>()

    private val TAG = "Login"
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Secuton)
        setContentView(R.layout.activity_login)


        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = Firebase.auth


        
        binding.signInUser.setOnClickListener {
            val email = binding.emailAddressLogin.text.toString().trim()
            val password = binding.passwordLogin.text.toString().trim()
            val rePass = binding.passwordLoginConfirm.text.toString().trim()

            binding.signInUser.visibility = View.GONE
            binding.loginProgressBar.visibility = View.VISIBLE

            if(email.isEmpty()) {
                Toast.makeText(this@Login, "E-mail cannot be empty!", Toast.LENGTH_SHORT).show()
                binding.signInUser.visibility = View.VISIBLE
                binding.loginProgressBar.visibility = View.GONE
                return@setOnClickListener
            }
            if(password!=rePass) {
                Toast.makeText(this@Login, "Passwords do not match!", Toast.LENGTH_SHORT).show()
                binding.signInUser.visibility = View.VISIBLE
                binding.loginProgressBar.visibility = View.GONE
                return@setOnClickListener
            }
            if(password.length<6) {
                Toast.makeText(this@Login, "Password should be min 6 characters long!", Toast.LENGTH_SHORT).show()
                binding.signInUser.visibility = View.VISIBLE
                binding.loginProgressBar.visibility = View.GONE
                return@setOnClickListener
            }

            signInUserWithEmailPassword(email, password)
        }

        binding.termsAndConditions.setOnClickListener{
            openTermsAndConditions()
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = firebaseAuth.currentUser
        Log.d(TAG, "onStart(): $currentUser")
        if(currentUser!=null) {
            if(currentUser!!.isEmailVerified) {
                startActivity(Intent(this@Login, HomeActivity::class.java))
                finish()
            } else {
                Toast.makeText(this@Login, "Please check your email (even your spam folder) for verification mail!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun createUserAndVerifyEmail(email:String, password:String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task->
                if(task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    if(user!=null) {
                        user.sendEmailVerification()
                            .addOnCompleteListener { emailTask ->
                                if (emailTask.isSuccessful) {
                                    firebaseAuth.uid?.let { authViewModel.initRoutineTask(it) }
                                    Toast.makeText(this@Login, "Please check your e-mail (even your Spam/Archive folder) for verification email!", Toast.LENGTH_LONG).show()
                                }
                            }
                    } else {
                        Toast.makeText(this@Login, "Please try again!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@Login, "Unable to create user, please try again!", Toast.LENGTH_SHORT).show()
                }
                binding.signInUser.visibility = View.VISIBLE
                binding.loginProgressBar.visibility = View.GONE
            }
    }

    private fun signInUserWithEmailPassword(email:String, password:String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task->
                if(task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    if (user != null) {
                        updateUI(user)
                    } else {
                        binding.signInUser.visibility = View.VISIBLE
                        binding.loginProgressBar.visibility = View.GONE
                        Toast.makeText(this@Login, "Please try again!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    /*Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()*/
                    createUserAndVerifyEmail(email, password)
                }
                binding.signInUser.visibility = View.VISIBLE
                binding.loginProgressBar.visibility = View.GONE
            }
    }

    private fun openTermsAndConditions() {
        val openUrl = Intent(Intent.ACTION_VIEW)
        openUrl.data = Uri.parse("https://docs.google.com/document/d/1yrMjOxLh_9YPAZKNiC-r7P4Pt0GlW-i3V9MNfEjuggQ/edit?usp=sharing")
        startActivity(openUrl)
    }

    private fun updateUI(firebaseUser: FirebaseUser) {
        if(firebaseUser.isEmailVerified) {
            binding.loginProgressBar.visibility = View.GONE
            startActivity(Intent(this@Login, HomeActivity::class.java))
            finish()
        } else {
            Toast.makeText(this@Login, "Please check your email (even your spam/archive folders) for verification link before signing-in!", Toast.LENGTH_LONG).show()
        }
    }
}