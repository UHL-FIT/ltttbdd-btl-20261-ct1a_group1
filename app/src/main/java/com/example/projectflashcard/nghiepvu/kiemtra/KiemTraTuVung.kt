package com.example.projectflashcard.nghiepvu.kiemtra

import com.example.projectflashcard.nghiepvu.kieudulieu.TuVung

object KiemTraTuVung {
    fun kiemTraNoiDung(tu: String, nghia: String): String? {
        return when {
            tu.isEmpty() -> "Từ vựng không được để trống"
            nghia.isEmpty() -> "Nghĩa tiếng Việt không được để trống"
            else -> null
        }
    }

    fun kiemTraTrungTu(
        tu: String,
        danhSachTuVung: List<TuVung>,
        tuVungGoc: TuVung?
    ): String? {
        val biTrung = danhSachTuVung.any { tuVung ->
            tuVung.tu.trim().equals(tu, ignoreCase = true) && tuVung.id != tuVungGoc?.id
        }

        return if (biTrung) {
            "Từ này đã tồn tại trong bộ thẻ hiện tại"
        } else {
            null
        }
    }
}
