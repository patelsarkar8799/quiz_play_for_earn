package com.harsh.myapplication.Activities

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.harsh.myapplication.Models.UserData
import com.harsh.myapplication.databinding.ActivityLoginBinding
import org.checkerframework.checker.nullness.qual.RequiresNonNull
import java.util.Objects

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var dialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")


        dialog = ProgressDialog(this)
        dialog.setMessage("We're Logging in your account...")

        if (auth.currentUser != null) {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }

        binding.loginBtn.setOnClickListener {

            val email = binding.emailBox.text.toString()
            val pass = binding.passwordBox.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                loginUser(email,pass)
            }
        }
        binding.createNewBtn.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SignupActivity::class.java))
            finish()
        }
        binding.forgotPass.setOnClickListener {
            val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }
    private fun loginUser(email:String, pass:String){
        databaseReference.orderByChild("email").equalTo(email)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (userSnapshot in dataSnapshot.children) {
                            val userData = userSnapshot.getValue(UserData::class.java)
                            if (userData != null && userData.pass == pass) {
                                dialog.show()
                                startActivity(Intent(this@LoginActivity,MainActivity::class.java))
                                dialog.dismiss()
                                finish()
                            }
                        }
                    } else {
                        Toast.makeText(this@LoginActivity,"Login Failed", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(this@LoginActivity,"Database Error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}