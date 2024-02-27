package it.polito.did.g2.shopdrop.ui.crr.collection

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import it.polito.did.g2.shopdrop.MainViewModel
import it.polito.did.g2.shopdrop.R
import it.polito.did.g2.shopdrop.navigation.Screens
import it.polito.did.g2.shopdrop.ui.common.DoneScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CrrCollected(viewModel: MainViewModel, navController: NavController, orderID : String){

    DoneScreen(
        stringResource(R.string.done_order_collected).capitalize(),
        stringResource(R.string.done_btn_see_order).capitalize()
    ) {
        navController.navigate(Screens.CrrOrderDetail.route+"/$orderID")
    }
}