package me.ricardo.romero.fersi.models

import com.google.firebase.firestore.DocumentReference

data class Message (
    val message: String,
    val product: DocumentReference
)