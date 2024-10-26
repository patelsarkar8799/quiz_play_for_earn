package com.harsh.myapplication.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.auth.User
import com.harsh.myapplication.Adaptors.LeaderboardsAdapter
import com.harsh.myapplication.Models.UserData
import com.harsh.myapplication.databinding.FragmentLeaderBoardBinding

class LeaderBoardFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    var binding: FragmentLeaderBoardBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLeaderBoardBinding.inflate(inflater, container, false)

        // Inflate the layout for this fragment
        val database = FirebaseFirestore.getInstance()

        val users = ArrayList<UserData?>()
        val adapter = LeaderboardsAdapter(requireContext(), users)

        binding!!.recyclerView.adapter = adapter
        binding!!.recyclerView.layoutManager = LinearLayoutManager(context)

        database.collection("users")
            .orderBy("coins", Query.Direction.DESCENDING).get()
            .addOnSuccessListener { queryDocumentSnapshots ->
                for (snapshot in queryDocumentSnapshots) {
                    val user = snapshot.toObject(UserData::class.java)
                    users.add(user)
                }
                adapter.notifyDataSetChanged()
            }

        return binding!!.root
    }
}