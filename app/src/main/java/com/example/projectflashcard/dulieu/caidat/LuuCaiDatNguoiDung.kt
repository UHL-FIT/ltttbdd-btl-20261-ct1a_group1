package com.example.projectflashcard.dulieu.caidat

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.projectflashcard.nghiepvu.kieudulieu.CaiDatNguoiDung
import com.example.projectflashcard.nghiepvu.kieudulieu.CheDoGiaoDien
import java.io.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

private val Context.dataStoreCaiDat: DataStore<Preferences> by preferencesDataStore(
    name = "cai_dat_nguoi_dung"
)

class LuuCaiDatNguoiDung(private val context: Context) {

    private object KhoaCaiDat {
        val MUC_TIEU_ON_TAP_MOI_NGAY = intPreferencesKey("muc_tieu_on_tap_moi_ngay")
        val CHE_DO_GIAO_DIEN = stringPreferencesKey("che_do_giao_dien")
    }

    private val caiDatMacDinh = CaiDatNguoiDung()

    val caiDatNguoiDung: Flow<CaiDatNguoiDung> = context.dataStoreCaiDat.data
        .catch { loi ->
            if (loi is IOException) {
                emit(emptyPreferences())
            } else {
                throw loi
            }
        }
        .map { preferences ->
            CaiDatNguoiDung(
                mucTieuOnTapMoiNgay = preferences[KhoaCaiDat.MUC_TIEU_ON_TAP_MOI_NGAY]
                    ?: caiDatMacDinh.mucTieuOnTapMoiNgay,
                cheDoGiaoDien = docCheDoGiaoDien(preferences[KhoaCaiDat.CHE_DO_GIAO_DIEN])
            )
        }

    suspend fun luuMucTieuOnTapMoiNgay(mucTieu: Int) {
        context.dataStoreCaiDat.edit { preferences ->
            preferences[KhoaCaiDat.MUC_TIEU_ON_TAP_MOI_NGAY] = mucTieu
        }
    }

    suspend fun luuCheDoGiaoDien(cheDoGiaoDien: CheDoGiaoDien) {
        context.dataStoreCaiDat.edit { preferences ->
            preferences[KhoaCaiDat.CHE_DO_GIAO_DIEN] = cheDoGiaoDien.name
        }
    }

    suspend fun luuCaiDatNguoiDung(caiDatNguoiDung: CaiDatNguoiDung) {
        context.dataStoreCaiDat.edit { preferences ->
            preferences[KhoaCaiDat.MUC_TIEU_ON_TAP_MOI_NGAY] = caiDatNguoiDung.mucTieuOnTapMoiNgay
            preferences[KhoaCaiDat.CHE_DO_GIAO_DIEN] = caiDatNguoiDung.cheDoGiaoDien.name
        }
    }

    private fun docCheDoGiaoDien(giaTri: String?): CheDoGiaoDien {
        return giaTri
            ?.let { tenCheDo ->
                runCatching { CheDoGiaoDien.valueOf(tenCheDo) }.getOrNull()
            }
            ?: caiDatMacDinh.cheDoGiaoDien
    }
}
