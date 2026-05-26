package com.example.projectflashcard.giaodien.chucnang.themsuatuvung

data class ThemSuaTuVungUiState(
    val boTheId: Int = 1,
    val tu: String = "",
    val nghia: String = "",
    val phienAm: String = "",
    val viDu: String = "",
    val thongBaoLoi: String? = null,
    val dangLuu: Boolean = false,
    val daLuu: Boolean = false
)
