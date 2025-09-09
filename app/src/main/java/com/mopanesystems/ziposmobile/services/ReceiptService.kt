package com.mopanesystems.ziposmobile.services

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import com.mopanesystems.ziposmobile.data.model.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ReceiptService(private val context: Context) {

    // private val receiptGenerator = ReceiptGenerator(context) // Temporarily disabled - ReceiptGenerator needs iText 7 API update
    private val dateFormatter = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())

    fun generateReceipt(
        transaction: Transaction,
        transactionItems: List<TransactionItem>,
        store: Store?,
        customer: Customer?
    ): ReceiptResult {
        return try {
            val fileName = "receipt_${transaction.transactionNumber}_${dateFormatter.format(Date())}.pdf"
            val receiptDir = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "receipts")
            
            if (!receiptDir.exists()) {
                receiptDir.mkdirs()
            }

            val receiptFile = File(receiptDir, fileName)
            // TODO: Re-enable when ReceiptGenerator is updated for iText 7
            val success = false // receiptGenerator.generateReceipt(...)

            if (success) {
                ReceiptResult.Success(receiptFile.absolutePath)
            } else {
                ReceiptResult.Error("Failed to generate receipt")
            }
        } catch (e: Exception) {
            ReceiptResult.Error("Error generating receipt: ${e.message}")
        }
    }

    fun generateReceiptBitmap(
        transaction: Transaction,
        transactionItems: List<TransactionItem>,
        store: Store?,
        customer: Customer?,
        width: Int = 400,
        height: Int = 600
    ): Bitmap? {
        // TODO: Re-enable when ReceiptGenerator is updated for iText 7
        return null // receiptGenerator.generateReceiptBitmap(...)
    }

    fun getReceiptsDirectory(): File {
        val receiptDir = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "receipts")
        if (!receiptDir.exists()) {
            receiptDir.mkdirs()
        }
        return receiptDir
    }

    fun getReceiptFile(transactionNumber: String): File? {
        val receiptDir = getReceiptsDirectory()
        val files = receiptDir.listFiles { file ->
            file.name.startsWith("receipt_${transactionNumber}_") && file.name.endsWith(".pdf")
        }
        return files?.firstOrNull()
    }

    fun deleteReceipt(transactionNumber: String): Boolean {
        val receiptFile = getReceiptFile(transactionNumber)
        return receiptFile?.delete() ?: false
    }

    fun getAllReceipts(): List<File> {
        val receiptDir = getReceiptsDirectory()
        return receiptDir.listFiles { file ->
            file.name.startsWith("receipt_") && file.name.endsWith(".pdf")
        }?.sortedByDescending { it.lastModified() } ?: emptyList()
    }
}

sealed class ReceiptResult {
    data class Success(val filePath: String) : ReceiptResult()
    data class Error(val message: String) : ReceiptResult()
}
