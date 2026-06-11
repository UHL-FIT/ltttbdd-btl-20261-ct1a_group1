package com.example.projectflashcard.nghiepvu.kieudulieu

data class ThongKeHocTap(
    val tongSoTu: Int = 0,
    val soTuMoiHoc: Int = 0,
    val soTuDangHoc: Int = 0,
    val soTuDaThuoc: Int = 0,
    val chuoiNgayHoc: Int = 0,
    val soPhienTuanNay: Int = 0
) {
    val tyLeGhiNho: Int
        get() = if (tongSoTu == 0) 0 else soTuDaThuoc * 100 / tongSoTu
}