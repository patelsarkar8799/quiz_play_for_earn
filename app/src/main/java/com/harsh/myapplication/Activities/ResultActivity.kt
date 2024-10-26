package com.harsh.myapplication.Activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.harsh.myapplication.databinding.ActivityResultBinding
import java.util.Objects

class ResultActivity : AppCompatActivity() {
    var binding: ActivityResultBinding? = null
    var POINTS: Int = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        val correctAnswers = intent.getIntExtra("correct", 0)
        val totalQuestions = intent.getIntExtra("total", 0)

        val points = correctAnswers.toLong() * POINTS

        binding!!.score.text = String.format("%d/%d", correctAnswers, totalQuestions)
        binding!!.earnedCoins.text = points.toString()

        val database = FirebaseFirestore.getInstance()

        Objects.requireNonNull(FirebaseAuth.getInstance().uid)?.let {
            database.collection("users")
            .document(it)
            .update("coins", FieldValue.increment(points))
        }

        binding!!.restartBtn.setOnClickListener {
            startActivity(
                Intent(
                    this@ResultActivity,
                    MainActivity::class.java
                )
            )
            finishAffinity()
        }
    }
}