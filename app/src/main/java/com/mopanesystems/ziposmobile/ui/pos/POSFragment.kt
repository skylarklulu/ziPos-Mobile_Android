package com.mopanesystems.ziposmobile.ui.pos

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.mopanesystems.ziposmobile.services.ReceiptService
import com.mopanesystems.ziposmobile.ui.barcode.BarcodeScannerActivity
import com.mopanesystems.ziposmobile.ui.theme.ZiPOSTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

@AndroidEntryPoint
class POSFragment : Fragment() {
    private val viewModel: POSViewModel by viewModels()
    private val currencyFormatter = NumberFormat.getCurrencyInstance(Locale.getDefault())
    private lateinit var receiptService: ReceiptService

    // Barcode scanner launcher
    private val barcodeScannerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val barcode = result.data?.getStringExtra(BarcodeScannerActivity.EXTRA_BARCODE_RESULT)
            barcode?.let { handleScannedBarcode(it) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                ZiPOSTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        POSScreenCompose(
                            viewModel = viewModel,
                            onScanBarcode = { startBarcodeScanner() },
                            onProcessPayment = { paymentMethod ->
                                viewModel.processTransaction(paymentMethod)
                            },
                            onClearCart = { viewModel.clearCart() },
                            currencyFormatter = currencyFormatter
                        )
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Initialize receipt service
        receiptService = ReceiptService(requireContext())
    }


    private fun startBarcodeScanner() {
        val intent = Intent(requireContext(), BarcodeScannerActivity::class.java)
        barcodeScannerLauncher.launch(intent)
    }

    private fun handleScannedBarcode(barcode: String) {
        // Search for product by barcode
        viewModel.findProductByBarcode(barcode)
    }
}
