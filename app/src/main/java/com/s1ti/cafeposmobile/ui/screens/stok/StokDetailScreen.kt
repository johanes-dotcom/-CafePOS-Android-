package com.s1ti.cafeposmobile.ui.screens.stok

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.s1ti.cafeposmobile.data.local.entity.BahanBakuEntity
import com.s1ti.cafeposmobile.data.model.StokStatus
import com.s1ti.cafeposmobile.ui.screens.stok.components.DeleteStokDialog
import com.s1ti.cafeposmobile.util.CurrencyFormatter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private fun statusColors(status: StokStatus): Pair<Color, Color> = when (status) {
    StokStatus.AMAN -> Color(0xFFDCEDC8) to Color(0xFF2E7D32)
    StokStatus.MENIPIS -> Color(0xFFFFE0B2) to Color(0xFFEF6C00)
    StokStatus.HABIS -> Color(0xFFFFCDD2) to Color(0xFFC62828)
}

private fun formatJumlah(value: Double): String {
    return if (value == value.toLong().toDouble()) value.toLong().toString() else value.toString()
}

/** Detail bahan baku -- REQ-3.1 (rincian), jalan masuk ke Edit & Hapus. */
@Composable
fun StokDetailScreen(
    bahan: BahanBakuEntity,
    viewModel: StokViewModel,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onDeleted: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showDeleteDialog by remember { mutableStateOf(false) }
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy HH:mm", Locale("in", "ID")) }
    val (containerColor, contentColor) = statusColors(bahan.statusStok)

    if (showDeleteDialog) {
        DeleteStokDialog(
            bahanName = bahan.nama,
            onConfirm = {
                viewModel.deleteBahanBaku(bahan)
                // REQ-3.11
                Toast.makeText(context, "\"${bahan.nama}\" berhasil dihapus", Toast.LENGTH_SHORT).show()
                showDeleteDialog = false
                onDeleted()
            },
            onDismiss = { showDeleteDialog = false }
        )
    }

    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
            }
            Text("Detail Bahan Baku", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(bahan.nama, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Surface(color = containerColor, shape = RoundedCornerShape(6.dp)) {
                    Text(
                        text = bahan.statusStok.label,
                        style = MaterialTheme.typography.labelSmall,
                        color = contentColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            DetailBaris("Stok Saat Ini", "${formatJumlah(bahan.stokSaatIni)} ${bahan.satuan}")
            DetailBaris("Stok Minimal", "${formatJumlah(bahan.stokMinimal)} ${bahan.satuan}")
            if (bahan.hargaBeli != null) {
                DetailBaris("Harga Beli", CurrencyFormatter.formatRupiah(bahan.hargaBeli))
            }
            if (bahan.supplier != null) {
                DetailBaris("Supplier", bahan.supplier)
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                "Diupdate ${dateFormat.format(Date(bahan.updatedAt))}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // REQ-3.3, REQ-3.9
                Button(onClick = onEdit, modifier = Modifier.weight(1f)) {
                    Text("Update Stok")
                }
                // REQ-3.4
                OutlinedButton(onClick = { showDeleteDialog = true }, modifier = Modifier.weight(1f)) {
                    Text("Hapus")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun DetailBaris(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
    }
}
