package it.polito.did.g2.shopdrop

import android.content.Context
import android.util.Log
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.tasks.await

class BarcodeScanner {
    class BarcodeScanner(
        appContext: Context
    ) {

        private val options = GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_CODE_128,
                Barcode.FORMAT_DATA_MATRIX,
                Barcode.FORMAT_QR_CODE
            )
            .build()

        private val scanner = GmsBarcodeScanning.getClient(appContext, options)
        val barCodeResults = MutableStateFlow<String?>(null)

        suspend fun startScan() {
            try {
                val result = scanner.startScan().await()
                barCodeResults.value = result.rawValue
                Log.i("BC_SCANNER", "Scanned ${barCodeResults.value}")

            } catch (e: Exception) {
                Log.e("BC_SCANNER", "ERROR $e")
            }
        }

        /* alt:
        scanner.startScan()
        .addOnSuccessListener { barcode ->
            // Task completed successfully
        }
        .addOnCanceledListener {
            // Task canceled
        }
        .addOnFailureListener { e ->
            // Task failed with an exception
        }*/

    }
}