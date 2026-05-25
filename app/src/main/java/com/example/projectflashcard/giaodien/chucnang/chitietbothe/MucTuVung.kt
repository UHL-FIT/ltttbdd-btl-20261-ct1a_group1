package com.example.projectflashcard.giaodien.chucnang.chitietbothe

import com.example.projectflashcard.nghiepvu.kieudulieu.TrangThaiTuVung

data class MucTuVung(
    val id: Long,
    val boTheId: Int,
    val tu: String,
    val nghia: String,
    val phienAm: String,
    val viDu: String,
    val trangThai: TrangThaiTuVung,
    val canOnHomNay: Boolean
)
//mỗi từ vựng Đều có id riêng thuộc id bộ thẻ khác nhau