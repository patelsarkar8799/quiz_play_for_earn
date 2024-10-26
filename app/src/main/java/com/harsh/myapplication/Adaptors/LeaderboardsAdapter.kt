package com.harsh.myapplication.Adaptors

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.auth.User
import com.harsh.myapplication.Adaptors.LeaderboardsAdapter.LeaderboardViewHolder
import com.harsh.myapplication.Models.UserData
import com.harsh.myapplication.R
import com.harsh.myapplication.databinding.RowLeaderboardBinding

class LeaderboardsAdapter(var context: Context, var users: ArrayList<UserData?>) :
    RecyclerView.Adapter<LeaderboardViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderboardViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_leaderboard, parent, false)
        return LeaderboardViewHolder(view)
    }
    override fun onBindViewHolder(holder: LeaderboardViewHolder, position: Int) {
        val user = users[position]

        holder.binding.name.text = user?.name
        holder.binding.coins.text = user?.coins.toString()
        holder.binding.index.text = String.format("#%d", position + 1)

        //        Glide.with(context)
//                .load(user.getProfile())
//                .into(holder.binding.userImage);
        if (user != null) {
            if (user.profile != null && user.profile!!.isNotEmpty()) {
                Glide.with(context)
                    .load(user.profile)
                    .into(holder.binding.userImage)
            }
        }
    }
    override fun getItemCount(): Int {
        return users.size
    }
    class LeaderboardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: RowLeaderboardBinding = RowLeaderboardBinding.bind(itemView)
    }
}
