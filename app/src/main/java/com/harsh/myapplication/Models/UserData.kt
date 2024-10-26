package com.harsh.myapplication.Models

data class UserData(
    val id: String? = null,
    val name: String? = null,
    val email: String? = null,
    val pass: String? = null,
    val contact: String? = null,
    var profile: String? = null,
    var coins: Long? = 10
)
