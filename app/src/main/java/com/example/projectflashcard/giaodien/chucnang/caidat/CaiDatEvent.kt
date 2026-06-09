package com.example.projectflashcard.giaodien.chucnang.caidat

import com.example.projectflashcard.nghiepvu.kieudulieu.CheDoGiaoDien

sealed interface CaiDatEvent {
    data class DoiMucTieuOnTapMoiNgay(val mucTieu: Int) : CaiDatEvent
    data class DoiCheDoGiaoDien(val cheDoGiaoDien: CheDoGiaoDien) : CaiDatEvent
    data object XoaThongBao : CaiDatEvent
}
