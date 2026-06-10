package com.example.projectflashcard.nghiepvu.kieudulieu

data class DuLieuNhapXuat(
    val phienBan: Int = 1,
    val ngayXuat: Long = System.currentTimeMillis(),
    val boThe: List<BoTheNhapXuat> = emptyList(),
    val tuVung: List<TuVungNhapXuat> = emptyList()
)
