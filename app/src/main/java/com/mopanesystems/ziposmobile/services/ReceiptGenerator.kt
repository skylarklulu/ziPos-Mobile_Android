package com.mopanesystems.ziposmobile.services

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfWriter
import com.mopanesystems.ziposmobile.data.model.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.math.BigDecimal
import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import java.util.*

class ReceiptGenerator(private val context: Context) {

    private val currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US)
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    fun generateReceipt(
        transaction: Transaction,
        transactionItems: List<TransactionItem>,
        store: Store?,
        customer: Customer?,
        outputPath: String
    ): Boolean {
        return try {
            val document = Document(PageSize.A4)
            val writer = PdfWriter.getInstance(document, FileOutputStream(outputPath))
            document.open()

            // Add store header
            addStoreHeader(document, store)

            // Add receipt title
            addReceiptTitle(document, transaction)

            // Add customer info if available
            customer?.let { addCustomerInfo(document, it) }

            // Add transaction details
            addTransactionDetails(document, transaction)

            // Add items table
            addItemsTable(document, transactionItems)

            // Add totals
            addTotals(document, transaction)

            // Add footer
            addFooter(document, store)

            document.close()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun addStoreHeader(document: Document, store: Store?) {
        val headerFont = Font(Font.FontFamily.HELVETICA, 18f, Font.BOLD, BaseColor.BLACK)
        val subHeaderFont = Font(Font.FontFamily.HELVETICA, 12f, Font.NORMAL, BaseColor.BLACK)

        val storeName = store?.name ?: "ziPOS Store"
        val storeAddress = store?.address ?: "123 Main Street"
        val storePhone = store?.phone ?: "(555) 123-4567"

        val headerParagraph = Paragraph(storeName, headerFont)
        headerParagraph.alignment = Element.ALIGN_CENTER
        document.add(headerParagraph)

        val addressParagraph = Paragraph(storeAddress, subHeaderFont)
        addressParagraph.alignment = Element.ALIGN_CENTER
        document.add(addressParagraph)

        val phoneParagraph = Paragraph(storePhone, subHeaderFont)
        phoneParagraph.alignment = Element.ALIGN_CENTER
        document.add(phoneParagraph)

        // Add separator line
        document.add(Chunk.NEWLINE)
        val line = LineSeparator()
        document.add(Chunk(line))
        document.add(Chunk.NEWLINE)
    }

    private fun addReceiptTitle(document: Document, transaction: Transaction) {
        val titleFont = Font(Font.FontFamily.HELVETICA, 14f, Font.BOLD, BaseColor.BLACK)
        val titleParagraph = Paragraph("RECEIPT", titleFont)
        titleParagraph.alignment = Element.ALIGN_CENTER
        document.add(titleParagraph)

        val transactionNumberFont = Font(Font.FontFamily.HELVETICA, 10f, Font.NORMAL, BaseColor.BLACK)
        val transactionNumberParagraph = Paragraph("Transaction #: ${transaction.transactionNumber}", transactionNumberFont)
        transactionNumberParagraph.alignment = Element.ALIGN_CENTER
        document.add(transactionNumberParagraph)

        val dateParagraph = Paragraph("Date: ${transaction.createdAt.format(dateFormatter)}", transactionNumberFont)
        dateParagraph.alignment = Element.ALIGN_CENTER
        document.add(dateParagraph)

        document.add(Chunk.NEWLINE)
    }

    private fun addCustomerInfo(document: Document, customer: Customer) {
        val customerFont = Font(Font.FontFamily.HELVETICA, 10f, Font.BOLD, BaseColor.BLACK)
        val customerInfoFont = Font(Font.FontFamily.HELVETICA, 10f, Font.NORMAL, BaseColor.BLACK)

        val customerParagraph = Paragraph("Customer Information", customerFont)
        document.add(customerParagraph)

        val nameParagraph = Paragraph("Name: ${customer.fullName}", customerInfoFont)
        document.add(nameParagraph)

        customer.phone?.let {
            val phoneParagraph = Paragraph("Phone: $it", customerInfoFont)
            document.add(phoneParagraph)
        }

        customer.email?.let {
            val emailParagraph = Paragraph("Email: $it", customerInfoFont)
            document.add(emailParagraph)
        }

        document.add(Chunk.NEWLINE)
    }

    private fun addTransactionDetails(document: Document, transaction: Transaction) {
        val detailsFont = Font(Font.FontFamily.HELVETICA, 10f, Font.NORMAL, BaseColor.BLACK)

        val cashierParagraph = Paragraph("Cashier: User ${transaction.cashierId}", detailsFont)
        document.add(cashierParagraph)

        val paymentMethodParagraph = Paragraph("Payment Method: ${transaction.paymentMethod?.name ?: "N/A"}", detailsFont)
        document.add(paymentMethodParagraph)

        document.add(Chunk.NEWLINE)
    }

    private fun addItemsTable(document: Document, transactionItems: List<TransactionItem>) {
        val table = com.itextpdf.text.pdf.PdfPTable(4)
        table.widthPercentage = 100f
        table.setWidths(floatArrayOf(3f, 1f, 1f, 1f))

        // Table headers
        val headerFont = Font(Font.FontFamily.HELVETICA, 10f, Font.BOLD, BaseColor.BLACK)
        val headerCellFont = Font(Font.FontFamily.HELVETICA, 10f, Font.BOLD, BaseColor.WHITE)

        val headers = arrayOf("Item", "Qty", "Price", "Total")
        headers.forEach { header ->
            val cell = com.itextpdf.text.pdf.PdfPCell(Phrase(header, headerCellFont))
            cell.backgroundColor = BaseColor.BLACK
            cell.horizontalAlignment = Element.ALIGN_CENTER
            cell.padding = 5f
            table.addCell(cell)
        }

        // Table data
        val dataFont = Font(Font.FontFamily.HELVETICA, 9f, Font.NORMAL, BaseColor.BLACK)
        transactionItems.forEach { item ->
            // Item name (truncated if too long)
            val itemName = "Product ID: ${item.productId}" // TODO: Get actual product name
            val itemCell = com.itextpdf.text.pdf.PdfPCell(Phrase(itemName, dataFont))
            itemCell.padding = 5f
            table.addCell(itemCell)

            // Quantity
            val qtyCell = com.itextpdf.text.pdf.PdfPCell(Phrase(item.quantity.toString(), dataFont))
            qtyCell.horizontalAlignment = Element.ALIGN_CENTER
            qtyCell.padding = 5f
            table.addCell(qtyCell)

            // Unit price
            val priceCell = com.itextpdf.text.pdf.PdfPCell(Phrase(currencyFormatter.format(item.unitPrice), dataFont))
            priceCell.horizontalAlignment = Element.ALIGN_RIGHT
            priceCell.padding = 5f
            table.addCell(priceCell)

            // Total
            val totalCell = com.itextpdf.text.pdf.PdfPCell(Phrase(currencyFormatter.format(item.totalPrice), dataFont))
            totalCell.horizontalAlignment = Element.ALIGN_RIGHT
            totalCell.padding = 5f
            table.addCell(totalCell)
        }

        document.add(table)
        document.add(Chunk.NEWLINE)
    }

    private fun addTotals(document: Document, transaction: Transaction) {
        val totalsFont = Font(Font.FontFamily.HELVETICA, 10f, Font.NORMAL, BaseColor.BLACK)
        val totalFont = Font(Font.FontFamily.HELVETICA, 12f, Font.BOLD, BaseColor.BLACK)

        // Subtotal
        val subtotalParagraph = Paragraph("Subtotal: ${currencyFormatter.format(transaction.subtotal)}", totalsFont)
        subtotalParagraph.alignment = Element.ALIGN_RIGHT
        document.add(subtotalParagraph)

        // Tax
        val taxParagraph = Paragraph("Tax: ${currencyFormatter.format(transaction.taxAmount)}", totalsFont)
        taxParagraph.alignment = Element.ALIGN_RIGHT
        document.add(taxParagraph)

        // Discount (if any)
        if (transaction.discountAmount > BigDecimal.ZERO) {
            val discountParagraph = Paragraph("Discount: -${currencyFormatter.format(transaction.discountAmount)}", totalsFont)
            discountParagraph.alignment = Element.ALIGN_RIGHT
            document.add(discountParagraph)
        }

        // Total
        val totalParagraph = Paragraph("TOTAL: ${currencyFormatter.format(transaction.totalAmount)}", totalFont)
        totalParagraph.alignment = Element.ALIGN_RIGHT
        document.add(totalParagraph)

        // Payment details
        if (transaction.paidAmount > transaction.totalAmount) {
            val paidParagraph = Paragraph("Paid: ${currencyFormatter.format(transaction.paidAmount)}", totalsFont)
            paidParagraph.alignment = Element.ALIGN_RIGHT
            document.add(paidParagraph)

            val changeParagraph = Paragraph("Change: ${currencyFormatter.format(transaction.changeAmount)}", totalsFont)
            changeParagraph.alignment = Element.ALIGN_RIGHT
            document.add(changeParagraph)
        }

        document.add(Chunk.NEWLINE)
    }

    private fun addFooter(document: Document, store: Store?) {
        val footerFont = Font(Font.FontFamily.HELVETICA, 8f, Font.NORMAL, BaseColor.GRAY)
        val footerParagraph = Paragraph("Thank you for your business!", footerFont)
        footerParagraph.alignment = Element.ALIGN_CENTER
        document.add(footerParagraph)

        val returnPolicyParagraph = Paragraph("Returns accepted within 30 days with receipt", footerFont)
        returnPolicyParagraph.alignment = Element.ALIGN_CENTER
        document.add(returnPolicyParagraph)

        document.add(Chunk.NEWLINE)
    }

    fun generateReceiptBitmap(
        transaction: Transaction,
        transactionItems: List<TransactionItem>,
        store: Store?,
        customer: Customer?,
        width: Int = 400,
        height: Int = 600
    ): Bitmap? {
        return try {
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            val paint = Paint().apply {
                color = Color.WHITE
                style = Paint.Style.FILL
            }
            canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)

            // Draw receipt content
            paint.apply {
                color = Color.BLACK
                textSize = 16f
                isAntiAlias = true
            }

            var y = 30f
            val lineHeight = 20f

            // Store header
            val storeName = store?.name ?: "ziPOS Store"
            canvas.drawText(storeName, width / 2f - paint.measureText(storeName) / 2, y, paint)
            y += lineHeight

            // Transaction details
            paint.textSize = 12f
            canvas.drawText("Transaction #: ${transaction.transactionNumber}", 20f, y, paint)
            y += lineHeight
            canvas.drawText("Date: ${transaction.createdAt.format(dateFormatter)}", 20f, y, paint)
            y += lineHeight * 2

            // Items
            paint.textSize = 10f
            transactionItems.forEach { item ->
                val itemText = "Product ID: ${item.productId} x${item.quantity} = ${currencyFormatter.format(item.totalPrice)}"
                canvas.drawText(itemText, 20f, y, paint)
                y += lineHeight
            }

            y += lineHeight
            paint.textSize = 12f
            canvas.drawText("Total: ${currencyFormatter.format(transaction.totalAmount)}", 20f, y, paint)

            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
