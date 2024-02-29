package it.polito.did.g2.shopdrop.ui.cst

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import it.polito.did.g2.shopdrop.MainViewModel
import it.polito.did.g2.shopdrop.navigation.Screens

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CSTCollection(navController: NavController, viewmodel : MainViewModel, orderID : String){

    Column(){
        Text("TODO")
        Text("Chiudi lo sportello")

        Button(
            onClick = {
                if(viewmodel.checkClosed(orderID)) {
                    //viewmodel.updateOrderState(orderID)
                    navController.navigate(Screens.CstHome.route)
                }
            }
        ){
            Text(text = "HOME")
        }
    }
}