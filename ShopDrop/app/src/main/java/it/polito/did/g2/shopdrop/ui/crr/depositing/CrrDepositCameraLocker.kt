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
fun CrrDepositCameraLocker(navController : NavController, viewModel: MainViewModel, orderID : String?){

    Box(){
        CameraBase(
            message = stringResource(R.string.crr_scan_locker),
            checkValue = null,
            { navController.navigateUp() }
        ) {
            //val success = viewModel.crrStartsDeposit(it, orderID!!)

            //Log.i("SUCCESS", "Success=$success (se true dovrebbe cambiare screen)")

            if(viewModel.crrStartsDeposit(it, orderID!!))
                navController.navigate(Screens.CrrDepositCameraOrder.route+"/$orderID")
        }
    }
}