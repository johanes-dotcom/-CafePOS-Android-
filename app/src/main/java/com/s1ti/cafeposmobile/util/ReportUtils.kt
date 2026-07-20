package com.s1ti.cafeposmobile.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.CancellationSignal
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.print.PrintManager
import android.widget.Toast
import com.s1ti.cafeposmobile.data.TransactionHistory
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

object ReportUtils {

    private val localeID = Locale("id", "ID")

    fun getPeriodDateRangeText(filter: String): String {
        val today = Calendar.getInstance()
        val sdf = SimpleDateFormat("dd MMMM yyyy", localeID)
        
        return when (filter) {
            "Hari Ini" -> sdf.format(today.time)
            "Kemarin" -> {
                val yesterday = today.clone() as Calendar
                yesterday.add(Calendar.DATE, -1)
                sdf.format(yesterday.time)
            }
            "Minggu Ini" -> {
                val start = today.clone() as Calendar
                start.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
                val end = start.clone() as Calendar
                end.add(Calendar.DATE, 6)
                "${sdf.format(start.time)} - ${sdf.format(end.time)}"
            }
            "Bulan Ini" -> {
                val start = today.clone() as Calendar
                start.set(Calendar.DAY_OF_MONTH, 1)
                val end = today.clone() as Calendar
                end.set(Calendar.DAY_OF_MONTH, end.getActualMaximum(Calendar.DAY_OF_MONTH))
                "${sdf.format(start.time)} - ${sdf.format(end.time)}"
            }
            "Tahun Ini" -> {
                val start = today.clone() as Calendar
                start.set(Calendar.DAY_OF_YEAR, 1)
                val end = today.clone() as Calendar
                end.set(Calendar.DAY_OF_YEAR, end.getActualMaximum(Calendar.DAY_OF_YEAR))
                "${sdf.format(start.time)} - ${sdf.format(end.time)}"
            }
            else -> "Seluruh Periode"
        }
    }

    fun generatePdfReport(
        context: Context,
        transactions: List<TransactionHistory>,
        periodLabel: String,
        periodRange: String,
        isPrinting: Boolean = false
    ) {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas: Canvas = page.canvas
        val paint = Paint()
        
        var yPos = 50f
        
        // Logo & Header
        paint.color = Color.parseColor("#6F4E37") // Brown Cafe Theme
        canvas.drawRect(40f, 40f, 555f, 110f, paint)
        paint.color = Color.WHITE
        paint.textSize = 28f
        paint.isFakeBoldText = true
        canvas.drawText("CafePOS Mobile", 60f, 85f, paint)
        
        paint.color = Color.BLACK
        yPos = 140f
        paint.textSize = 18f
        canvas.drawText("LAPORAN PENJUALAN LENGKAP", 40f, yPos, paint)
        yPos += 25f
        
        paint.textSize = 12f
        paint.isFakeBoldText = false
        canvas.drawText("Periode: $periodLabel ($periodRange)", 40f, yPos, paint)
        yPos += 20f
        canvas.drawText("Tanggal Cetak: ${SimpleDateFormat("dd/MM/yyyy HH:mm", localeID).format(Date())}", 40f, yPos, paint)
        yPos += 40f
        
        // Stats
        val revenue = transactions.sumOf { it.total }
        val profit = transactions.sumOf { it.total - it.totalCostPrice }
        val tax = revenue * 0.10
        val netProfit = profit - tax
        
        paint.isFakeBoldText = true
        canvas.drawText("RINGKASAN KEUANGAN", 40f, yPos, paint)
        yPos += 25f
        paint.isFakeBoldText = false
        drawRow(canvas, paint, "Total Penjualan", "Rp ${String.format(localeID, "%,.0f", revenue)}", 40f, yPos)
        yPos += 20f
        drawRow(canvas, paint, "Total Transaksi", transactions.size.toString(), 40f, yPos)
        yPos += 20f
        drawRow(canvas, paint, "Laba Bersih (setelah pajak 10%)", "Rp ${String.format(localeID, "%,.0f", netProfit)}", 40f, yPos)
        yPos += 40f
        
        // Top Products
        paint.isFakeBoldText = true
        canvas.drawText("PRODUK TERLARIS", 40f, yPos, paint)
        yPos += 25f
        paint.isFakeBoldText = false
        val topProducts = transactions.flatMap { it.items }.groupBy { it.product.name }
            .mapValues { it.value.sumOf { item -> item.quantity } }
            .toList().sortedByDescending { it.second }.take(5)
            
        if (topProducts.isEmpty()) {
            canvas.drawText("Tidak ada data produk.", 60f, yPos, paint)
            yPos += 18f
        } else {
            topProducts.forEach { (name, qty) ->
                canvas.drawText("• $name: $qty terjual", 60f, yPos, paint)
                yPos += 18f
            }
        }
        yPos += 40f
        
        // Transaction Table
        paint.isFakeBoldText = true
        canvas.drawText("RIWAYAT TRANSAKSI (25 Terakhir)", 40f, yPos, paint)
        yPos += 25f
        paint.textSize = 10f
        canvas.drawText("Invoice ID", 40f, yPos, paint)
        canvas.drawText("Waktu", 160f, yPos, paint)
        canvas.drawText("Kasir", 300f, yPos, paint)
        canvas.drawText("Metode", 420f, yPos, paint)
        canvas.drawText("Total", 500f, yPos, paint)
        yPos += 5f
        canvas.drawLine(40f, yPos, 555f, yPos, paint)
        yPos += 15f
        
        paint.isFakeBoldText = false
        transactions.reversed().take(25).forEach { tx ->
            if (yPos > 780f) return@forEach
            canvas.drawText(tx.id.take(10), 40f, yPos, paint)
            canvas.drawText("${tx.date} ${tx.time}", 160f, yPos, paint)
            canvas.drawText(tx.cashierName.take(15), 300f, yPos, paint)
            canvas.drawText(tx.method, 420f, yPos, paint)
            canvas.drawText(String.format(localeID, "%,.0f", tx.total), 500f, yPos, paint)
            yPos += 15f
        }
        
        pdfDocument.finishPage(page)
        val fileName = "Laporan_${periodLabel}_${System.currentTimeMillis()}"
        if (isPrinting) printPdf(context, pdfDocument, fileName) else savePdf(context, pdfDocument, fileName)
    }

