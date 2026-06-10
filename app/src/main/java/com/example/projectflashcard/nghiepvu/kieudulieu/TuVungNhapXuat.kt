package com.example.projectflashcard.nghiepvu.kieudulieu

data class TuVungNhapXuat(
    val id: Long,
    val boTheId: Long,
    val tu: String,
    val nghia: String,
    val phienAm: String = "",
    val viDu: String = "",
    val trangThai: String = TrangThaiTuVung.MOI_HOC.name,
    val canOnHomNay: Boolean = true,
    val ngayTao: Long = System.currentTimeMillis()
)
