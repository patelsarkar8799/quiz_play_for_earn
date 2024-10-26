package com.harsh.myapplication.Models

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

class Withdrawal {
    var userId: String? = null
    var emailAddress: String? = null
    var requestedBy: String? = null
    var money: Double = 0.0


    constructor()

    constructor(userId: String?, emailAddress: String?, requestedBy: String?, money: Double) {
        this.userId = userId
        this.emailAddress = emailAddress
        this.requestedBy = requestedBy
        this.money = money
    }

    @ServerTimestamp
    var createdAt: Date? = null
}
