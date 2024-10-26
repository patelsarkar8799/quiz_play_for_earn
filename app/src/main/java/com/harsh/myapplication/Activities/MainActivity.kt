package com.harsh.myapplication.Activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.harsh.myapplication.Fragments.HomeFragment
import com.harsh.myapplication.Fragments.LeaderBoardFragment
import com.harsh.myapplication.Fragments.ProfileFragment
import com.harsh.myapplication.Fragments.WalletFragment
import com.harsh.myapplication.R
import com.harsh.myapplication.databinding.ActivityMainBinding
import me.ibrahimsn.lib.OnItemSelectedListener

class MainActivity : AppCompatActivity() {
    var binding: ActivityMainBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        setSupportActionBar(binding!!.toolbar)

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.content, HomeFragment())
        transaction.commit()


        binding!!.bottomBar.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelect(i: Int): Boolean {
                val transaction = supportFragmentManager.beginTransaction()
                when (i) {
                    0 -> {
                        transaction.replace(R.id.content, HomeFragment())
                        transaction.commit()
                    }

                    1 -> {
                        transaction.replace(R.id.content, LeaderBoardFragment())
                        transaction.commit()
                    }

                    2 -> {
                        transaction.replace(R.id.content, WalletFragment())
                        transaction.commit()
                    }

                    3 -> {
                        transaction.replace(R.id.content, ProfileFragment())
                        transaction.commit()
                    }
                }
                return false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.wallet) {
            val walletFragment = WalletFragment()
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.content,
                    walletFragment
                ) // R.id.fragment_container is the id of your FrameLayout in the activity
                .addToBackStack(null) // Optional, allows the user to navigate back
                .commit()
            //            return true;
            Toast.makeText(this, "wallet is clicked", Toast.LENGTH_SHORT).show()
        }

        return super.onOptionsItemSelected(item)
    }
}