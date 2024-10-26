package com.harsh.myapplication.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.harsh.myapplication.Activities.SpinnerActivity
import com.harsh.myapplication.Adaptors.CategoryAdapter
import com.harsh.myapplication.Models.CategoryModel
import com.harsh.myapplication.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private lateinit var binding: FragmentHomeBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var databaseReference: DatabaseReference
    private var categoryList = ArrayList<CategoryModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        binding = FragmentHomeBinding.inflate(inflater, container, false)
//        firebaseFirestore = FirebaseFirestore.getInstance()
//        firebaseDatabase = FirebaseDatabase.getInstance()
//
//        val categories = ArrayList<CategoryModel>()
//        val adapter = CategoryAdapter(requireContext(), categories)
//
//        firebaseFirestore.collection("categories").get()
//            .addOnSuccessListener { value ->
//                if (value != null) {
//                    for (snapshot in value.documents) {
//                        val model = snapshot.toObject(CategoryModel::class.java)
//                        if (model != null) {
//                            model.categoryId = snapshot.id
//                            categories.add(model)
//                        }
//                    }
//                }
//                adapter.notifyDataSetChanged()
//            }
//
//        binding.categoryList.layoutManager = GridLayoutManager(context, 2)
//        binding.categoryList.adapter = adapter
//
//        binding.spinwheelBtn.setOnClickListener {
//            startActivity(
//                Intent(context, SpinnerActivity::class.java))
//        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()

        val categories = ArrayList<CategoryModel>()
        val adapter = CategoryAdapter(requireContext(), categories)

        firebaseFirestore.collection("categories").get()
            .addOnSuccessListener { value ->
                if (value != null) {
                    for (snapshot in value.documents) {
                        val model = snapshot.toObject(CategoryModel::class.java)
                        if (model != null) {
                            model.categoryId = snapshot.id
                            categories.add(model)
                        }
                    }
                }
                adapter.notifyDataSetChanged()
            }

        binding.categoryList.layoutManager = GridLayoutManager(context, 2)
        binding.categoryList.adapter = adapter
    }
}