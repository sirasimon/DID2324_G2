package it.polito.did.s306067.shopdrop_client.ui.common

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import it.polito.did.s306067.shopdrop_client.R

@Composable
fun ScanButton(isActive : Boolean = true){
    FloatingActionButton(
        onClick = { /*TODO*/ },
        containerColor = if(isActive) Color.Yellow else Color.Gray) {
        Icon(painter = painterResource(R.drawable.baseline_qr_code_scanner_24), "Done")
    }
}