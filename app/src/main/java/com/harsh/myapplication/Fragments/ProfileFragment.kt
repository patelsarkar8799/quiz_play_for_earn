package com.harsh.myapplication.Fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.harsh.myapplication.databinding.FragmentProfileBinding
import java.util.Objects

class ProfileFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    var binding: FragmentProfileBinding? = null
    var LogAuth: FirebaseAuth? = null
    var database: FirebaseFirestore? = null
    var storage: FirebaseStorage? = null
    var sr: StorageReference? = null
    private var selectedImageUri: Uri? = null
    var uid: String? = FirebaseAuth.getInstance().uid
    var userUpdates: MutableMap<String, Any> = HashMap()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        database = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        sr = storage!!.reference
        LogAuth = FirebaseAuth.getInstance()

        binding!!.logoutBtn.setOnClickListener {
            LogAuth!!.signOut()
            Toast.makeText(context, "Logout Successfully", Toast.LENGTH_SHORT).show()
        }

        binding!!.profileImage.setOnClickListener { openImageChooser() }


        binding!!.profileBtn.setOnClickListener {
            if (uid != null) {
                // get data from input fields to update in Firestore

                val updateName = binding!!.name.text.toString()
                val updateEmail = binding!!.email.text.toString()
                val updateContact = binding!!.contact.text.toString()
                val updatePass = binding!!.password.text.toString()
                if (selectedImageUri != null) {
                    uploadImageToFirebase(selectedImageUri)
                }

                // create a map of fields to update
                userUpdates["name"] = updateName
                userUpdates["email"] = updateEmail
                userUpdates["contact"] = updateContact
                userUpdates["pass"] = updatePass

                //Update the Fields in firestore
                database!!.collection("users").document(Objects.requireNonNull<String>(uid))
                    .update(userUpdates)
                    .addOnSuccessListener(OnSuccessListener<Void?> {
                        Toast.makeText(
                            context,
                            "Profile Update Successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    })
                    .addOnFailureListener(OnFailureListener {
                        Toast.makeText(
                            context,
                            "Failed to update profile",
                            Toast.LENGTH_SHORT
                        ).show()
                    })
            }
        }
        // Inflate the layout for this fragment
        return binding!!.root
    }

    private fun uploadImageToFirebase(selectedImageUri: Uri?) {
        if (selectedImageUri != null) {
            val profileImageRef = storage!!.reference.child("profile_images/$uid.jpg")

            profileImageRef.putFile(selectedImageUri)
                .addOnSuccessListener {
                    profileImageRef.downloadUrl.addOnSuccessListener { uri ->
                        userUpdates["profile"] = uri.toString()
                        database!!.collection("users").document(uid!!)
                            .update(userUpdates)
                            .addOnSuccessListener {
                                Toast.makeText(
                                    context,
                                    "Profile updated Successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(
                                    context,
                                    "failed to updated profile" + e.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        context,
                        "Failed to upload image:" + e.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
        } else {
            database!!.collection("users").document(uid!!)
                .update(userUpdates)
                .addOnSuccessListener {
                    Toast.makeText(
                        context,
                        "Profile Updated sussfully!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        context,
                        "Failed to update profile" + e.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    private fun openImageChooser() {
        val intent = Intent()
        intent.setType("image/*")
        intent.setAction((Intent.ACTION_GET_CONTENT))
        startActivityForResult(Intent.createChooser(intent, "Selected Picture"), IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            selectedImageUri = data.data
            binding!!.profileImage.setImageURI(selectedImageUri)
        }
    }

    companion object {
        private const val IMAGE_REQUEST_CODE = 100
    }
}