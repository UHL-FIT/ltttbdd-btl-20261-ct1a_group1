package com.example.projectflashcard.nghiepvu.kieudulieu

data class TuVung(
    val id: Long = 0,
    val boTheId: Long,
    val tu: String,
    val nghia: String,
    val phienAm: String = "",
    val viDu: String = "",
    val trangThai: TrangThaiTuVung = TrangThaiTuVung.MOI_HOC,
    val canOnHomNay: Boolean = true,
    val ngayTao: Long = System.currentTimeMillis()
)
