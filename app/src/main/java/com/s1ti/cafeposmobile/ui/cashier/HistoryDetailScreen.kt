package com.s1ti.cafeposmobile.ui.cashier

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.s1ti.cafeposmobile.R
import com.s1ti.cafeposmobile.data.TransactionHistory
import com.s1ti.cafeposmobile.ui.components.CafeCard
import com.s1ti.cafeposmobile.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryDetailScreen(
    windowSizeClass: WindowSizeClass,
    transaction: TransactionHistory?,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (transaction == null) {
        onNavigate("history")
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.order_detail)) },
                navigationIcon = {
                    IconButton(onClick = { onNavigate("history") }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CafeCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(R.string.transaction_summary),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                DetailItemRow(stringResource(R.string.transaction_no), transaction.id)
                DetailItemRow(stringResource(R.string.date), "${transaction.date}, ${transaction.time}")
                DetailItemRow("Kasir", transaction.cashierName)
                DetailItemRow("Pelanggan", transaction.customerName.ifEmpty { "-" })
                DetailItemRow(stringResource(R.string.payment_method), transaction.method)
                DetailItemRow(stringResource(R.string.status), transaction.status)
                
                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = MaterialTheme.colorScheme.background)
                
                Text(
                    text = stringResource(R.string.orders),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                // Real items from transaction
                transaction.items.forEach { item ->
                    HistoryDetailItem(
                        name = item.product.name,
                        qty = item.quantity,
                        price = item.product.price
                    )
                }
                
                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = MaterialTheme.colorScheme.background)
                
                DetailItemRow("Subtotal", "Rp${transaction.total.toInt()}")
                DetailItemRow("Pajak", "Rp${transaction.tax.toInt()}")
                
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(stringResource(R.string.total), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                    Text("Rp${(transaction.total + transaction.tax).toInt()}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                }

                if (transaction.method == "Cash") {
                    DetailItemRow("Bayar", "Rp${transaction.amountPaid.toInt()}")
                    DetailItemRow("Kembalian", "Rp${(transaction.amountPaid - (transaction.total + transaction.tax)).toInt()}")
                }
            }
        }
    }
}

@Composable
fun DetailItemRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
        Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
fun HistoryDetailItem(name: String, qty: Int, price: Double) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(name, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
            Text("$qty x Rp${price.toInt()}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
        }
        Text("Rp${(qty * price).toInt()}", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
    }
}
