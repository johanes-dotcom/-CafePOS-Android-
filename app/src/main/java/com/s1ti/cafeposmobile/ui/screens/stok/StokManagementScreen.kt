package com.s1ti.cafeposmobile.ui.screens.stok

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.s1ti.cafeposmobile.data.local.entity.BahanBakuEntity

private sealed class StokRoute {
    object List : StokRoute()
    data class Detail(val bahan: BahanBakuEntity) : StokRoute()
    data class Form(val bahan: BahanBakuEntity?) : StokRoute() // null = tambah baru
}

/**
 * Manajemen Stok (CRUD) -- SRS 3.4, REQ-3.1 s.d REQ-3.12.
 * Pola navigasi internal sama seperti MenuManagementScreen.
 */
@Composable
fun StokManagementScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val viewModel: StokViewModel = viewModel(factory = StokViewModelFactory(context))
    var route by remember { mutableStateOf<StokRoute>(StokRoute.List) }

    when (val current = route) {
        is StokRoute.List -> StokListScreen(
            viewModel = viewModel,
            onBahanClick = { bahan -> route = StokRoute.Detail(bahan) },
            onAddClick = { route = StokRoute.Form(null) },
            modifier = modifier
        )
        is StokRoute.Detail -> StokDetailScreen(
            bahan = current.bahan,
            viewModel = viewModel,
            onBack = { route = StokRoute.List },
            onEdit = { route = StokRoute.Form(current.bahan) },
            onDeleted = { route = StokRoute.List },
            modifier = modifier
        )
        is StokRoute.Form -> StokFormScreen(
            existingBahan = current.bahan,
            viewModel = viewModel,
            onBack = { route = StokRoute.List },
            onSaved = { route = StokRoute.List },
            modifier = modifier
        )
    }
}
