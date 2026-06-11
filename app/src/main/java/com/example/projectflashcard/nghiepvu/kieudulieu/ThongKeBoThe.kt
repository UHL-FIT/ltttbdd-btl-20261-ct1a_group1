package com.example.projectflashcard.nghiepvu.kieudulieu

data class ThongKeBoThe(
    val boTheId: Long,
    val tenBoThe: String,
    val tongSoTu: Int = 0,
    val soTuMoiHoc: Int = 0,
    val soTuDangHoc: Int = 0,
    val soTuDaThuoc: Int = 0
) {
    val tyLeGhiNho: Int
        get() = if (tongSoTu == 0) 0 else soTuDaThuoc * 100 / tongSoTu
}