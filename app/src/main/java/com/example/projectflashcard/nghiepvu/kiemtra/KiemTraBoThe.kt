package com.example.projectflashcard.nghiepvu.kiemtra

import com.example.projectflashcard.nghiepvu.kieudulieu.BoThe

object KiemTraBoThe {
    const val DO_DAI_TEN_TOI_DA = 50

    fun kiemTraTenBoThe(
        tenBoThe: String,
        danhSachBoThe: List<BoThe>,
        boTheChinhSua: BoThe?
    ): String? {
        val tenTrimmed = tenBoThe.trim()

        return when {
            tenTrimmed.isEmpty() -> "Tên bộ thẻ không được để trống"
            tenTrimmed.length > DO_DAI_TEN_TOI_DA ->
                "Tên bộ thẻ không được quá $DO_DAI_TEN_TOI_DA ký tự"
            danhSachBoThe.any { boThe ->
                boThe.tenBoThe.trim().equals(tenTrimmed, ignoreCase = true) &&
                    boThe.id != boTheChinhSua?.id
            } -> "Đã tồn tại bộ thẻ với tên này"
            else -> null
        }
    }
}
