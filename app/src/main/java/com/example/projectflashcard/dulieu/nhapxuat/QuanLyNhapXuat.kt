package com.example.projectflashcard.dulieu.nhapxuat

import com.example.projectflashcard.nghiepvu.khodulieu.KhoFlashcard
import com.example.projectflashcard.nghiepvu.kieudulieu.BoTheNhapXuat
import com.example.projectflashcard.nghiepvu.kieudulieu.DuLieuNhapXuat
import com.example.projectflashcard.nghiepvu.kieudulieu.TuVungNhapXuat
import kotlinx.coroutines.flow.first

data class KetQuaNhapXuat(
    val soBoThe: Int,
    val soTuVung: Int,
    val thongBao: String
)

class QuanLyNhapXuat(
    private val khoFlashcard: KhoFlashcard
) {

    suspend fun xuatDuLieuRaJson(): Result<String> = runCatching {
        val danhSachBoThe = khoFlashcard.layTatCaBoThe().first()
        val danhSachTuVung = khoFlashcard.layTatCaTuVung().first()

        val duLieuNhapXuat = DuLieuNhapXuat(
            boThe = danhSachBoThe.map { boThe ->
                BoTheNhapXuat(
                    id = boThe.id,
                    tenBoThe = boThe.tenBoThe,
                    moTa = boThe.moTa,
                    ngayTao = boThe.ngayTao
                )
            },
            tuVung = danhSachTuVung.map { tuVung ->
                TuVungNhapXuat(
                    id = tuVung.id,
                    boTheId = tuVung.boTheId,
                    tu = tuVung.tu,
                    nghia = tuVung.nghia,
                    phienAm = tuVung.phienAm,
                    viDu = tuVung.viDu,
                    trangThai = tuVung.trangThai.name,
                    canOnHomNay = tuVung.canOnHomNay,
                    ngayTao = tuVung.ngayTao
                )
            }
        )

        MauNhapXuat.taoJson(duLieuNhapXuat)
    }

    suspend fun nhapDuLieuTuJson(noiDungJson: String): Result<KetQuaNhapXuat> = runCatching {
        if (noiDungJson.isBlank()) {
            throw IllegalArgumentException("Vui lòng nhập nội dung JSON")
        }

        val duLieuNhapXuat = MauNhapXuat.docJson(noiDungJson)
        val loiKiemTra = MauNhapXuat.kiemTraDuLieu(duLieuNhapXuat)
        if (loiKiemTra != null) {
            throw IllegalArgumentException(loiKiemTra)
        }

        khoFlashcard.nhapDuLieuNhapXuat(duLieuNhapXuat)

        KetQuaNhapXuat(
            soBoThe = duLieuNhapXuat.boThe.size,
            soTuVung = duLieuNhapXuat.tuVung.size,
            thongBao = "Đã nhập ${duLieuNhapXuat.boThe.size} bộ thẻ và ${duLieuNhapXuat.tuVung.size} từ vựng"
        )
    }
}
