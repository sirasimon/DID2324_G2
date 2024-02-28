package it.polito.did.g2.shopdrop.ui.crr.depositing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import it.polito.did.g2.shopdrop.R
import it.polito.did.g2.shopdrop.navigation.Screens

@Composable
fun CRRBeforeDeposit(navController: NavController, orderID: String){
    Scaffold(
    ) { paddingValues ->

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {


                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(stringResource(id = R.string.title_deposit).capitalize(), style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(64.dp))

                    Text(stringResource(R.string.info_scan_locker).capitalize())
                    Text("⬇︎")
                    Text(stringResource(R.string.info_scan_order).capitalize())
                    Text("⬇︎")
                    Text(stringResource(R.string.info_deposit_order).capitalize())
                    Text("⬇︎")
                    Text(stringResource(R.string.info_close_locker).capitalize())
                }

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(32.dp)
                ){
                    Button(onClick = { navController.navigateUp()} ){
                        Text(stringResource(R.string.btn_cancel).capitalize())
                    }

                    Button(onClick = { navController.navigate(Screens.CrrDepositCameraLocker.route+"/$orderID") }){
                        Text(stringResource(R.string.btn_proceed).capitalize())
                    }
                }
            }
        }
    }
}