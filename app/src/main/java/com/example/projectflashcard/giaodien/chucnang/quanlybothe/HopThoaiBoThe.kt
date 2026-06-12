package com.example.projectflashcard.giaodien.chucnang.quanlybothe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.projectflashcard.giaodien.chude.KichThuocUi
import com.example.projectflashcard.nghiepvu.kieudulieu.BoThe

@Composable
fun HopThoaiBoThe(
    boTheChinhSua: BoThe?,
    thongBaoLoi: String?,
    onLuu: (tenBoThe: String, moTa: String) -> Unit,
    onHuy: () -> Unit
) {
    var ten by rememberSaveable(boTheChinhSua) { mutableStateOf(boTheChinhSua?.tenBoThe ?: "") }
    var moTa by rememberSaveable(boTheChinhSua) { mutableStateOf(boTheChinhSua?.moTa ?: "") }

    AlertDialog(
        onDismissRequest = onHuy,
        title = {
            Text(
                text = if (boTheChinhSua == null) "Thêm bộ thẻ" else "Sửa bộ thẻ",
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(KichThuocUi.khoangCachVua)) {
                OutlinedTextField(
                    value = ten,
                    onValueChange = { ten = it },
                    label = { Text("Tên bộ thẻ *") },
                    singleLine = true,
                    isError = thongBaoLoi != null,
                    supportingText = if (thongBaoLoi != null) {
                        { Text(text = thongBaoLoi, color = MaterialTheme.colorScheme.error) }
                    } else {
                        null
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = moTa,
                    onValueChange = { moTa = it },
                    label = { Text("Mô tả") },
                    minLines = 2,
                    maxLines = 4,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onLuu(ten, moTa) }) {
                Text("Lưu")
            }
        },
        dismissButton = {
            TextButton(onClick = onHuy) {
                Text("Hủy")
            }
        }
    )
}
