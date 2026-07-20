package com.s1ti.cafeposmobile.ui.screens.stok.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

/** REQ-3.4: dialog konfirmasi sebelum menghapus bahan baku. */
@Composable
fun DeleteStokDialog(
    bahanName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Hapus Bahan Baku") },
        text = { Text("Apakah Anda yakin ingin menghapus \"$bahanName\" dari daftar stok?") },
        confirmButton = {
            TextButton(onClick = onConfirm) { Text("Hapus") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Batal") }
        }
    )
}
