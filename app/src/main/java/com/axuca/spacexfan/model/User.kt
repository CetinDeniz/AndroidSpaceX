package com.axuca.spacexfan.model

/** Firebase Database - User Schema */
data class User(
    val email: String,
    val favorites: List<String>
)