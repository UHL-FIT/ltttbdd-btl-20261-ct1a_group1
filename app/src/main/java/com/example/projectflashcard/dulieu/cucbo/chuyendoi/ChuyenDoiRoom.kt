package com.example.projectflashcard.dulieu.cucbo.chuyendoi

import com.example.projectflashcard.dulieu.cucbo.bang.BangBoThe
import com.example.projectflashcard.nghiepvu.kieudulieu.BoThe

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