# Aplikasi Pemesanan Toko Thrift Berbasis Android

Aplikasi Android untuk pemesanan produk barang bekas (thrift) dengan fitur untuk Pelanggan dan Admin.

## Fitur Utama

### Pelanggan (Customer)
- **Registrasi & Login**: Mendaftar akun baru dan masuk ke aplikasi.
- **Katalog Produk**: Menjelajahi berbagai produk thrift.
- **Detail Produk**: Melihat deskripsi lengkap dan stok produk.
- **Keranjang Belanja**: Menambah, mengubah jumlah, dan menghapus item dari keranjang.
- **Checkout**: Melakukan pemesanan produk.
- **Riwayat Pesanan**: Melihat status dan riwayat transaksi.
- **Profil**: Mengelola informasi alamat pengiriman.

### Admin
- **Panel Admin**: Ringkasan transaksi dan pendapatan.
- **Kelola Katalog**: Menambah (CRUD), mengubah, dan menghapus produk dari katalog.
- **Kelola Pesanan**: Memperbarui status pesanan (Proses, Selesai, Dibatalkan).
- **Laporan Penjualan**: Melihat total transaksi dan total pendapatan.

## Teknologi
- **Bahasa**: Kotlin
- **Arsitektur**: MVVM (Model-View-ViewModel)
- **Database**: Room (Local SQLite)
- **UI/UX**: Material Design
- **Navigation**: Jetpack Navigation Component

## Cara Menjalankan Project

1. Pastikan Anda memiliki **Android Studio** versi terbaru (koala atau lebih baru direkomendasikan).
2. Clone atau Download repository ini.
3. Buka Android Studio dan pilih **Open**, lalu arahkan ke folder project ini.
4. Tunggu hingga proses **Gradle Sync** selesai.
5. Jalankan aplikasi menggunakan **Emulator** atau **Device fisik** (Minimum Android 8.0 Oreo).

### Akun Demo (Seeded Data)
- **Admin**:
  - Username: `admin`
  - Password: `admin` (Centang "Masuk sebagai Admin")
- **Customer**:
  - Email: `dhito@demo.com`
  - Password: `123456`

## Struktur Project
- `data/model`: Definisi entitas database (User, Admin, Product, Order).
- `data/local`: Room Database, DAO.
- `data/repository`: Lapisan abstraksi data.
- `ui`: Lapisan UI dengan Fragment dan ViewModel untuk Auth, Customer, dan Admin.
- `utils`: Helper classes seperti SessionManager.
