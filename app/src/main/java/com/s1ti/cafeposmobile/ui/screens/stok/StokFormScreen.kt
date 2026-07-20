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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.s1ti.cafeposmobile.data.local.entity.BahanBakuEntity
import com.s1ti.cafeposmobile.data.repository.StokRepository

/**
 * Form Tambah/Update Bahan Baku -- REQ-3.2 (tambah), REQ-3.3 & REQ-3.9 (ubah
 * jumlah stok & batas minimum), REQ-3.11/REQ-3.12 (notifikasi sukses/gagal).
 */
@Composable
fun StokFormScreen(
    existingBahan: BahanBakuEntity?,
    viewModel: StokViewModel,
    onBack: () -> Unit,
    onSaved: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isEditMode = existingBahan != null

    var nama by remember { mutableStateOf(existingBahan?.nama.orEmpty()) }
    var satuan by remember { mutableStateOf(existingBahan?.satuan.orEmpty()) }
    var stokSaatIni by remember { mutableStateOf(existingBahan?.stokSaatIni?.let { formatInput(it) }.orEmpty()) }
    var stokMinimal by remember { mutableStateOf(existingBahan?.stokMinimal?.let { formatInput(it) }.orEmpty()) }
    var hargaBeli by remember { mutableStateOf(existingBahan?.hargaBeli?.toString().orEmpty()) }
    var supplier by remember { mutableStateOf(existingBahan?.supplier.orEmpty()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val saveResult by viewModel.saveResult.collectAsState()

    LaunchedEffect(saveResult) {
        when (val result = saveResult) {
            is StokRepository.SaveResult.Success -> {
                // REQ-3.11
                val message = if (isEditMode) "Stok berhasil diperbarui" else "Bahan baku berhasil ditambahkan"
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                viewModel.consumeSaveResult()
                onSaved()
            }
            is StokRepository.SaveResult.ValidationError -> {
                errorMessage = result.message
                viewModel.consumeSaveResult()
            }
            is StokRepository.SaveResult.Failure -> {
                // REQ-3.12
                errorMessage = result.message
                viewModel.consumeSaveResult()
            }
            null -> Unit
        }
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
            Text(
                if (isEditMode) "Update Bahan Baku" else "Tambah Bahan Baku",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        // Konten form bisa di-scroll -- biar tombol Simpan gak pernah kepotong
        // ke luar layar (pelajaran dari bug di MenuFormScreen sebelumnya).
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            OutlinedTextField(
                value = nama,
                onValueChange = { nama = it; errorMessage = null },
                label = { Text("Nama Bahan Baku") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = satuan,
                onValueChange = { satuan = it; errorMessage = null },
                label = { Text("Satuan") },
                placeholder = { Text("contoh: kg, liter, pcs") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = stokSaatIni,
                    onValueChange = { stokSaatIni = it.filterNumeric(); errorMessage = null },
                    label = { Text("Stok Saat Ini") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = stokMinimal,
                    onValueChange = { stokMinimal = it.filterNumeric(); errorMessage = null },
                    label = { Text("Stok Minimal") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = hargaBeli,
                onValueChange = { hargaBeli = it.filter { c -> c.isDigit() } },
                label = { Text("Harga Beli (Opsional)") },
                prefix = { Text("Rp") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = supplier,
                onValueChange = { supplier = it },
                label = { Text("Supplier (Opsional)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = errorMessage.orEmpty(),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    viewModel.saveBahanBaku(
                        id = existingBahan?.id,
                        nama = nama,
                        satuan = satuan,
                        stokSaatIni = stokSaatIni,
                        stokMinimal = stokMinimal,
                        hargaBeli = hargaBeli,
                        supplier = supplier
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(if (isEditMode) "Simpan Perubahan" else "Simpan Bahan Baku")
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

private fun String.filterNumeric(): String = filterIndexed { index, c ->
    c.isDigit() || (c == '.' && !this.take(index).contains('.'))
}

private fun formatInput(value: Double): String {
    return if (value == value.toLong().toDouble()) value.toLong().toString() else value.toString()
}
