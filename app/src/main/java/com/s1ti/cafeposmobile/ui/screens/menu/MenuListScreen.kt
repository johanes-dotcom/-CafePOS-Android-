package com.s1ti.cafeposmobile.ui.screens.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.s1ti.cafeposmobile.data.model.MenuKategori
import com.s1ti.cafeposmobile.ui.screens.dashboard.components.EmptyStatePlaceholder
import com.s1ti.cafeposmobile.ui.screens.menu.components.MenuListItem

/** Daftar menu -- REQ-2.1, REQ-2.7 (cari), REQ-2.8 (filter kategori). */
@Composable
fun MenuListScreen(
    viewModel: MenuViewModel,
    onMenuClick: (com.s1ti.cafeposmobile.data.local.entity.MenuEntity) -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val query by viewModel.searchQuery.collectAsState()
    val selectedKategori by viewModel.selectedKategori.collectAsState()
    val menus by viewModel.filteredMenus.collectAsState()

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            // REQ-2.2
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Menu")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Text(
                text = "Manajemen Menu",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
            )

            OutlinedTextField(
                value = query,
                onValueChange = viewModel::onSearchChange,
                placeholder = { Text("Cari menu...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = selectedKategori == null,
                        onClick = { viewModel.onKategoriChange(null) },
                        label = { Text("Semua") }
                    )
                }
                items(MenuKategori.values().toList()) { kategori ->
                    FilterChip(
                        selected = selectedKategori == kategori,
                        onClick = { viewModel.onKategoriChange(kategori) },
                        label = { Text(kategori.label) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (menus.isEmpty()) {
                EmptyStatePlaceholder(text = "Belum ada menu. Tap tombol + untuk menambahkan.")
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    items(menus, key = { it.id }) { menu ->
                        MenuListItem(menu = menu, onClick = { onMenuClick(menu) })
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    item { Spacer(modifier = Modifier.height(72.dp)) } // ruang buat FAB
                }
            }
        }
    }
}
