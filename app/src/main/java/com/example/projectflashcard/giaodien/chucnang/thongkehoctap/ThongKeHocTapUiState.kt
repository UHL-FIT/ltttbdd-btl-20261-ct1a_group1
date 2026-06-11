package com.example.projectflashcard.giaodien.chucnang.thongkehoctap

import com.example.projectflashcard.nghiepvu.kieudulieu.ThongKeBoThe

data class ThongKeHocTapUiState(
    val dangTai: Boolean = true,
    val tongSoBoThe: Int = 0,
    val tongSoTu: Int = 0,
    val soTuDaThuoc: Int = 0,
    val soTuChuaThuoc: Int = 0,
    val soTuCanOnHomNay: Int = 0,
    val tongLuotOn: Int = 0,
    val tyLeGhiNho: Int = 0,
    val thongKeTheoBoThe: List<ThongKeBoThe> = emptyList()
)
