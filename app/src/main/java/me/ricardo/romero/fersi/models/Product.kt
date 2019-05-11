package me.ricardo.romero.fersi.models

data class Product(
    val id: String,
    val name: String,
    val images: List<String>,
    val price: Double,
    val category: String
)