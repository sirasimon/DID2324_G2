package it.polito.did.g2.shopdrop.ui.crr.depositing

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import it.polito.did.g2.shopdrop.MainViewModel
import it.polito.did.g2.shopdrop.R
import it.polito.did.g2.shopdrop.navigation.Screens

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CRRDepositing(navController: NavController, viewModel: MainViewModel, orderID: String){
    var prevOpenVal by remember { mutableStateOf(false) }

    val is1Open = viewModel.is1Open.collectAsState()
    val is2Open = viewModel.is2Open.collectAsState()

    if(is1Open.value==true || is2Open.value == true){
        prevOpenVal = true
    }

    Scaffold(
    ) { paddingValues ->

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            if(is1Open.value == false && is2Open.value == false && prevOpenVal){
                Log.i("#####", "Sportello chiuso, deve cambiare")
                viewModel.crrHasDeposited(orderID)
                navController.navigate(Screens.CrrDepositDone.route)
            }

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    stringResource(R.string.title_locker_unlocked).capitalize(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(64.dp))

                Text(stringResource(R.string.txt_remember_to_close), Modifier.fillMaxWidth(.75f),
                    textAlign = TextAlign.Center)
            }
        }
    }
}