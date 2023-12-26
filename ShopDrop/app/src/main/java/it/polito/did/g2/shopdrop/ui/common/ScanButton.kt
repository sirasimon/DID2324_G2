package it.polito.did.g2.shopdrop.ui.common

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import it.polito.did.g2.shopdrop.MainViewModel
import it.polito.did.g2.shopdrop.R

@Composable
fun ScanButton(isActive : Boolean = true, vm: MainViewModel){
    FloatingActionButton(
        onClick = { vm.debugOpen() },
        containerColor = if(isActive) Color.Yellow else Color.Gray) {
        Icon(painter = painterResource(R.drawable.baseline_qr_code_scanner_24), "Done")
    }
}