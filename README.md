# ☕ CafePOS Mobile

<div align="center">

# CafePOS Mobile ☕

**Aplikasi Point of Sale (POS) modern untuk kafe dan UMKM**

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9+-7F52FF?logo=kotlin\&logoColor=white)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-2024.10.00-4285F4?logo=jetpackcompose\&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Android](https://img.shields.io/badge/Platform-Android-3DDC84?logo=android\&logoColor=white)](https://developer.android.com/)
[![Material 3](https://img.shields.io/badge/Material-3-6200EE)](https://m3.material.io/)
[![Room Database](https://img.shields.io/badge/Database-Room-FF9800)](https://developer.android.com/training/data-storage/room)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

**CafePOS Mobile** adalah aplikasi **Point of Sale (POS)** berbasis Android yang dirancang khusus untuk **kafe, coffee shop, restoran kecil, dan UMKM**. Aplikasi ini mendukung operasional kasir secara **offline-first**, manajemen pesanan, riwayat transaksi, serta pelaporan penjualan dengan antarmuka modern menggunakan **Jetpack Compose**.

</div>

---

## 📱 Tentang Proyek

CafePOS Mobile dikembangkan untuk membantu bisnis kafe mengelola transaksi harian dengan lebih cepat, akurat, dan efisien tanpa bergantung pada koneksi internet.

### 🎯 Tujuan Pengembangan

* Mempermudah proses pemesanan dan pembayaran di kasir.
* Menyediakan sistem pencatatan transaksi yang otomatis dan terstruktur.
* Mendukung operasional **offline** sehingga tetap dapat digunakan saat internet tidak tersedia.
* Menyediakan dashboard dan laporan penjualan untuk membantu pemilik usaha mengambil keputusan.

---

## ✨ Fitur Unggulan

### 🧾 Sistem Kasir (Checkout)

* Perhitungan otomatis subtotal, pajak, dan total pembayaran.
* Mendukung pembayaran **tunai** dan metode pembayaran lainnya.
* Perhitungan **uang kembalian otomatis**.
* Validasi input pembayaran untuk menghindari kesalahan transaksi.

### 🍽️ Manajemen Pesanan

* Status pesanan **Aktif** dan **Selesai**.
* Informasi **nama pelanggan**, **nomor meja**, dan **catatan khusus (notes)**.
* Monitoring pesanan secara real-time di dashboard kasir.

### 📦 Riwayat Transaksi

* Penyimpanan otomatis seluruh transaksi.
* Format invoice unik: `INV-YYYYMMDD-XXXX`.
* Detail item transaksi tersimpan lengkap untuk audit dan laporan.

### 📊 Laporan Penjualan

* Ringkasan penjualan harian.
* Total transaksi dan pendapatan.
* Statistik sederhana untuk membantu analisis usaha.

### 🔒 Offline First

* Seluruh data disimpan secara lokal menggunakan **Room Database**.
* Tidak memerlukan koneksi internet untuk operasional utama.
* Data tetap aman meskipun aplikasi ditutup atau perangkat direstart.

### 🎨 Modern UI

* Dibangun dengan **Material Design 3**.
* Antarmuka responsif dan nyaman digunakan di berbagai ukuran layar.
* Menggunakan **Jetpack Compose** untuk performa UI yang lebih baik.

---

## 🏗️ Arsitektur Aplikasi

Aplikasi menggunakan pola **MVVM (Model–View–ViewModel)** dan **Repository Pattern** untuk memisahkan UI, logika bisnis, dan akses data.

```text
┌─────────────┐
│     UI      │  → Jetpack Compose Screens
└──────┬──────┘
       │
┌──────▼──────┐
│  ViewModel  │  → State Management
└──────┬──────┘
       │
┌──────▼──────┐
│ Repository  │  → Business Logic
└──────┬──────┘
       │
┌──────▼──────┐
│   Room DB   │  → Local Storage
└─────────────┘
```

---

## 🛠️ Tech Stack

| Kategori                  | Teknologi                                |
| ------------------------- | ---------------------------------------- |
| **Bahasa**                | Kotlin 1.9+                              |
| **UI**                    | Jetpack Compose + Material 3             |
| **Arsitektur**            | MVVM + Repository Pattern                |
| **Database**              | Room Persistence Library                 |
| **Async**                 | Kotlin Coroutines & Flow                 |
| **Navigasi**              | Compose Navigation                       |
| **Image Loading**         | Coil Compose                             |
| **Dependency Management** | Version Catalog + Compose BOM 2024.10.00 |

---

## 📁 Struktur Proyek

```text
app/src/main/java/com/s1ti/cafeposmobile/
│
├── data/
│   ├── local/
│   │   ├── dao/
│   │   ├── entity/
│   │   └── AppDatabase.kt
│   ├── repository/
│   └── model/
│
├── navigation/
│   ├── AppNavHost.kt
│   └── Screen.kt
│
├── ui/
│   ├── cashier/
│   ├── screens/
│   │   ├── dashboard/
│   │   ├── login/
│   │   ├── menu/
│   │   └── stok/
│   ├── components/
│   └── theme/
│
└── util/
```

---

## 🔄 Alur Transaksi

<List><List.Item>Kasir memilih menu yang dipesan pelanggan.</List.Item><List.Item>Sistem menghitung subtotal dan pajak otomatis.</List.Item><List.Item>Kasir memasukkan nominal pembayaran.</List.Item><List.Item>Aplikasi menghitung kembalian.</List.Item><List.Item>Transaksi disimpan ke database lokal.</List.Item><List.Item>Invoice unik dibuat otomatis.</List.Item><List.Item>Status pesanan diubah menjadi **Aktif** untuk diproses dapur.</List.Item><List.Item>Setelah pesanan selesai, status diperbarui menjadi **Selesai**.</List.Item></List>

---

## 🧠 Contoh Logika Invoice

<pre><code class="language-kotlin">fun generateInvoiceId(): String {
    val date = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
    val sequence = (1000..9999).random()
    return "INV-$date-$sequence"
}
</code></pre>

Contoh hasil:

<pre><code>INV-20260720-4831
</code></pre>

---

## 🚀 Cara Menjalankan Proyek

### Prasyarat

* **Android Studio Koala / Narwhal atau lebih baru**
* **JDK 17**
* **Android SDK 34+**
* **Gradle 8+**

### Langkah Instalasi

<pre><code class="language-bash"># Clone repository
git clone https://github.com/johanes-dotcom/-CafePOS-Android-.git

# Masuk ke folder proyek
cd -CafePOS-Android-

# Buka dengan Android Studio
</code></pre>

### Menjalankan Aplikasi

<List><List.Item>Buka proyek di **Android Studio**.</List.Item><List.Item>Tunggu proses **Gradle Sync** selesai.</List.Item><List.Item>Hubungkan perangkat Android atau jalankan emulator.</List.Item><List.Item>Klik **Run ▶**.</List.Item></List>

---

## 📸 Tampilan Aplikasi

<Box background="surface" border={{"size":1,"color":"default"}} radius="3xl" padding={6}><Row align="center"><Icon name="image" color="secondary"/><Text weight="semibold">Screenshot aplikasi</Text></Row><Text color="default">Tambahkan screenshot dashboard, halaman transaksi, pembayaran, dan laporan di sini.</Text></Box>

Contoh penempatan screenshot:

<pre><code class="language-md">| Dashboard | Transaksi | Pembayaran |
|-----------|------------|-------------|
| ![dashboard](screenshots/dashboard.png) | ![transaksi](screenshots/transaksi.png) | ![payment](screenshots/payment.png) |
</code></pre>

---

## 📌 Status Pengembangan

<Badge label="Active Development" color="success"/>

### Roadmap

* <Checkbox label="Sistem kasir dasar" defaultChecked/>
* <Checkbox label="Room Database offline" defaultChecked/>
* <Checkbox label="Riwayat transaksi" defaultChecked/>
* <Checkbox label="Dashboard penjualan" defaultChecked/>
* <Checkbox label="Manajemen stok bahan baku" defaultChecked/>
* <Checkbox label="Export PDF laporan"/>
* <Checkbox label="Sinkronisasi cloud/Firebase"/>
* <Checkbox label="Multi-device / multi-cashier"/>
* <Checkbox label="Integrasi printer thermal Bluetooth"/>

---

## 🤝 Kontribusi

Kontribusi sangat terbuka untuk pengembangan fitur baru, perbaikan bug, dan peningkatan performa.

### Langkah kontribusi

<pre><code class="language-bash"># Fork repository
# Buat branch baru
git checkout -b fitur-baru

# Commit perubahan
git commit -m "Menambahkan fitur baru"

# Push ke branch
git push origin fitur-baru
</code></pre>

Lalu buat **Pull Request** 🚀

---

## 👨‍💻 Developer

**Johanes Kelvin Ge'e**

* 🎓 Teknik Informatika — Universitas Kristen Satya Wacana
* 💼 Software Engineer & UI/UX Developer
* 🌐 GitHub: https://github.com/johanes-dotcom
* 📧 Email: [johanesgee@gmail.com](mailto:johanesgee@gmail.com)

---

## 📄 Lisensi

Proyek ini menggunakan lisensi **MIT**. Silakan gunakan, modifikasi, dan distribusikan untuk kebutuhan pembelajaran maupun pengembangan bisnis.

---

<div align="center">

### ⭐ Jika proyek ini bermanfaat, jangan lupa beri **Star** di GitHub!

**Dibuat dengan ❤️ menggunakan Kotlin & Jetpack Compose**

</div>
