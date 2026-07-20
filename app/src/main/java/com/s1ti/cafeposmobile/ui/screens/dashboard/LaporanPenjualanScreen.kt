package com.s1ti.cafeposmobile.ui.screens.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.s1ti.cafeposmobile.data.local.entity.TransactionEntity
import com.s1ti.cafeposmobile.ui.cashier.OrderDetailDialog
import com.s1ti.cafeposmobile.ui.screens.dashboard.components.DashboardSummaryCard
import com.s1ti.cafeposmobile.ui.screens.dashboard.components.EmptyStatePlaceholder
import com.s1ti.cafeposmobile.util.CurrencyFormatter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun LaporanPenjualanScreen(
    viewModel: AdminReportViewModel,
    modifier: Modifier = Modifier
) {
    val selectedPeriode by viewModel.selectedPeriode.collectAsState()
    val report by viewModel.reportData.collectAsState()
    var selectedTransaction by remember { mutableStateOf<TransactionEntity?>(null) }
    val transactionItems by viewModel.transactionItems.collectAsState()

    if (selectedTransaction != null) {
        OrderDetailDialog(
            order = selectedTransaction!!,
            items = transactionItems,
            onDismiss = { selectedTransaction = null }
        )
    }

    Column(modifier = modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = selectedPeriode.ordinal) {
            PeriodeLaporan.values().forEach { periode ->
                Tab(
                    selected = selectedPeriode == periode,
                    onClick = { viewModel.setPeriode(periode) },
                    text = { Text(periode.label) }
                )
            }
        }

        val currentReport = report
        if (currentReport == null || currentReport.isEmpty) {
            EmptyStatePlaceholder(
                text = "Belum ada transaksi pada periode ${selectedPeriode.label}."
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(20.dp)
            ) {
                item {
                    Text(
                        text = currentReport.tanggalLabel,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        DashboardSummaryCard(
                            title = "Total Penjualan",
                            value = CurrencyFormatter.formatRupiah(currentReport.totalPenjualan),
                            deltaPercent = currentReport.deltaPercent,
                            modifier = Modifier.weight(1f)
                        )
                        DashboardSummaryCard(
                            title = "Total Transaksi",
                            value = currentReport.totalTransaksi.toString(),
                            deltaPercent = currentReport.deltaPercent,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            RingkasanBaris("Total Diskon", "-" + CurrencyFormatter.formatRupiah(currentReport.totalDiskon))
                            RingkasanBaris("Total Pajak", CurrencyFormatter.formatRupiah(currentReport.totalPajak))
                            RingkasanBaris("Total Service", CurrencyFormatter.formatRupiah(currentReport.totalService))
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                            RingkasanBaris(
                                "Total Pembayaran",
                                CurrencyFormatter.formatRupiah(currentReport.totalPembayaran),
                                bold = true
                            )
                            RingkasanBaris(
                                "Total Profit (Estimasi)",
                                CurrencyFormatter.formatRupiah(currentReport.totalProfitEstimasi),
                                bold = true
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        "Metode Pembayaran",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                items(currentReport.metodePembayaran) { metode ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(metode.metode, style = MaterialTheme.typography.bodyMedium)
                        Text(
                            "${CurrencyFormatter.formatRupiah(metode.nominal)} (${metode.persentase}%)",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        "Menu Terlaris",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                items(currentReport.menuTerlaris) { menu ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(menu.nama, style = MaterialTheme.typography.bodyMedium)
                        Text("${menu.jumlahTerjual} terjual", style = MaterialTheme.typography.bodyMedium)
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        "Daftar Transaksi",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                items(currentReport.daftarTransaksi) { tx ->
                    TransactionRow(
                        transaction = tx,
                        onClick = {
                            selectedTransaction = tx
                            viewModel.loadTransactionItems(tx.id)
                        }
                    )
                }

                item { Spacer(modifier = Modifier.height(24.dp)) }
            }
        }
    }
}

@Composable
private fun TransactionRow(transaction: TransactionEntity, onClick: () -> Unit) {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(transaction.id, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                Text(sdf.format(Date(transaction.timestamp)), style = MaterialTheme.typography.bodySmall)
                Text("Kasir: ${transaction.cashierName}", style = MaterialTheme.typography.bodySmall)
            }
            Text(
                CurrencyFormatter.formatRupiah(transaction.totalAmount.toLong()),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun RingkasanBaris(label: String, value: String, bold: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Text(
            value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal
        )
    }
}
