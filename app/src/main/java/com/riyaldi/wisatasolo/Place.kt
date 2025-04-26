package com.riyaldi.wisatasolo

data class Place(
    val id: Int,
    val name: String,
    val location: String,
    val description: String,
    val openDays: String,
    val openTime: String,
    val ticketPrice: String,
    val imageAsset: String,
    val imageUrls: List<String>,
    val mapUrl: String,
    val rating: Double
)