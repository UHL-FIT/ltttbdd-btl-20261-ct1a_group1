package com.example.projectflashcard.giaodien.chucnang.chitietbothe



//cấu trúc khá giống struct dùng để lưu thông tin
data class ChiTietBoTheUiState(
    val boTheId: Int,
    val tenBoThe: String,
    val tuKhoaTimKiem: String = "",
    val boLocDangChon: BoLocTuVung = BoLocTuVung.TAT_CA,
    val danhSachTuVung: List<MucTuVung> = emptyList(),
    val danhSachHienThi: List<MucTuVung> = emptyList(),
    val tuVungDangXoa: MucTuVung? = null,
    val dangTai: Boolean = false
)
