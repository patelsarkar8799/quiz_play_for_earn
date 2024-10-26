package com.harsh.myapplication.Activities

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.harsh.myapplication.Models.Question
import com.harsh.myapplication.R
import com.harsh.myapplication.databinding.ActivityQuizBinding
import java.util.Random

class QuizActivity : AppCompatActivity() {
    var binding: ActivityQuizBinding? = null

    var questions: ArrayList<Question?>? = null
    var index: Int = 0
    var question: Question? = null
    var timer: CountDownTimer? = null
    var database: FirebaseFirestore? = null
    var correctAnswers: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        questions = ArrayList()
        database = FirebaseFirestore.getInstance()

        val catId = intent.getStringExtra("catId")

        val random = Random()
        val rand = random.nextInt(20)

        checkNotNull(catId)
        database!!.collection("categories")
            .document(catId)
            .collection("questions")
            .whereGreaterThanOrEqualTo("index", rand)
            .orderBy("index")
            .limit(5)
            .get().addOnSuccessListener { queryDocumentSnapshots ->
                if (queryDocumentSnapshots.documents.size < 5) {
                    database!!.collection("categories")
                        .document(catId)
                        .collection("questions")
                        .whereLessThanOrEqualTo("index", rand)
                        .orderBy("index")
                        .limit(5).get().addOnSuccessListener { queryDocumentSnapshots ->
                            for (snapshot in queryDocumentSnapshots) {
                                val question = snapshot.toObject(Question::class.java)
                                questions!!.add(question)
                            }
                            setNextQuestion()
                        }
                } else {
                    for (snapshot in queryDocumentSnapshots) {
                        val question = snapshot.toObject(Question::class.java)
                        questions!!.add(question)
                    }
                    setNextQuestion()
                }
            }
        resetTimer()
    }

    fun resetTimer() {
        timer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding!!.timer.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
            }
        }
    }

    fun showAnswer() {
        if (question!!.answer == binding!!.option1.text.toString()) binding!!.option1.background =
                resources.getDrawable(R.drawable.option_right)
        else if (question!!.answer == binding!!.option2.text.toString()) binding!!.option2.background =
                resources.getDrawable(R.drawable.option_right)
        else if (question!!.answer == binding!!.option3.text.toString()) binding!!.option3.background =
                resources.getDrawable(R.drawable.option_right)
        else if (question!!.answer == binding!!.option4.text.toString()) binding!!.option4.background =
                resources.getDrawable(R.drawable.option_right)
    }

    fun setNextQuestion() {
        if (timer != null) timer!!.cancel()
        checkNotNull(timer)
        timer!!.start()
        if (index < questions!!.size) {
            binding!!.questionCounter.text =
                    String.format("%d/%d", (index), questions!!.size)
            question = questions!![index]
            binding!!.question.text = question!!.question
            binding!!.option1.text = question!!.option1
            binding!!.option2.text = question!!.option2
            binding!!.option3.text = question!!.option3
            binding!!.option4.text = question!!.option4
        }
    }

    fun checkAnswer(textView: TextView) {
        val selectedAnswer = textView.text.toString()
        if (selectedAnswer == question!!.answer) {
            correctAnswers++
            textView.background = resources.getDrawable(R.drawable.option_right)
        } else {
            showAnswer()
            textView.background = resources.getDrawable(R.drawable.option_wrong)
        }
    }

    fun reset() {
        binding!!.option1.background = resources.getDrawable(R.drawable.option_unselected)
        binding!!.option2.background = resources.getDrawable(R.drawable.option_unselected)
        binding!!.option3.background = resources.getDrawable(R.drawable.option_unselected)
        binding!!.option4.background = resources.getDrawable(R.drawable.option_unselected)
    }

    fun onClick(view: View) {
        val id = view.id
        // Handle option buttons
        if (id == R.id.option1 || id == R.id.option2 || id == R.id.option3 || id == R.id.option4) {
            if (timer != null) timer!!.cancel()
            val selected = view as TextView
            checkAnswer(selected)

            // Handle next button
        } else if (id == R.id.nextBtn) {
            reset()
            if (index < questions!!.size) { // Adjust condition to avoid out of bounds error
                index++
                setNextQuestion()
            } else {
                val intent = Intent(
                    this@QuizActivity,
                    ResultActivity::class.java
                )
                intent.putExtra("correct", correctAnswers)
                intent.putExtra("total", questions!!.size)
                startActivity(intent)
            }
        }
    }
}