    private fun drawRow(canvas: Canvas, paint: Paint, label: String, value: String, x: Float, y: Float) {
        paint.isFakeBoldText = false
        canvas.drawText(label, x, y, paint)
        paint.isFakeBoldText = true
        canvas.drawText(": $value", x + 180f, y, paint)
    }

    private fun savePdf(context: Context, pdfDocument: PdfDocument, fileName: String) {
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "$fileName.pdf")
        try {
            pdfDocument.writeTo(FileOutputStream(file))
            Toast.makeText(context, "Laporan PDF berhasil disimpan di Documents", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) { Toast.makeText(context, "Gagal simpan PDF: ${e.message}", Toast.LENGTH_SHORT).show() }
        finally { pdfDocument.close() }
    }

    private fun printPdf(context: Context, pdfDocument: PdfDocument, jobName: String) {
        val printManager = context.getSystemService(Context.PRINT_SERVICE) as PrintManager
        val printAdapter = object : PrintDocumentAdapter() {
            override fun onLayout(oa: PrintAttributes?, na: PrintAttributes, cs: CancellationSignal?, callback: LayoutResultCallback, extras: Bundle?) {
                if (cs?.isCanceled == true) {
                    callback.onLayoutCancelled()
                    return
                }
                callback.onLayoutFinished(PrintDocumentInfo.Builder(jobName).setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT).setPageCount(1).build(), true)
            }
            override fun onWrite(pages: Array<out PageRange>?, dest: ParcelFileDescriptor, cs: CancellationSignal?, callback: WriteResultCallback) {
                try {
                    pdfDocument.writeTo(FileOutputStream(dest.fileDescriptor))
                    callback.onWriteFinished(arrayOf(PageRange.ALL_PAGES))
                } catch (e: Exception) { callback.onWriteFailed(e.message) }
                finally { pdfDocument.close() }
            }
        }
        printManager.print(jobName, printAdapter, null)
    }

    fun generateExcelReport(context: Context, transactions: List<TransactionHistory>, periodLabel: String) {
        val csv = StringBuilder("\ufeffID Transaksi,Tanggal,Waktu,Kasir,Metode,Total,Cost,Profit\n")
        transactions.forEach { tx ->
            csv.append("${tx.id},${tx.date},${tx.time},\"${tx.cashierName}\",${tx.method},${tx.total},${tx.totalCostPrice},${tx.total - tx.totalCostPrice}\n")
        }
        val fileName = "Laporan_Excel_${periodLabel}_${System.currentTimeMillis()}.csv"
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)
        try {
            FileOutputStream(file).use { it.write(csv.toString().toByteArray(Charsets.UTF_8)) }
            Toast.makeText(context, "Laporan Excel (CSV) berhasil disimpan di Documents", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) { Toast.makeText(context, "Gagal simpan Excel: ${e.message}", Toast.LENGTH_SHORT).show() }
    }
}
