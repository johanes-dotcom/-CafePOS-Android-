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

<AsyncImage query="CafePOS Mobile Android dashboard admin dark green analytics sales chart quick menu bottom navigation" aspectRatio="9:16" maxWidth={260} maxHeight={520}/>

---

### 💳 Dashboard Kasir

Dashboard kasir dirancang untuk mempercepat proses transaksi, melihat pesanan aktif, mengakses riwayat transaksi, dan membuat transaksi baru dengan antarmuka yang sederhana dan responsif.

<AsyncImage query="CafePOS Mobile Android cashier dashboard dark green POS transaction new order history profile bottom navigation" aspectRatio="9:16" maxWidth={260} maxHeight={520}/>

---

### 📂 Struktur Folder Screenshot

<CodeBlock language="text" content="screenshots/
├── dashboard-admin.png
└── dashboard-kasir.png"/>

> Simpan screenshot aplikasi ke folder `screenshots/` agar tampil otomatis di README GitHub.

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

### Roadmap

* <Checkbox label="Sistem kasir & dashboard" defaultChecked/>
* <Checkbox label="Room Database offline" defaultChecked/>
* <Checkbox label="Riwayat transaksi" defaultChecked/>
* <Checkbox label="Laporan penjualan" defaultChecked/>
* <Checkbox label="Manajemen stok bahan baku" defaultChecked/>
* <Checkbox label="Export PDF laporan"/>
* <Checkbox label="Sinkronisasi cloud"/>
* <Checkbox label="Integrasi printer thermal Bluetooth"/>

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
