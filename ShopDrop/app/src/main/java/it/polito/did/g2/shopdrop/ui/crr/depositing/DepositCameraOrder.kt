package it.polito.did.g2.shopdrop.ui.crr.depositing

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import it.polito.did.g2.shopdrop.MainViewModel
import it.polito.did.g2.shopdrop.R
import it.polito.did.g2.shopdrop.navigation.Screens
import it.polito.did.g2.shopdrop.ui.common.CameraBase

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CrrDepositCameraOrder(navController : NavController, viewModel: MainViewModel, orderID : String?){

    Box(){
        CameraBase(
            message = stringResource( R.string.crr_scan_parcel),
            checkValue = orderID?:"",
            { navController.navigateUp() }
        ) {
            viewModel.crrDepositing(orderID!!)
            navController.navigate(Screens.CrrDepositing.route+"/$orderID")
        }
    }
}