package com.s1ti.cafeposmobile.ui.cashier

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.s1ti.cafeposmobile.R
import com.s1ti.cafeposmobile.data.CartItem
import com.s1ti.cafeposmobile.data.TransactionHistory
import com.s1ti.cafeposmobile.ui.components.CafeButton
import com.s1ti.cafeposmobile.ui.components.CafeCard
import com.s1ti.cafeposmobile.ui.components.CashierNavigationRail
import com.s1ti.cafeposmobile.ui.theme.*
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    windowSizeClass: WindowSizeClass,
    cartItems: List<CartItem>,
    onNavigate: (String) -> Unit,
    onTransactionComplete: (TransactionHistory) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PaymentViewModel = viewModel(factory = PaymentViewModelFactory(LocalContext.current))
) {
    val isExpanded = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded
    val isMedium = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Medium
    
    var selectedMethod by remember { mutableStateOf("Cash") }
    val paymentMethods = listOf("Cash", "Debit", "QRIS")

    var customerName by remember { mutableStateOf("") }
    var tableNumber by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    
    val totalAmount = cartItems.sumOf { it.product.price * it.quantity }
    val totalCostPrice = cartItems.sumOf { it.product.costPrice * it.quantity }

    var amountPaidText by remember { mutableStateOf("") }
    val amountPaid = amountPaidText.toDoubleOrNull() ?: 0.0
    val change = if (selectedMethod == "Cash") amountPaid - totalAmount else 0.0
    val isAmountSufficient = selectedMethod != "Cash" || amountPaid >= totalAmount

    val currencyFormatter = remember { NumberFormat.getCurrencyInstance(Locale("id", "ID")).apply { 
        maximumFractionDigits = 0 
    } }

    Row(modifier = modifier.fillMaxSize()) {
        if (isExpanded || isMedium) {
            CashierNavigationRail(currentRoute = "transaction", onNavigate = onNavigate)
        }

        Scaffold(
            modifier = Modifier.weight(1f),
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.payment)) },
                    navigationIcon = {
                        IconButton(onClick = { onNavigate("transaction") }) {
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
                    .padding(horizontal = 16.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Text(
                            text = stringResource(R.string.transaction_summary),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 8.dp),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = customerName,
                            onValueChange = { customerName = it },
                            label = { Text("Nama Pelanggan (Opsional)") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = tableNumber,
                            onValueChange = { tableNumber = it },
                            label = { Text("Nomor Meja (Kosongkan jika Take Away)") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    item {
                        Text("Daftar Pesanan", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    }

                    items(cartItems) { item ->
                        CafeCard(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(item.product.name, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                    Text("${item.quantity}x Rp${item.product.price.toInt()}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                                }
                                Text("Rp${(item.product.price * item.quantity).toInt()}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                            }
                        }
                    }

                    item {
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.surfaceVariant)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(stringResource(R.string.total), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                            Text("Rp${totalAmount.toInt()}", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(R.string.payment_method),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 8.dp),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    item {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            paymentMethods.forEach { method ->
                                PaymentMethodCard(
                                    method = method,
                                    isSelected = selectedMethod == method,
                                    onClick = { 
                                        selectedMethod = method
                                        if (method != "Cash") amountPaidText = totalAmount.toInt().toString()
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }

                    if (selectedMethod == "Cash") {
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            OutlinedTextField(
                                value = amountPaidText,
                                onValueChange = { if (it.all { char -> char.isDigit() }) amountPaidText = it },
                                label = { Text("Uang Dibayar") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                prefix = { Text("Rp") },
                                isError = !isAmountSufficient && amountPaidText.isNotEmpty()
                            )
                            if (!isAmountSufficient && amountPaidText.isNotEmpty()) {
                                Text(
                                    text = "Uang yang dibayarkan kurang",
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                                )
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Kembalian", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Text(
                                    text = if (change == 0.0 && amountPaidText.isNotEmpty() && isAmountSufficient) "Uang pas" 
                                           else "Rp${change.coerceAtLeast(0.0).toInt()}",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = if (change >= 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = note,
                            onValueChange = { note = it },
                            label = { Text("Catatan Pesanan") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                    
                    item { Spacer(modifier = Modifier.height(24.dp)) }
                }

                CafeButton(
                    text = stringResource(R.string.finish_transaction),
                    onClick = {
                        viewModel.completeTransaction(
                            cartItems = cartItems,
                            totalAmount = totalAmount,
                            totalCostPrice = totalCostPrice,
                            paymentMethod = selectedMethod,
                            amountPaid = if (selectedMethod == "Cash") amountPaid else totalAmount,
                            customerName = customerName,
                            tableNumber = tableNumber,
                            note = note,
                            onComplete = { transaction ->
                                onTransactionComplete(transaction)
                            }
                        )
                    },
                    enabled = isAmountSufficient && (selectedMethod != "Cash" || amountPaidText.isNotEmpty()),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp)
                )
            }
        }
    }
}

@Composable
fun PaymentMethodCard(
    method: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        onClick = onClick,
        modifier = modifier.height(80.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(2.dp, if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant),
        colors = CardDefaults.outlinedCardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.05f) else MaterialTheme.colorScheme.surface
        )
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (isSelected) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                }
                Text(
                    text = method,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
