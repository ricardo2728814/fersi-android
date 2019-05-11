package me.ricardo.romero.fersi.services

import com.google.firebase.firestore.FirebaseFirestore

class FireStoreService {
    private val db = FirebaseFirestore.getInstance()

    fun getDB(): FirebaseFirestore {
        return this.db
    }
}