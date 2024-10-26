package com.harsh.myapplication.Activities

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.harsh.myapplication.Models.UserData
import com.harsh.myapplication.databinding.ActivitySignupBinding

//import com.google.firebase.firestore.auth.User;
class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var dialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)


        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase =FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")


        dialog = ProgressDialog(this)
        dialog.setMessage("We're creating new account...")


        binding.createNewBtn.setOnClickListener {

            val email = binding.emailBox.text.toString()
            val pass = binding.passwordBox.text.toString()
            val name = binding.nameBox.text.toString()
            val contact = binding.editContact.text.toString()
            val confirmPass = binding.confirmPass.text.toString();

//            dialog.show()

            if (email.isNotEmpty() && pass.isNotEmpty() && contact.isNotEmpty() && name.isNotEmpty() && confirmPass.isNotEmpty()) {
                signupUser(email,pass,name, contact)
            } else if (!pass.equals(confirmPass)) {
                binding.passwordBox.error = "Password Not match"
            } else if (contact.length < 10) {
                binding.editContact.error = "Maximum 10 Number's Contact required"
            } else {
                Toast.makeText(this@SignupActivity,"All field Are mandatory to fill", Toast.LENGTH_SHORT).show()
            }

//            firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
//                val uid = Objects.requireNonNull(task.result.user)?.uid
//                uid?.let { it1 ->
//                    firebaseDatabase
//                        .collection("users")
//                        .document(it1)
//                        .set(user).addOnCompleteListener { task ->
//                            if (task.isSuccessful) {
//                                dialog!!.dismiss()
//                                startActivity(
//                                    Intent(
//                                        this@SignupActivity,
//                                        MainActivity::class.java
//                                    )
//                                )
//                                finish()
//                            } else {
//
//                            }
//                        }
//                }
//            }
        }


        binding.loginBtn.setOnClickListener {
            startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
            finish()
        }
    }
    private fun signupUser(email:String, pass:String, name: String, contact: String) {
        databaseReference.orderByChild("email").equalTo(email)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(datasnapshot: DataSnapshot) {
                    if (!datasnapshot.exists()) {
                        val id = databaseReference.push().key
                        val userData = UserData(id, name, email, pass, contact)
                        databaseReference.child(id!!).setValue(userData)
                        dialog.show()
                        startActivity(Intent(this@SignupActivity,MainActivity::class.java))
                        dialog.dismiss()
                        finish()
                    } else {
                        Toast.makeText(this@SignupActivity,"User Already exist", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(this@SignupActivity,"Database Error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
                }

            })
    }
}