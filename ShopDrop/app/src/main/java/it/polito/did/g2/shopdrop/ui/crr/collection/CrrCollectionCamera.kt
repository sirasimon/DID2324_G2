package it.polito.did.g2.shopdrop.ui.crr.collection

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
fun CrrCollectionCamera(navController : NavController, viewModel: MainViewModel, orderID : String?){

    Box(){
        CameraBase(
            message = stringResource(id = R.string.crr_scan_parcel),
            checkValue = orderID?:"",
            { navController.navigateUp() }
        ) {
            navController.navigate(Screens.CrrCollectedScreen.route+"/$orderID")
            viewModel.crrHasCollected(orderID!!)
        }
    }
}