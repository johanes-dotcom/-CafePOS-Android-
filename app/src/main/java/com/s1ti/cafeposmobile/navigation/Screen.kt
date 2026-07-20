package com.s1ti.cafeposmobile.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object DashboardAdmin : Screen("dashboard_admin")
    object DashboardKasir : Screen("dashboard_kasir")
}
