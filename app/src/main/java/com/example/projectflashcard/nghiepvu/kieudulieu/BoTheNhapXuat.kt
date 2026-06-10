package com.example.projectflashcard.nghiepvu.kieudulieu

data class BoTheNhapXuat(
    val id: Long,
    val tenBoThe: String,
    val moTa: String = "",
    val ngayTao: Long = System.currentTimeMillis()
)
