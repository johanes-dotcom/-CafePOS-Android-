# ☕ CafePOS Mobile

<div align="center">

# CafePOS Mobile ☕

**Aplikasi Point of Sale (POS) modern untuk kafe dan UMKM**

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9+-7F52FF?logo=kotlin\&logoColor=white)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-2024.10.00-4285F4?logo=jetpackcompose\&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Android](https://img.shields.io/badge/Platform-Android-3DDC84?logo=android\&logoColor=white)](https://developer.android.com/)
[![Material 3](https://img.shields.io/badge/Material-3-6200EE)](https://m3.material.io/)
[![Room Database](https://img.shields.io/badge/Database-Room-FF9800)](https://developer.android.com/training/data-storage/room)

**CafePOS Mobile** adalah aplikasi **Point of Sale (POS)** berbasis Android yang dirancang untuk **kafe, coffee shop, dan UMKM**. Aplikasi ini mendukung transaksi kasir, manajemen pesanan, riwayat transaksi, serta laporan penjualan dengan konsep **offline-first** menggunakan **Room Database**.

</div>

---

## 📱 Tentang Proyek

CafePOS Mobile dikembangkan untuk membantu operasional kafe menjadi lebih **cepat, akurat, dan efisien** tanpa bergantung pada koneksi internet.

### 🎯 Tujuan Pengembangan

* Mempermudah proses pemesanan dan pembayaran.
* Menyediakan pencatatan transaksi yang otomatis dan terstruktur.
* Mendukung operasional **offline** saat internet tidak tersedia.
* Menyediakan dashboard dan laporan penjualan untuk membantu pengambilan keputusan bisnis.

---

## ✨ Fitur Utama

### 💳 Sistem Kasir

* Perhitungan otomatis subtotal, pajak, dan total pembayaran.
* Perhitungan uang kembalian otomatis.
* Mendukung berbagai metode pembayaran.
* Validasi input pembayaran.

### 🍽️ Manajemen Pesanan

* Status pesanan **Aktif** dan **Selesai**.
* Input nama pelanggan dan nomor meja.
* Catatan khusus (**notes**) untuk setiap pesanan.
* Monitoring pesanan secara **real-time**.

### 📦 Riwayat Transaksi

* Penyimpanan otomatis seluruh transaksi.
* Invoice unik dengan format `INV-YYYYMMDD-XXXX`.
* Detail item transaksi tersimpan lengkap.

### 📊 Dashboard Penjualan

* Ringkasan penjualan harian.
* Total transaksi dan pendapatan.
* Grafik penjualan harian.
* Statistik performa usaha.

### 🔒 Offline First

* Data tersimpan aman di database lokal.
* Tidak memerlukan koneksi internet untuk operasional utama.
* Menggunakan **Room Persistence Library**.

---

## 📸 Tampilan Aplikasi

### 🛠️ Dashboard Admin

Dashboard admin digunakan untuk memantau statistik penjualan, total transaksi, profit, average order, grafik penjualan harian, serta akses cepat ke menu manajemen dan laporan.

<img width="720" height="1600" alt="image" src="https://github.com/user-attachments/assets/38d4bc72-6e5c-4ba6-9e1f-e3715db930c1" />


---

### 💳 Dashboard Kasir

Dashboard kasir dirancang untuk mempercepat proses transaksi, melihat pesanan aktif, mengakses riwayat transaksi, dan membuat transaksi baru dengan antarmuka yang sederhana dan responsif.


<img width="458" height="1600" alt="image" src="https://github.com/user-attachments/assets/0f595254-184b-4542-98df-36df8676f90a" />

---


---

## 🛠️ Tech Stack

<Table columnSizing="equal" rowDivider={{"size":1,"color":"default"}}><Table.Row header><Table.Cell>Kategori</Table.Cell><Table.Cell>Teknologi</Table.Cell></Table.Row><Table.Row><Table.Cell><Text inline weight="semibold">Bahasa</Text></Table.Cell><Table.Cell>Kotlin 1.9+</Table.Cell></Table.Row><Table.Row><Table.Cell><Text inline weight="semibold">UI</Text></Table.Cell><Table.Cell>Jetpack Compose + Material 3</Table.Cell></Table.Row><Table.Row><Table.Cell><Text inline weight="semibold">Arsitektur</Text></Table.Cell><Table.Cell>MVVM + Repository Pattern</Table.Cell></Table.Row><Table.Row><Table.Cell><Text inline weight="semibold">Database</Text></Table.Cell><Table.Cell>Room Database</Table.Cell></Table.Row><Table.Row><Table.Cell><Text inline weight="semibold">Async</Text></Table.Cell><Table.Cell>Kotlin Coroutines & Flow</Table.Cell></Table.Row><Table.Row><Table.Cell><Text inline weight="semibold">Navigation</Text></Table.Cell><Table.Cell>Compose Navigation</Table.Cell></Table.Row><Table.Row><Table.Cell><Text inline weight="semibold">Image Loading</Text></Table.Cell><Table.Cell>Coil Compose</Table.Cell></Table.Row></Table>

---

## 📁 Struktur Proyek

<CodeBlock language="text" content="app/src/main/java/com/s1ti/cafeposmobile/
│
├── data/
│   ├── local/
│   ├── repository/
│   └── model/
│
├── navigation/
│
├── ui/
│   ├── cashier/
│   ├── screens/
│   ├── components/
│   └── theme/
│
└── util/"/>

---

## 🚀 Menjalankan Proyek

### Clone Repository

<CodeBlock language="bash" content="git clone https://github.com/johanes-dotcom/-CafePOS-Android-.git
cd -CafePOS-Android-"/>

### Jalankan Aplikasi

<List><List.Item>Buka proyek di **Android Studio**</List.Item><List.Item>Tunggu proses **Gradle Sync** selesai</List.Item><List.Item>Hubungkan perangkat Android atau jalankan emulator</List.Item><List.Item>Klik **Run ▶**</List.Item></List>

---

## 📌 Status Pengembangan

<Badge label="Active Development" color="success"/>

## 🗺️ Roadmap Pengembangan

Proyek ini sedang dalam pengembangan aktif. Berikut adalah rencana tahapan pengembangannya:

### Fase 1: Fondasi & Fitur Inti (Selesai) ✅
- [x] Setup Arsitektur MVVM & Clean Architecture.
- [x] Integrasi Database Lokal (Room DB) untuk persistensi data.
- [x] Implementasi Jetpack Compose untuk UI yang responsif.
- [x] Fitur Checkout & Logika Invoice unik (`INV-YYYYMMDD-XXXX`).
- [x] Manajemen status pesanan (Aktif & Selesai).
- [x] Pencatatan detail pelanggan (Nama, Nomor Meja, Catatan).

### Fase 2: Manajemen Produk & Stok (Sedang Berjalan) 🏗️
- [ ] Modul Manajemen Menu (Tambah, Edit, Hapus Produk).
- [ ] Fitur Kategori Produk (Makanan, Minuman, Snack).
- [ ] Manajemen Stok/Inventori sederhana.
- [ ] Fitur Pencarian & Filter Produk di halaman Kasir.

### Fase 3: Laporan & Analitik 📊
- [ ] Ringkasan penjualan harian, mingguan, dan bulanan.
- [ ] Grafik performa penjualan menggunakan library charting.
- [ ] Ekspor riwayat transaksi ke format CSV atau Excel.
- [ ] Fitur kalkulasi laba bersih (berdasarkan `totalCostPrice`).

### Fase 4: Integrasi & Pengalaman Pengguna 🚀
- [ ] Integrasi Cetak Struk via Bluetooth Thermal Printer.
- [ ] Fitur Scan Barcode/QR Code untuk input produk.
- [ ] Dukungan Mode Gelap (Dark Mode).
- [ ] Cloud Sync (Firebase/Supabase) untuk backup data antar perangkat.
- [ ] Support untuk Multi-user (Owner & Kasir) dengan hak akses berbeda.

---

## 👨‍💻 Developer

**Johanes Kelvin Ge'e**

* 🎓 Teknik Informatika — Universitas Kristen Satya Wacana
* 💼 Software Engineer & UI/UX Developer
* 🌐 GitHub: **https://github.com/johanes-dotcom**
* 📧 Email: **[johanesgee@gmail.com](mailto:johanesgee@gmail.com)**

---

<div align="center">

### ⭐ Jangan lupa beri **Star** jika proyek ini bermanfaat!

**Dibuat dengan ❤️ menggunakan Kotlin & Jetpack Compose**

</div>
