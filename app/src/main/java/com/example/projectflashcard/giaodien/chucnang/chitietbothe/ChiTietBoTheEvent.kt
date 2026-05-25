package com.example.projectflashcard.giaodien.chucnang.chitietbothe

sealed interface ChiTietBoTheEvent {
    data class ThayDoiTuKhoaTimKiem(val tuKhoa: String) : ChiTietBoTheEvent
    data class ChonBoLoc(val boLoc: BoLocTuVung) : ChiTietBoTheEvent
    data object BamThemTuVung : ChiTietBoTheEvent
    data object BamOnTap : ChiTietBoTheEvent
    data class BamSuaTuVung(val tuVung: MucTuVung) : ChiTietBoTheEvent
    data class BamXoaTuVung(val tuVung: MucTuVung) : ChiTietBoTheEvent
    data object XacNhanXoaTuVung : ChiTietBoTheEvent
    data object HuyXoaTuVung : ChiTietBoTheEvent
}
