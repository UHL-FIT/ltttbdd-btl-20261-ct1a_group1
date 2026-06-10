package com.example.projectflashcard.giaodien.chucnang.caidat

import com.example.projectflashcard.nghiepvu.kieudulieu.CheDoGiaoDien

data class CaiDatUiState(
    val mucTieuOnTapMoiNgay: Int = 20,
    val cheDoGiaoDien: CheDoGiaoDien = CheDoGiaoDien.HE_THONG,
    val noiDungJsonNhap: String = "",
    val jsonDaXuat: String = "",
    val dangTai: Boolean = true,
    val dangLuu: Boolean = false,
    val thongBaoLoi: String? = null,
    val thongBaoThanhCong: String? = null
)
