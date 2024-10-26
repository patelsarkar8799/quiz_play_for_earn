package com.harsh.myapplication.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.harsh.myapplication.Models.UserData
import com.harsh.myapplication.Models.Withdrawal
import com.harsh.myapplication.databinding.FragmentWalletBinding
import java.util.Objects

class WalletFragment : Fragment() {

    private lateinit var binding: FragmentWalletBinding
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var firebaseDatabase: FirebaseDatabase
    private var user: UserData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWalletBinding.inflate(inflater, container, false)
        firebaseFirestore = FirebaseFirestore.getInstance()

        Objects.requireNonNull(FirebaseAuth.getInstance().uid).let {
            firebaseFirestore.collection("users")
                    .document().get().addOnSuccessListener {
                        documentSnapshot ->
                        user = documentSnapshot.toObject(UserData::class.java)
                        binding.currentCoins.text = user!!.coins.toString()
                    }
        }

        binding.sendRequest.setOnClickListener {
            if (user!!.coins!! >= 25000) {

                val uid = FirebaseAuth.getInstance().uid
                val payPal = binding.upiAddress.text.toString()
                val covertRate = 0.001
                val money = user!!.coins?.times(covertRate)
                val request = money?.let { it1 -> Withdrawal(uid, payPal, user!!.name, it1) }

                if (request != null) {
                    firebaseFirestore.collection("withdraws").document(uid!!)
                        .set(request).addOnSuccessListener {
                            user!!.coins = user!!.coins?.minus(25000)
                            firebaseFirestore.collection("users").document(uid)
                                .update("coins", user!!.coins).addOnSuccessListener {
                                    Toast.makeText(context, "Coins deducted and request sent Successfully.", Toast.LENGTH_SHORT).show()
                                }
                            sendMoneyToPayPal(payPal, money)
                        }
                }
            } else {
                Toast.makeText(context, "You need more coins to get withdraw.", Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }
    private fun sendMoneyToPayPal(payPal: String, money: Double) {
        Toast.makeText(context, "₹" + money + "has been sent to " + payPal, Toast.LENGTH_SHORT).show()
    }
}