package com.example.projectflashcard.giaodien.chucnang.caidat

import com.example.projectflashcard.nghiepvu.kieudulieu.CheDoGiaoDien

sealed interface CaiDatEvent {
    data class DoiMucTieuOnTapMoiNgay(val mucTieu: Int) : CaiDatEvent
    data class DoiCheDoGiaoDien(val cheDoGiaoDien: CheDoGiaoDien) : CaiDatEvent
    data class CapNhatNoiDungJsonNhap(val noiDung: String) : CaiDatEvent
    data object XuatJson : CaiDatEvent
    data object NhapJson : CaiDatEvent
    data object XoaJsonDaXuat : CaiDatEvent
    data object XoaThongBao : CaiDatEvent
}
