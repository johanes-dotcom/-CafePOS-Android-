package com.s1ti.cafeposmobile.util

import android.util.Base64
import java.security.SecureRandom
import java.security.spec.KeySpec
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

/**
 * REQ-1.11: Sistem harus menyimpan password pengguna dalam bentuk yang aman
 * (hashed) sehingga tidak disimpan sebagai teks biasa (plain text).
 *
 * Pakai PBKDF2WithHmacSHA256 -- sudah tersedia bawaan di JDK/Android,
 * jadi tidak perlu tambah dependency library hashing pihak ketiga.
 *
 * Format string yang disimpan ke DB: "iterasi:saltBase64:hashBase64"
 * Salt unik per-user dibuat otomatis tiap kali hash() dipanggil.
 */
object PasswordHasher {

    private const val ITERATIONS = 10_000
    private const val KEY_LENGTH = 256
    private const val ALGORITHM = "PBKDF2WithHmacSHA256"

    fun hash(plainPassword: String): String {
        val salt = ByteArray(16).also { SecureRandom().nextBytes(it) }
        val hashBytes = pbkdf2(plainPassword.toCharArray(), salt, ITERATIONS)
        val saltB64 = Base64.encodeToString(salt, Base64.NO_WRAP)
        val hashB64 = Base64.encodeToString(hashBytes, Base64.NO_WRAP)
        return "$ITERATIONS:$saltB64:$hashB64"
    }

    fun verify(plainPassword: String, storedHash: String): Boolean {
        return try {
            val parts = storedHash.split(":")
            if (parts.size != 3) return false
            val iterations = parts[0].toInt()
            val salt = Base64.decode(parts[1], Base64.NO_WRAP)
            val expectedHash = Base64.decode(parts[2], Base64.NO_WRAP)
            val actualHash = pbkdf2(plainPassword.toCharArray(), salt, iterations)
            actualHash.contentEquals(expectedHash)
        } catch (e: Exception) {
            false
        }
    }

    private fun pbkdf2(password: CharArray, salt: ByteArray, iterations: Int): ByteArray {
        val spec: KeySpec = PBEKeySpec(password, salt, iterations, KEY_LENGTH)
        val factory = SecretKeyFactory.getInstance(ALGORITHM)
        return factory.generateSecret(spec).encoded
    }
}
