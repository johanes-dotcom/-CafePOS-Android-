package com.s1ti.cafeposmobile.ui.screens.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Placeholder Dashboard Kasir -- akan diisi sesuai SRS 3.1 & rancangan
 * antarmuka D.1 (Pesanan Baru, Riwayat, dll) pada increment berikutnya.
 */
@Composable
fun KasirDashboardScreen(onLogout: () -> Unit) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Dashboard Kasir", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Login berhasil. Fitur pesanan baru, pembayaran, dll menyusul pada increment berikutnya.")
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedButton(onClick = onLogout) {
                Text("Logout")
            }
        }
    }
}
