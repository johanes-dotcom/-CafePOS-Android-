package com.s1ti.cafeposmobile.ui.screens.stok.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.s1ti.cafeposmobile.data.local.entity.BahanBakuEntity
import com.s1ti.cafeposmobile.data.model.StokStatus

private fun statusColors(status: StokStatus): Pair<Color, Color> = when (status) {
    StokStatus.AMAN -> Color(0xFFDCEDC8) to Color(0xFF2E7D32)
    StokStatus.MENIPIS -> Color(0xFFFFE0B2) to Color(0xFFEF6C00)
    StokStatus.HABIS -> Color(0xFFFFCDD2) to Color(0xFFC62828)
}

private fun formatJumlah(value: Double): String {
    return if (value == value.toLong().toDouble()) {
        value.toLong().toString()
    } else {
        value.toString()
    }
}

/** Satu baris bahan baku di daftar Manajemen Stok -- REQ-3.1. */
@Composable
fun StokListItem(
    bahan: BahanBakuEntity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (containerColor, contentColor) = statusColors(bahan.statusStok)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(bahan.nama, fontWeight = FontWeight.SemiBold)
                Text(
                    "${formatJumlah(bahan.stokSaatIni)} ${bahan.satuan} (min. ${formatJumlah(bahan.stokMinimal)} ${bahan.satuan})",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Surface(color = containerColor, shape = RoundedCornerShape(6.dp)) {
                Text(
                    text = bahan.statusStok.label,
                    style = MaterialTheme.typography.labelSmall,
                    color = contentColor,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}
