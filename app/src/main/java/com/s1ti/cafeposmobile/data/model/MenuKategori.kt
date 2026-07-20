package com.s1ti.cafeposmobile.data.model

/**
 * REQ-2.1, REQ-2.8: kategori menu -- dipakai buat tampilan daftar & filter.
 */
enum class MenuKategori(val label: String) {
    MAKANAN("Makanan"),
    MINUMAN("Minuman"),
    SNACK("Snack"),
    LAINNYA("Lainnya")
}
