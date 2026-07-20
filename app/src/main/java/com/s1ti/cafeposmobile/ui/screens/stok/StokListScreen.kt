package com.s1ti.cafeposmobile.ui.screens.stok

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.s1ti.cafeposmobile.data.local.entity.BahanBakuEntity
import com.s1ti.cafeposmobile.ui.screens.dashboard.components.EmptyStatePlaceholder
import com.s1ti.cafeposmobile.ui.screens.stok.components.StokListItem

/** Daftar bahan baku -- REQ-3.1, REQ-3.8 (cari), REQ-3.6 (banner stok menipis/habis). */
@Composable
fun StokListScreen(
    viewModel: StokViewModel,
    onBahanClick: (BahanBakuEntity) -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val query by viewModel.searchQuery.collectAsState()
    val bahanList by viewModel.filteredBahanBaku.collectAsState()
    val lowStock by viewModel.lowStockItems.collectAsState()

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            // REQ-3.2
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Bahan Baku")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Text(
                text = "Manajemen Stok",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
            )

            OutlinedTextField(
                value = query,
                onValueChange = viewModel::onSearchChange,
                placeholder = { Text("Cari bahan baku...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            )

            // REQ-3.6: notifikasi in-app kalau ada bahan yang menipis/habis
            if (lowStock.isNotEmpty()) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.WarningAmber,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Text(
                            text = "${lowStock.size} bahan baku menipis/habis, segera lakukan pengadaan.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            } else {
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (bahanList.isEmpty()) {
                EmptyStatePlaceholder(text = "Belum ada bahan baku. Tap tombol + untuk menambahkan.")
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    items(bahanList, key = { it.id }) { bahan ->
                        StokListItem(bahan = bahan, onClick = { onBahanClick(bahan) })
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    item { Spacer(modifier = Modifier.height(72.dp)) } // ruang buat FAB
                }
            }
        }
    }
}
