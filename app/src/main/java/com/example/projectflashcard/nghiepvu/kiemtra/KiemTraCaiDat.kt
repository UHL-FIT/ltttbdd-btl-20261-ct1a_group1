package com.example.projectflashcard.nghiepvu.kiemtra

object KiemTraCaiDat {
    const val MUC_TIEU_TOI_THIEU = 5
    const val MUC_TIEU_TOI_DA = 50
    const val BUOC_MUC_TIEU = 5

    fun lamTronTheoBuoc(mucTieu: Int): Int {
        val mucTieuTrongKhoang = mucTieu.coerceIn(MUC_TIEU_TOI_THIEU, MUC_TIEU_TOI_DA)
        return (mucTieuTrongKhoang / BUOC_MUC_TIEU) * BUOC_MUC_TIEU
    }

    fun kiemTraMucTieuOnTapMoiNgay(mucTieu: Int): String? {
        return when {
            mucTieu < MUC_TIEU_TOI_THIEU ->
                "Mục tiêu ôn tập mỗi ngày phải từ $MUC_TIEU_TOI_THIEU từ trở lên"
            mucTieu > MUC_TIEU_TOI_DA ->
                "Mục tiêu ôn tập mỗi ngày không được quá $MUC_TIEU_TOI_DA từ"
            mucTieu % BUOC_MUC_TIEU != 0 ->
                "Mục tiêu ôn tập nên chia theo bước $BUOC_MUC_TIEU từ"
            else -> null
        }
    }
}
