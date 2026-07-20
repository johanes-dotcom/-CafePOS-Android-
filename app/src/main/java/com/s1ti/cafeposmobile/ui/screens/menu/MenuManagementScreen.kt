package com.s1ti.cafeposmobile.ui.screens.menu

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.s1ti.cafeposmobile.data.local.entity.MenuEntity

private sealed class MenuRoute {
    object List : MenuRoute()
    data class Detail(val menu: MenuEntity) : MenuRoute()
    data class Form(val menu: MenuEntity?) : MenuRoute() // null = tambah baru
}

/**
 * Manajemen Menu (CRUD) -- SRS 3.3, REQ-2.1 s.d REQ-2.12.
 * Navigasi internal (List/Detail/Form) dikelola di sini sendiri, mirip pola
 * showAnalytics di AdminDashboardScreen -- gak perlu nambah route baru ke
 * NavHost utama buat fitur yang sepenuhnya di dalam tab "Menu".
 */
@Composable
fun MenuManagementScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val viewModel: MenuViewModel = viewModel(factory = MenuViewModelFactory(context))
    var route by remember { mutableStateOf<MenuRoute>(MenuRoute.List) }

    when (val current = route) {
        is MenuRoute.List -> MenuListScreen(
            viewModel = viewModel,
            onMenuClick = { menu -> route = MenuRoute.Detail(menu) },
            onAddClick = { route = MenuRoute.Form(null) },
            modifier = modifier
        )
        is MenuRoute.Detail -> MenuDetailScreen(
            menu = current.menu,
            viewModel = viewModel,
            onBack = { route = MenuRoute.List },
            onEdit = { route = MenuRoute.Form(current.menu) },
            onDeleted = { route = MenuRoute.List },
            modifier = modifier
        )
        is MenuRoute.Form -> MenuFormScreen(
            existingMenu = current.menu,
            viewModel = viewModel,
            onBack = { route = MenuRoute.List },
            onSaved = { route = MenuRoute.List },
            modifier = modifier
        )
    }
}
