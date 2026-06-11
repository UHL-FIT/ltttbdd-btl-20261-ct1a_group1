package com.example.projectflashcard.dulieu.cucbo.chuyendoi

import com.example.projectflashcard.dulieu.cucbo.bang.BangBoThe
import com.example.projectflashcard.dulieu.cucbo.bang.BangLichSuOnTap
import com.example.projectflashcard.dulieu.cucbo.bang.BangTuVung
import com.example.projectflashcard.nghiepvu.kieudulieu.BoThe
import com.example.projectflashcard.nghiepvu.kieudulieu.LichSuOnTap
import com.example.projectflashcard.nghiepvu.kieudulieu.MucDoOnTap
import com.example.projectflashcard.nghiepvu.kieudulieu.TrangThaiTuVung
import com.example.projectflashcard.nghiepvu.kieudulieu.TuVung

fun BangBoThe.thanhBoThe(): BoThe = BoThe(
    id = id,
    tenBoThe = tenBoThe,
    moTa = moTa,
    ngayTao = ngayTao
)

fun BoThe.thanhBangBoThe(): BangBoThe = BangBoThe(
    id = id,
    tenBoThe = tenBoThe,
    moTa = moTa,
    ngayTao = ngayTao
)

fun BangTuVung.thanhTuVung(): TuVung = TuVung(
    id = id,
    boTheId = boTheId,
    tu = tu,
    nghia = nghia,
    phienAm = phienAm,
    viDu = viDu,
    trangThai = runCatching { TrangThaiTuVung.valueOf(trangThai) }
        .getOrDefault(TrangThaiTuVung.MOI_HOC),
    canOnHomNay = canOnHomNay,
    ngayTao = ngayTao
)

fun TuVung.thanhBangTuVung(): BangTuVung = BangTuVung(
    id = id,
    boTheId = boTheId,
    tu = tu,
    nghia = nghia,
    phienAm = phienAm,
    viDu = viDu,
    trangThai = trangThai.name,
    canOnHomNay = canOnHomNay,
    ngayTao = ngayTao
)

fun BangLichSuOnTap.thanhLichSuOnTap(): LichSuOnTap = LichSuOnTap(
    id = id,
    tuVungId = tuVungId,
    boTheId = boTheId,
    mucDoOnTap = runCatching { MucDoOnTap.valueOf(mucDoOnTap) }
        .getOrDefault(MucDoOnTap.KHO_NHO),
    ngayOn = ngayOn
)

fun LichSuOnTap.thanhBangLichSuOnTap(): BangLichSuOnTap = BangLichSuOnTap(
    id = id,
    tuVungId = tuVungId,
    boTheId = boTheId,
    mucDoOnTap = mucDoOnTap.name,
    ngayOn = ngayOn
)
