package com.s1ti.cafeposmobile.ui.cashier

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.s1ti.cafeposmobile.R
import com.s1ti.cafeposmobile.ui.components.CafeCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpScreen(
    windowSizeClass: WindowSizeClass,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.help_center)) },
                navigationIcon = {
                    IconButton(onClick = { onNavigate("settings") }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.faq),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            FaqItem("Bagaimana cara membuat transaksi?", "Buka menu Transaksi, pilih item yang dipesan, lalu tekan Bayar.")
            FaqItem("Bagaimana cara menghubungkan printer?", "Pastikan Bluetooth aktif, buka struk pembayaran, klik Hubungkan Printer, dan pilih perangkat Anda.")
            FaqItem("Bagaimana cara mengubah bahasa?", "Buka Pengaturan, pilih menu Bahasa, dan pilih Indonesia atau English.")
            FaqItem("Bagaimana cara mengubah tema?", "Buka Pengaturan, pilih menu Tema Aplikasi, dan pilih Mode Terang atau Mode Gelap.")
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                text = "Butuh bantuan lebih lanjut?",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Hubungi admin kami jika Anda mengalami kendala teknis.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = { /* Contact Admin */ },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Hubungi Admin")
            }
        }
    }
}

@Composable
fun FaqItem(question: String, answer: String) {
    CafeCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = question,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Icon(Icons.Default.ExpandMore, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = answer,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}
