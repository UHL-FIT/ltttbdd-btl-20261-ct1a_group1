package com.example.projectflashcard.nghiepvu.kieudulieu

data class BoThe(
    val id: Long = 0,
    val tenBoThe: String,
    val moTa: String = "",
    val ngayTao: Long = System.currentTimeMillis()
)

