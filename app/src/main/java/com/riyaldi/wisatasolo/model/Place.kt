package com.riyaldi.wisatasolo.model

data class Place(
    val id: Int,
    val name: String,
    val location: String,
    val description: String,
    val imageUrls: List<String>,
    val mapUrl: String,
)