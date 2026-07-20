package com.s1ti.cafeposmobile.ui.cashier

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Print
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.s1ti.cafeposmobile.R
import com.s1ti.cafeposmobile.data.BluetoothPrinter
import com.s1ti.cafeposmobile.data.PrinterManager
import com.s1ti.cafeposmobile.data.PrinterStatus
import com.s1ti.cafeposmobile.data.TransactionHistory
import com.s1ti.cafeposmobile.ui.components.CafeButton
import com.s1ti.cafeposmobile.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceiptScreen(
    windowSizeClass: WindowSizeClass,
    transaction: TransactionHistory?,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val printerManager = remember { PrinterManager(context) }
    val printerStatus by printerManager.status.collectAsState()
    val availablePrinters by printerManager.availablePrinters.collectAsState()
    val connectedPrinter by printerManager.connectedPrinter.collectAsState()
    
    var showPrinterDialog by remember { mutableStateOf(false) }

    if (transaction == null) {
        onNavigate("dashboard")
        return
    }

    if (showPrinterDialog) {
        PrinterSelectionDialog(
            availablePrinters = availablePrinters,
            status = printerStatus,
            onConnect = { printerManager.connectToPrinter(it) },
            onScan = { printerManager.startScanning() },
            onDismiss = { showPrinterDialog = false }
        )
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            Column(modifier = Modifier.padding(16.dp)) {
                CafeButton(
                    text = stringResource(R.string.done),
                    onClick = { onNavigate("dashboard") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = { 
                        if (printerStatus == PrinterStatus.CONNECTED) {
                            // Implement real printing logic with transaction data
                            printerManager.printReceipt("Struk ${transaction.id}")
                        } else {
                            printerManager.startScanning()
                            showPrinterDialog = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Print, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        if (printerStatus == PrinterStatus.CONNECTED) stringResource(R.string.print_receipt)
                        else stringResource(R.string.connect_printer),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.CheckCircle,
                contentDescription = null,
                tint = CafeSuccess,
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.payment_success),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = stringResource(R.string.thank_you),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(32.dp))

            if (connectedPrinter != null) {
                Surface(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Bluetooth, 
                            contentDescription = null, 
                            modifier = Modifier.size(16.dp), 
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${stringResource(R.string.connected)}: ${connectedPrinter?.name}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "CafePOS Mobile",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Jl. Merdeka No. 123, Jakarta",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    
                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = MaterialTheme.colorScheme.outlineVariant)
                    
                    ReceiptRow(stringResource(R.string.transaction_no), transaction.id)
                    ReceiptRow(stringResource(R.string.date), "${transaction.date} ${transaction.time}")
                    ReceiptRow(stringResource(R.string.cashier), transaction.cashierName)
                    if (transaction.customerName.isNotBlank()) {
                        ReceiptRow("Pelanggan", transaction.customerName)
                    }
                    ReceiptRow(stringResource(R.string.payment_method), transaction.method)
                    ReceiptRow(stringResource(R.string.status), transaction.status)
                    
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outlineVariant)
                    
                    Text(stringResource(R.string.orders) + ":", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    transaction.items.forEach { item ->
                        ReceiptItemRow(
                            name = item.product.name,
                            qty = item.quantity.toString(),
                            price = item.product.price.toInt().toString(),
                            subtotal = (item.product.price * item.quantity).toInt().toString()
                        )
                    }
                    
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outlineVariant)
                    
                    ReceiptSummaryRow(stringResource(R.string.subtotal), "Rp${(transaction.total - transaction.tax).toInt()}")
                    if (transaction.tax > 0) {
                        ReceiptSummaryRow(stringResource(R.string.tax), "Rp${transaction.tax.toInt()}")
                    }
                    
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(stringResource(R.string.total_bill), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        Text("Rp${transaction.total.toInt()}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, fontSize = 18.sp)
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outlineVariant)

                    ReceiptRow(stringResource(R.string.amount_paid), "Rp${transaction.amountPaid.toInt()}")
                    val change = transaction.amountPaid - transaction.total
                    ReceiptRow(stringResource(R.string.change), if (change <= 0) "Rp0" else "Rp${change.toInt()}")
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Simpan struk ini sebagai bukti pembayaran yang sah.",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun PrinterSelectionDialog(
    availablePrinters: List<BluetoothPrinter>,
    status: PrinterStatus,
    onConnect: (BluetoothPrinter) -> Unit,
    onScan: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.printer_bluetooth)) },
        text = {
            Column(modifier = Modifier.fillMaxWidth().heightIn(max = 300.dp)) {
                if (status == PrinterStatus.CONNECTING) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    Text(stringResource(R.string.connecting), modifier = Modifier.padding(top = 8.dp).align(Alignment.CenterHorizontally))
                } else {
                    if (availablePrinters.isEmpty()) {
                        Text(stringResource(R.string.no_printer_found))
                    } else {
                        LazyColumn {
                            items(availablePrinters) { printer ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onConnect(printer) }
                                        .padding(vertical = 12.dp, horizontal = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Print, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Column {
                                        Text(printer.name, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                        Text(printer.address, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                                    }
                                }
                                HorizontalDivider(color = MaterialTheme.colorScheme.background)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onScan) {
                Text(stringResource(R.string.searching_printer))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.done))
            }
        }
    )
}

@Composable
fun ReceiptSummaryRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
        Text(value, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
fun ReceiptRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
        Text(value, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
fun ReceiptItemRow(name: String, qty: String, price: String, subtotal: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(name, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
            Text("Rp$subtotal", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
        }
        Text("$qty x Rp$price", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
    }
}
