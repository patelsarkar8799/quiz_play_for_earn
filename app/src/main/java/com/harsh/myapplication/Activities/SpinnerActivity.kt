package com.harsh.myapplication.Activities

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.harsh.myapplication.SpinWheel.LuckyItem
import com.harsh.myapplication.databinding.ActivitySpinnerBinding
import java.util.Objects
import java.util.Random

class SpinnerActivity : AppCompatActivity() {
    var binding: ActivitySpinnerBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySpinnerBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        val data: MutableList<LuckyItem> = ArrayList()

        val luckyItem1 = LuckyItem()
        luckyItem1.topText = "5"
        luckyItem1.secondaryText = "COINS"
        luckyItem1.textColor = Color.parseColor("#212121")
        luckyItem1.color = Color.parseColor("#eceff1")
        data.add(luckyItem1)

        val luckyItem2 = LuckyItem()
        luckyItem2.topText = "10"
        luckyItem2.secondaryText = "COINS"
        luckyItem2.color = Color.parseColor("#00cf00")
        luckyItem2.textColor = Color.parseColor("#ffffff")
        data.add(luckyItem2)

        val luckyItem3 = LuckyItem()
        luckyItem3.topText = "15"
        luckyItem3.secondaryText = "COINS"
        luckyItem3.textColor = Color.parseColor("#212121")
        luckyItem3.color = Color.parseColor("#eceff1")
        data.add(luckyItem3)

        val luckyItem4 = LuckyItem()
        luckyItem4.topText = "20"
        luckyItem4.secondaryText = "COINS"
        luckyItem4.color = Color.parseColor("#7f00d9")
        luckyItem4.textColor = Color.parseColor("#ffffff")
        data.add(luckyItem4)

        val luckyItem5 = LuckyItem()
        luckyItem5.topText = "25"
        luckyItem5.secondaryText = "COINS"
        luckyItem5.textColor = Color.parseColor("#212121")
        luckyItem5.color = Color.parseColor("#eceff1")
        data.add(luckyItem5)

        val luckyItem6 = LuckyItem()
        luckyItem6.topText = "30"
        luckyItem6.secondaryText = "COINS"
        luckyItem6.color = Color.parseColor("#dc0000")
        luckyItem6.textColor = Color.parseColor("#ffffff")
        data.add(luckyItem6)

        val luckyItem7 = LuckyItem()
        luckyItem7.topText = "35"
        luckyItem7.secondaryText = "COINS"
        luckyItem7.textColor = Color.parseColor("#212121")
        luckyItem7.color = Color.parseColor("#eceff1")
        data.add(luckyItem7)

        val luckyItem8 = LuckyItem()
        luckyItem8.topText = "0"
        luckyItem8.secondaryText = "COINS"
        luckyItem8.color = Color.parseColor("#008bff")
        luckyItem8.textColor = Color.parseColor("#ffffff")
        data.add(luckyItem8)

        binding!!.luckywheel.setData(data)
        binding!!.luckywheel.setRound(5)

        binding!!.spinBtn.setOnClickListener {
            val r = Random()
            val randomNumber = r.nextInt(8)
            binding!!.luckywheel.startLuckyWheelWithTargetIndex(randomNumber)
        }

        binding!!.luckywheel.setLuckyRoundItemSelectedListener { index ->
            updateCash(index)
        }
    }

    fun updateCash(index: Any) {
        var cash: Long = 0
        when (index) {
            0 -> cash = 5
            1 -> cash = 10
            2 -> cash = 15
            3 -> cash = 20
            4 -> cash = 25
            5 -> cash = 30
            6 -> cash = 35
            7 -> cash = 0
        }
        val database = FirebaseFirestore.getInstance()
        Objects.requireNonNull(FirebaseAuth.getInstance().uid)?.let {
            database.collection("users")
            .document(it)
            .update("coins", FieldValue.increment(cash)).addOnSuccessListener {
                Toast.makeText(
                    this@SpinnerActivity,
                    "Your coins updated in your Wallet",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }
}