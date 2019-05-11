package me.ricardo.romero.fersi.services

import com.google.firebase.storage.FirebaseStorage

class FireStorageService {
    private val db = FirebaseStorage.getInstance()
    fun getStorage() : FirebaseStorage{
        return db
    }
}