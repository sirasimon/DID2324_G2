package it.polito.did.g2.shopdrop.ui.adm

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import it.polito.did.g2.shopdrop.MainViewModel
import it.polito.did.g2.shopdrop.data.OrderStateName

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DBOrdersPanel(viewModel: MainViewModel, navController: NavController) {

    var qrOrderID by remember { mutableStateOf("") }
    var openDialog by remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxSize()
            .padding(8.dp)
            .verticalScroll(rememberScrollState())
    ){
        Text("TOTAL ORDERS IN DB: ${viewModel.ordersList.observeAsState().value?.size}")

        viewModel.ordersList.observeAsState().value?.sortedBy { it.stateList?.find { sl -> sl.state == OrderStateName.CREATED }?.timestamp }?.reversed()?.forEach {

            Card(
                Modifier
                    .fillMaxWidth()
                    //.padding(8.dp)
                    .clickable {
                        qrOrderID = it.id.toString()
                        openDialog = true
                    }
            ){
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ){
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("${it.id}")
                        Text("${it.carrierID}")
                    }
                    HorizontalDivider(thickness = 2.dp)

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("${it.storeID}")
                        Text("->")
                        Text("${it.lockerID}")
                        Text("->")
                        Text("${it.customerID}")
                    }

                    HorizontalDivider(thickness = 2.dp)

                    it.stateList?.sortedBy { it.state.ordinal }?.forEach { os ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ){
                            Text(text = os.state.name, fontWeight = FontWeight.Bold)
                            Text(text = os.timestamp.toString())
                        }
                    }

                    if(it.stateList?.maxByOrNull { it.state.ordinal }?.state != OrderStateName.COLLECTED && it.stateList?.maxByOrNull { it.state.ordinal }?.state != OrderStateName.CANCELLED){
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            Button(onClick = { viewModel.updateOrderState(it.id!!) }) {
                                Text(text = "UPDATE STATE")
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
        }

        if(openDialog){
            Log.i("QR_CREATE", "DIALOG OPENS")

            fun onClose(){
                openDialog = false
            }

            Dialog(
                onDismissRequest = { onClose() }
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        //.height(375.dp)
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                ){
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ){
                        Image(
                            bitmap = generateQrCode(qrOrderID).asImageBitmap(),
                            contentDescription = "QR corrispondente al codice $qrOrderID",
                            modifier = Modifier.padding(16.dp)
                        )

                        Button(
                            onClick = { onClose() }
                        ){
                            Text(text = "Done")
                        }
                    }
                }
            }
        }
    }


}

fun generateQrCode(text: String): Bitmap {
    val writer = QRCodeWriter()
    val bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 1000, 1000)

    return Bitmap.createBitmap(bitMatrix.width, bitMatrix.height, Bitmap.Config.ARGB_8888)
        .apply {
            for (x in 0 until bitMatrix.width) {
                for (y in 0 until bitMatrix.height) {
                    setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
        }
}