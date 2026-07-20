package com.s1ti.cafeposmobile.ui.screens.menu.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

/** REQ-2.4: dialog konfirmasi sebelum menghapus menu. */
@Composable
fun DeleteMenuDialog(
    menuName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Hapus Menu") },
        text = { Text("Apakah Anda yakin ingin menghapus menu \"$menuName\"?") },
        confirmButton = {
            TextButton(onClick = onConfirm) { Text("Hapus") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Batal") }
        }
    )
}
