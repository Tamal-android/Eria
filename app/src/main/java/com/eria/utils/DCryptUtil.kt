package com.eria.utils

import android.util.Base64
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


object DCryptUtil {
    private const val AES_MODE_ENCCRYPTION_NAME="AES"
    private const val AES_MODE = "AES/CBC/PKCS7Padding"
    private val CHARSET = Charsets.UTF_8
    private const val HASH_ALGORITHM = "SHA-256"
    private val ivBytes = byteArrayOf(
        0x00,
        0x00,
        0x00,
        0x00,
        0x00,
        0x00,
        0x00,
        0x00,
        0x00,
        0x00,
        0x00,
        0x00,
        0x00,
        0x00,
        0x00,
        0x00
    )
    const val PASSWORD = "dGRscGFzc3dvcmQ=" //base 64 decode value

    private fun generateKey(password: String): SecretKeySpec {
        val digest =
            MessageDigest.getInstance(HASH_ALGORITHM)
        val bytes = password.toByteArray(CHARSET)
        digest.update(bytes, 0, bytes.size)
        val key = digest.digest()
        return SecretKeySpec(key, AES_MODE_ENCCRYPTION_NAME)
    }

    fun encrypt(password: String?, message: String?): String? {
        if (password.isNullOrBlank() || message.isNullOrBlank()) {
            return message
        }
        return try {
            val key =
                generateKey(password)
            val cipherArray = encrypt(key, ivBytes, message.toByteArray(CHARSET))
            cipherArray?.let { array ->
                return Base64.encodeToString(array, Base64.NO_WRAP)
            }
        } catch (e: Exception) {
            return null
        }
    }

    private fun encrypt(
        keySpec: SecretKeySpec?,
        iv: ByteArray,
        message: ByteArray?
    ): ByteArray? {
        message?.let { messageArray ->
            keySpec?.let { key ->
                val cipher = Cipher.getInstance(AES_MODE)
                cipher?.let { cipher ->
                    val ivSpec = IvParameterSpec(iv)
                    ivSpec?.let { spec ->
                        cipher.init(Cipher.ENCRYPT_MODE, key, spec)
                        return cipher.doFinal(messageArray)
                    }
                }
            }
        }
        return null
    }

    fun decrypt(password: String?, message: String?): String? {
        if (password.isNullOrBlank() || message.isNullOrBlank()) {
            return message
        }
        return try {
            val key =
                generateKey(password)
            val decodedCipherText =
                Base64.decode(message, Base64.NO_WRAP)
            val decryptedBytes = decrypt(
                key,
                ivBytes,
                decodedCipherText
            )
            decryptedBytes?.let { array ->
                return array.toString(Charsets.UTF_8)
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun decrypt(
        keySpec: SecretKeySpec?,
        iv: ByteArray,
        messageArray: ByteArray?
    ): ByteArray? {
        messageArray?.let { array ->
            keySpec?.let { key ->
                val cipher =
                    Cipher.getInstance(AES_MODE)
                cipher?.let {
                    val ivSpec = IvParameterSpec(iv)
                    cipher.init(Cipher.DECRYPT_MODE, key, ivSpec)
                    return cipher.doFinal(array)
                }
            }
        }
        return null
    }
}