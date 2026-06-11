package com.example.projectflashcard.nghiepvu.kieudulieu

data class LichSuOnTap(
    val id: Long = 0,
    val tuVungId: Long,
    val boTheId: Long,
    val mucDoOnTap: MucDoOnTap,
    val ngayOn: Long = System.currentTimeMillis()
)