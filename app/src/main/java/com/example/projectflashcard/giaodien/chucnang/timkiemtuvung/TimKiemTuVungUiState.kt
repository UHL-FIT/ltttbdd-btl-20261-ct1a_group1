package com.example.projectflashcard.giaodien.chucnang.timkiemtuvung

data class TimKiemTuVungUiState(
    val tuKhoa: String = "",
    val ketQua: List<KetQuaTimKiemTuVung> = emptyList(),
    val dangTai: Boolean = false
)
