package it.polito.did.g2.shopdrop.ui.crr

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import it.polito.did.g2.shopdrop.MainViewModel
import it.polito.did.g2.shopdrop.navigation.Screens

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CrrDeposited(navController: NavController, viewmodel : MainViewModel, orderID : String){


    Column(){
        Text("[Qui immagine]")
        Text("ORDINE DEPOSITATO")

        ExtendedFloatingActionButton(
            onClick = {
                navController.navigate(Screens.CrrHomeScreen.route)
                viewmodel.crrHasDeposited(orderID)
                      },
            modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)) {
            Text("HOME")
        }
    }
}