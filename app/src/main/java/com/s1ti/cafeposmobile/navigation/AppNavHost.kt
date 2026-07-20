package com.s1ti.cafeposmobile.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.windowsizeclass.WindowSizeClass

// Import model dan pengaturan
import com.s1ti.cafeposmobile.data.CartItem
import com.s1ti.cafeposmobile.data.DataRepository
import com.s1ti.cafeposmobile.data.SettingsManager
import com.s1ti.cafeposmobile.data.TransactionHistory
import com.s1ti.cafeposmobile.data.model.UserRole

// Import layar Admin & Login
import com.s1ti.cafeposmobile.ui.screens.dashboard.AdminDashboardScreen
import com.s1ti.cafeposmobile.ui.screens.login.LoginScreen

// Import semua layar Kasir
import com.s1ti.cafeposmobile.ui.cashier.*

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    windowSizeClass: WindowSizeClass,
    settingsManager: SettingsManager,
    onThemeChanged: (String) -> Unit,
    onLanguageChanged: (String) -> Unit
) {
    // Persistent Login: Tentukan start destination berdasarkan status login
    val startDestination = if (settingsManager.isLoggedIn) {
        // Jika sudah login, kita butuh role untuk menentukan dashboard mana.
        // Untuk sementara kita asumsikan KASIR jika tidak tersimpan role-nya secara explisit, 
        // atau kita arahkan ke Login jika data tidak lengkap.
        if (settingsManager.loggedInUsername.isNotEmpty()) Screen.DashboardKasir.route else Screen.Login.route
    } else {
        Screen.Login.route
    }

    var loggedInUserName by remember { mutableStateOf(settingsManager.loggedInUsername) }
    var loggedInUserRole by remember { mutableStateOf<UserRole?>(null) }

    NavHost(navController = navController, startDestination = startDestination) {

        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = { role, userName ->
                    loggedInUserName = userName
                    loggedInUserRole = role
                    val destination = when (role) {
                        UserRole.ADMIN_OWNER -> Screen.DashboardAdmin.route
                        UserRole.KASIR -> Screen.DashboardKasir.route
                    }
                    navController.navigate(destination) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.DashboardAdmin.route) {
            AdminDashboardScreen(
                ownerName = loggedInUserName,
                onLogout = {
                    settingsManager.clearSession()
                    loggedInUserName = ""
                    loggedInUserRole = null
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0)
                    }
                }
            )
        }

        composable(Screen.DashboardKasir.route) {
            var currentRoute by remember { mutableStateOf("dashboard") }
            var selectedTransactionForDetail by remember { mutableStateOf<TransactionHistory?>(null) }
            var lastTransaction by remember { mutableStateOf<TransactionHistory?>(null) }
            val cartItems = remember { mutableStateListOf<CartItem>() }

            val performLogout = {
                settingsManager.clearSession()
                loggedInUserName = ""
                loggedInUserRole = null
                navController.navigate(Screen.Login.route) {
                    popUpTo(0)
                }
            }

            when (currentRoute) {
                "dashboard" -> CashierDashboardScreen(
                    windowSizeClass = windowSizeClass,
                    onNavigate = { 
                        if (it == "logout") performLogout()
                        else currentRoute = it 
                    }
                )
                "transaction" -> TransactionScreen(
                    windowSizeClass = windowSizeClass,
                    cartItems = cartItems,
                    onNavigate = { currentRoute = it }
                )
                "payment" -> PaymentScreen(
                    windowSizeClass = windowSizeClass,
                    cartItems = cartItems,
                    onNavigate = { currentRoute = it },
                    onTransactionComplete = { transaction ->
                        lastTransaction = transaction
                        cartItems.clear()
                        currentRoute = "receipt"
                    }
                )
                "receipt" -> ReceiptScreen(
                    windowSizeClass = windowSizeClass,
                    transaction = lastTransaction,
                    onNavigate = { currentRoute = it }
                )
                "history" -> HistoryScreen(
                    windowSizeClass = windowSizeClass,
                    onNavigate = { currentRoute = it },
                    onDetail = {
                        selectedTransactionForDetail = it
                        currentRoute = "history_detail"
                    }
                )
                "history_detail" -> HistoryDetailScreen(
                    windowSizeClass = windowSizeClass,
                    transaction = selectedTransactionForDetail,
                    onNavigate = { currentRoute = it }
                )
                "orders" -> OrdersScreen(
                    windowSizeClass = windowSizeClass,
                    onNavigate = { currentRoute = it }
                )
                "profile" -> ProfileScreen(
                    windowSizeClass = windowSizeClass,
                    userName = loggedInUserName,
                    onNavigate = { 
                        if (it == "logout") performLogout()
                        else currentRoute = it 
                    }
                )
                "report" -> ReportScreen(
                    windowSizeClass = windowSizeClass,
                    onNavigate = { currentRoute = it }
                )
                "product_menu" -> ProductMenuScreen(
                    windowSizeClass = windowSizeClass,
                    onNavigate = { currentRoute = it }
                )
                "settings" -> SettingsScreen(
                    windowSizeClass = windowSizeClass,
                    onNavigate = { 
                        if (it == "logout") performLogout()
                        else currentRoute = it 
                    },
                    settingsManager = settingsManager,
                    onThemeChanged = onThemeChanged,
                    onLanguageChanged = onLanguageChanged
                )
                "about" -> AboutScreen(
                    windowSizeClass = windowSizeClass,
                    onNavigate = { currentRoute = it }
                )
                "help" -> HelpScreen(
                    windowSizeClass = windowSizeClass,
                    onNavigate = { currentRoute = it }
                )
            }
        }
    }
}
