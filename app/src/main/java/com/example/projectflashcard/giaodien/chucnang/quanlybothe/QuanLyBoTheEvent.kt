package com.example.projectflashcard.giaodien.chucnang.quanlybothe

import com.example.projectflashcard.nghiepvu.kieudulieu.BoThe

sealed interface QuanLyBoTheEvent {

    // --- Dữ liệu ---
    data object TaiDanhSach : QuanLyBoTheEvent
    data class DoiTuKhoa(val tuKhoa: String) : QuanLyBoTheEvent

    // --- Hộp thoại thêm / sửa ---
    data object MoHopThoaiThem : QuanLyBoTheEvent
    data class MoHopThoaiSua(val boThe: BoThe) : QuanLyBoTheEvent
    data class LuuBoThe(val tenBoThe: String, val moTa: String) : QuanLyBoTheEvent
    data object HuyHopThoai : QuanLyBoTheEvent

    // --- Xóa ---
    data class YeuCauXoa(val boThe: BoThe) : QuanLyBoTheEvent
    data object XacNhanXoa : QuanLyBoTheEvent

    // --- Điều hướng (xử lý ở NavGraph, không qua ViewModel) ---
    data object QuayLai : QuanLyBoTheEvent
    data class MoChiTiet(val boTheId: Long) : QuanLyBoTheEvent
}