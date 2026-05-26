package com.example.projectflashcard.giaodien.chucnang.quanlybothe

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.projectflashcard.nghiepvu.kieudulieu.BoThe

@Composable
fun MucBoThe(
    boThe: BoThe,
    onNhanVao: () -> Unit,
    onSua: () -> Unit,
    onXoa: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onNhanVao),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 8.dp, bottom = 8.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = boThe.tenBoThe,
                    style = MaterialTheme.typography.titleMedium
                )
                if (boThe.moTa.isNotBlank()) {
                    Text(
                        text = boThe.moTa,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            IconButton(onClick = onSua) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Sửa bộ thẻ",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            IconButton(onClick = onXoa) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Xóa bộ thẻ",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}