package com.example.projectflashcard.dulieu.nhapxuat

import com.example.projectflashcard.nghiepvu.kieudulieu.BoTheNhapXuat
import com.example.projectflashcard.nghiepvu.kieudulieu.DuLieuNhapXuat
import com.example.projectflashcard.nghiepvu.kieudulieu.TrangThaiTuVung
import com.example.projectflashcard.nghiepvu.kieudulieu.TuVungNhapXuat
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

object MauNhapXuat {
    private const val PHIEN_BAN_HIEN_TAI = 1
    private const val KHOA_PHIEN_BAN = "phienBan"
    private const val KHOA_NGAY_XUAT = "ngayXuat"
    private const val KHOA_BO_THE = "boThe"
    private const val KHOA_TU_VUNG = "tuVung"

    private const val KHOA_ID = "id"
    private const val KHOA_BO_THE_ID = "boTheId"
    private const val KHOA_TEN_BO_THE = "tenBoThe"
    private const val KHOA_MO_TA = "moTa"
    private const val KHOA_TU = "tu"
    private const val KHOA_NGHIA = "nghia"
    private const val KHOA_PHIEN_AM = "phienAm"
    private const val KHOA_VI_DU = "viDu"
    private const val KHOA_TRANG_THAI = "trangThai"
    private const val KHOA_CAN_ON_HOM_NAY = "canOnHomNay"
    private const val KHOA_NGAY_TAO = "ngayTao"

    fun taoJson(duLieu: DuLieuNhapXuat): String {
        val goc = JSONObject()
            .put(KHOA_PHIEN_BAN, PHIEN_BAN_HIEN_TAI)
            .put(KHOA_NGAY_XUAT, duLieu.ngayXuat)
            .put(KHOA_BO_THE, JSONArray().apply {
                duLieu.boThe.forEach { boThe -> put(boThe.thanhJson()) }
            })
            .put(KHOA_TU_VUNG, JSONArray().apply {
                duLieu.tuVung.forEach { tuVung -> put(tuVung.thanhJson()) }
            })

        return goc.toString(2)
    }

    fun docJson(noiDungJson: String): DuLieuNhapXuat {
        try {
            val goc = JSONObject(noiDungJson)
            val phienBan = goc.optInt(KHOA_PHIEN_BAN, PHIEN_BAN_HIEN_TAI)
            val ngayXuat = goc.optLong(KHOA_NGAY_XUAT, System.currentTimeMillis())
            val mangBoThe = goc.optJSONArray(KHOA_BO_THE) ?: JSONArray()
            val mangTuVung = goc.optJSONArray(KHOA_TU_VUNG) ?: JSONArray()

            return DuLieuNhapXuat(
                phienBan = phienBan,
                ngayXuat = ngayXuat,
                boThe = List(mangBoThe.length()) { index ->
                    mangBoThe.getJSONObject(index).thanhBoTheNhapXuat()
                },
                tuVung = List(mangTuVung.length()) { index ->
                    mangTuVung.getJSONObject(index).thanhTuVungNhapXuat()
                }
            )
        } catch (loi: JSONException) {
            throw IllegalArgumentException("JSON không đúng định dạng", loi)
        }
    }

    fun kiemTraDuLieu(duLieu: DuLieuNhapXuat): String? {
        if (duLieu.phienBan > PHIEN_BAN_HIEN_TAI) {
            return "Phiên bản JSON không được hỗ trợ"
        }

        val boTheIds = duLieu.boThe.map { it.id }
        if (boTheIds.distinct().size != boTheIds.size) {
            return "JSON có bộ thẻ bị trùng id"
        }

        val tapBoTheId = boTheIds.toSet()
        duLieu.boThe.forEach { boThe ->
            if (boThe.tenBoThe.isBlank()) {
                return "Tên bộ thẻ trong JSON không được bỏ trống"
            }
        }

        val capTuTrongBo = mutableSetOf<Pair<Long, String>>()
        duLieu.tuVung.forEach { tuVung ->
            if (tuVung.boTheId !in tapBoTheId) {
                return "Từ vựng '${tuVung.tu}' không thuộc bộ thẻ hợp lệ"
            }
            if (tuVung.tu.isBlank()) {
                return "Từ vựng trong JSON không được bỏ trống"
            }
            if (tuVung.nghia.isBlank()) {
                return "Nghĩa tiếng Việt trong JSON không được bỏ trống"
            }
            val trangThaiHopLe = runCatching { TrangThaiTuVung.valueOf(tuVung.trangThai) }.isSuccess
            if (!trangThaiHopLe) {
                return "Trạng thái của từ '${tuVung.tu}' không hợp lệ"
            }

            val khoaTrung = tuVung.boTheId to tuVung.tu.trim().lowercase()
            if (!capTuTrongBo.add(khoaTrung)) {
                return "Từ '${tuVung.tu}' bị trùng trong cùng một bộ thẻ"
            }
        }

        return null
    }

    private fun BoTheNhapXuat.thanhJson(): JSONObject = JSONObject()
        .put(KHOA_ID, id)
        .put(KHOA_TEN_BO_THE, tenBoThe)
        .put(KHOA_MO_TA, moTa)
        .put(KHOA_NGAY_TAO, ngayTao)

    private fun TuVungNhapXuat.thanhJson(): JSONObject = JSONObject()
        .put(KHOA_ID, id)
        .put(KHOA_BO_THE_ID, boTheId)
        .put(KHOA_TU, tu)
        .put(KHOA_NGHIA, nghia)
        .put(KHOA_PHIEN_AM, phienAm)
        .put(KHOA_VI_DU, viDu)
        .put(KHOA_TRANG_THAI, trangThai)
        .put(KHOA_CAN_ON_HOM_NAY, canOnHomNay)
        .put(KHOA_NGAY_TAO, ngayTao)

    private fun JSONObject.thanhBoTheNhapXuat(): BoTheNhapXuat = BoTheNhapXuat(
        id = optLong(KHOA_ID, 0L),
        tenBoThe = optString(KHOA_TEN_BO_THE, ""),
        moTa = optString(KHOA_MO_TA, ""),
        ngayTao = optLong(KHOA_NGAY_TAO, System.currentTimeMillis())
    )

    private fun JSONObject.thanhTuVungNhapXuat(): TuVungNhapXuat = TuVungNhapXuat(
        id = optLong(KHOA_ID, 0L),
        boTheId = optLong(KHOA_BO_THE_ID, 0L),
        tu = optString(KHOA_TU, ""),
        nghia = optString(KHOA_NGHIA, ""),
        phienAm = optString(KHOA_PHIEN_AM, ""),
        viDu = optString(KHOA_VI_DU, ""),
        trangThai = optString(KHOA_TRANG_THAI, TrangThaiTuVung.MOI_HOC.name),
        canOnHomNay = optBoolean(KHOA_CAN_ON_HOM_NAY, true),
        ngayTao = optLong(KHOA_NGAY_TAO, System.currentTimeMillis())
    )
}
