package com.example.projectflashcard.giaodien.chucnang.timkiemtuvung

sealed interface TimKiemTuVungEvent {
    data class ThayDoiTuKhoa(val tuKhoa: String) : TimKiemTuVungEvent
}
