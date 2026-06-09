package com.example.projectflashcard.nghiepvu.kieudulieu

enum class CheDoGiaoDien(
    val tenHienThi: String,
    val moTa: String
) {
    HE_THONG(
        tenHienThi = "Theo hệ thống",
        moTa = "Ứng dụng tự đổi sáng/tối theo thiết bị"
    ),
    SANG(
        tenHienThi = "Sáng",
        moTa = "Giao diện nền sáng, dễ dùng khi học ban ngày"
    ),
    TOI(
        tenHienThi = "Tối",
        moTa = "Giao diện nền tối, dịu mắt khi học buổi tối"
    )
}
