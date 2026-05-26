package com.example.projectflashcard.giaodien.chucnang.quanlybothe

import com.example.projectflashcard.nghiepvu.kieudulieu.BoThe

data class QuanLyBoTheUiState(
    val danhSach: List<BoThe> = emptyList(),
    val tuKhoaTimKiem: String = "",
    val dangTai: Boolean = true,
    val thongBaoLoi: String? = null,

    // Hộp thoại thêm / sửa
    val hienThiHopThoai: Boolean = false,
    val boTheChinhSua: BoThe? = null,   // null = đang thêm mới

    // Xác nhận xóa
    val hienThiXacNhanXoa: Boolean = false,
    val boTheSeXoa: BoThe? = null
)