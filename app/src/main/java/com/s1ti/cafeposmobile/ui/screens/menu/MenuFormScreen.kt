package com.s1ti.cafeposmobile.ui.screens.menu

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.s1ti.cafeposmobile.data.local.entity.MenuEntity
import com.s1ti.cafeposmobile.data.model.MenuKategori
import com.s1ti.cafeposmobile.data.repository.MenuRepository

/**
 * Form Tambah/Edit Menu -- REQ-2.2 (tambah), REQ-2.3 (ubah), REQ-2.5 (validasi),
 * REQ-2.11/REQ-2.12 (notifikasi sukses/gagal).
 *
 * Catatan soal foto: pakai ActivityResultContracts.GetContent() (Storage Access
 * Framework bawaan Android), jadi TIDAK perlu izin runtime tambahan. Tapi URI
 * hasil GetContent() belum di-persist (belum takePersistableUriPermission),
 * jadi ada kemungkinan gambar gagal dimuat lagi setelah app di-restart dalam
 * kondisi tertentu. Kalau ini jadi masalah pas testing, ganti ke
 * ActivityResultContracts.OpenDocument() + takePersistableUriPermission().
 */
@Composable
fun MenuFormScreen(
    existingMenu: MenuEntity?,
    viewModel: MenuViewModel,
    onBack: () -> Unit,
    onSaved: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isEditMode = existingMenu != null

    var nama by remember { mutableStateOf(existingMenu?.nama.orEmpty()) }
    var kategori by remember { mutableStateOf(existingMenu?.kategori ?: MenuKategori.MAKANAN) }
    var hargaJual by remember { mutableStateOf(existingMenu?.hargaJual?.toString().orEmpty()) }
    var hargaModal by remember { mutableStateOf(existingMenu?.hargaModal?.toString().orEmpty()) }
    var deskripsi by remember { mutableStateOf(existingMenu?.deskripsi.orEmpty()) }
    var tersedia by remember { mutableStateOf(existingMenu?.tersedia ?: true) }
    var fotoUri by remember { mutableStateOf(existingMenu?.fotoUri) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var kategoriExpanded by remember { mutableStateOf(false) }

    val saveResult by viewModel.saveResult.collectAsState()

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> if (uri != null) fotoUri = uri.toString() }

    LaunchedEffect(saveResult) {
        when (val result = saveResult) {
            is MenuRepository.SaveResult.Success -> {
                // REQ-2.11
                val message = if (isEditMode) "Menu berhasil diperbarui" else "Menu berhasil ditambahkan"
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                viewModel.consumeSaveResult()
                onSaved()
            }
            is MenuRepository.SaveResult.ValidationError -> {
                errorMessage = result.message
                viewModel.consumeSaveResult()
            }
            is MenuRepository.SaveResult.Failure -> {
                // REQ-2.12
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
                if (isEditMode) "Edit Menu" else "Tambah Menu",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            // REQ-2.2: foto menu
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable { imagePicker.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (fotoUri != null) {
                    AsyncImage(
                        model = fotoUri,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.PhotoCamera, contentDescription = null)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Upload Foto", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = nama,
                onValueChange = { nama = it; errorMessage = null },
                label = { Text("Nama Menu") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // REQ-2.2: kategori -- dropdown sederhana, gak pakai ExposedDropdownMenuBox
            // biar API-nya stabil lintas versi Material3.
            Box {
                OutlinedButton(
                    onClick = { kategoriExpanded = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(kategori.label, modifier = Modifier.weight(1f))
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }
                DropdownMenu(
                    expanded = kategoriExpanded,
                    onDismissRequest = { kategoriExpanded = false }
                ) {
                    MenuKategori.values().forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option.label) },
                            onClick = {
                                kategori = option
                                kategoriExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = hargaJual,
                onValueChange = { hargaJual = it.filter { c -> c.isDigit() }; errorMessage = null },
                label = { Text("Harga Jual") },
                prefix = { Text("Rp") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = hargaModal,
                onValueChange = { hargaModal = it.filter { c -> c.isDigit() } },
                label = { Text("Harga Modal (Opsional)") },
                prefix = { Text("Rp") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = deskripsi,
                onValueChange = { deskripsi = it },
                label = { Text("Deskripsi (Opsional)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Tersedia untuk dijual")
                Switch(checked = tersedia, onCheckedChange = { tersedia = it })
            }

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
                    viewModel.saveMenu(
                        id = existingMenu?.id,
                        nama = nama,
                        kategori = kategori,
                        hargaJual = hargaJual,
                        hargaModal = hargaModal,
                        deskripsi = deskripsi,
                        fotoUri = fotoUri,
                        tersedia = tersedia
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(if (isEditMode) "Simpan Perubahan" else "Simpan Menu")
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
