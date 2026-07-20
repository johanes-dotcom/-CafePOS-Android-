package com.s1ti.cafeposmobile.data

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

enum class PrinterStatus {
    DISCONNECTED, CONNECTING, CONNECTED
}

data class BluetoothPrinter(
    val name: String,
    val address: String
)

class PrinterManager(private val context: Context) {
    private val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
    
    private val _status = MutableStateFlow(PrinterStatus.DISCONNECTED)
    val status: StateFlow<PrinterStatus> = _status

    private val _availablePrinters = MutableStateFlow<List<BluetoothPrinter>>(emptyList())
    val availablePrinters: StateFlow<List<BluetoothPrinter>> = _availablePrinters

    private val _connectedPrinter = MutableStateFlow<BluetoothPrinter?>(null)
    val connectedPrinter: StateFlow<BluetoothPrinter?> = _connectedPrinter

    private val prefs = context.getSharedPreferences("printer_prefs", Context.MODE_PRIVATE)

    init {
        val lastAddress = prefs.getString("last_printer_address", null)
        val lastName = prefs.getString("last_printer_name", null)
        if (lastAddress != null && lastName != null) {
            _connectedPrinter.value = BluetoothPrinter(lastName, lastAddress)
            _status.value = PrinterStatus.CONNECTED
        }
    }

    @SuppressLint("MissingPermission")
    fun startScanning() {
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled) return
        
        val pairedDevices = bluetoothAdapter.bondedDevices
        val printers = pairedDevices.map { 
            BluetoothPrinter(it.name ?: "Unknown Printer", it.address)
        }
        _availablePrinters.value = printers
    }

    fun connectToPrinter(printer: BluetoothPrinter) {
        _status.value = PrinterStatus.CONNECTING
        
        // Simulation of connection
        _connectedPrinter.value = printer
        _status.value = PrinterStatus.CONNECTED
        
        prefs.edit().apply {
            putString("last_printer_address", printer.address)
            putString("last_printer_name", printer.name)
            apply()
        }
    }

    fun disconnect() {
        _status.value = PrinterStatus.DISCONNECTED
        _connectedPrinter.value = null
        prefs.edit().remove("last_printer_address").remove("last_printer_name").apply()
    }

    fun printReceipt(content: String): Boolean {
        if (_status.value != PrinterStatus.CONNECTED) return false
        // In a real implementation, we would write to BluetoothSocket
        return true
    }
}
