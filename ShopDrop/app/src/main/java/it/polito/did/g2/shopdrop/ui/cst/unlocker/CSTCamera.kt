package it.polito.did.g2.shopdrop.ui.cst.unlocker

import android.os.Build
import android.util.Log
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
fun CSTCamera(navController: NavController, viewModel: MainViewModel, orderID: String) {


    Box() {
        CameraBase(
            message = stringResource(R.string.cst_scan_locker),
            checkValue = null,
            { navController.navigateUp() }
        ) {
            //viewModel.cstStartsCollection(it, orderID)
            if(viewModel.cstStartsCollection(it, orderID)) {
                Log.i("SUCCESS", "Ora naviga verso la pagina di ritiro")
                navController.navigate(Screens.CstCollectionScreen.route + "/$orderID")
            }
        }
    }
}