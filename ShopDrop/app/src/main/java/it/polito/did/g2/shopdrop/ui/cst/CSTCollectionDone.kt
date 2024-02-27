package it.polito.did.g2.shopdrop.ui.cst

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import it.polito.did.g2.shopdrop.R
import it.polito.did.g2.shopdrop.navigation.Screens
import it.polito.did.g2.shopdrop.ui.common.DoneScreen

@Composable
fun CSTCollectionDone(navController: NavController){

    DoneScreen(
        stringResource(R.string.done_order_collected),
        stringResource(R.string.btn_back_home).capitalize()
    ) {
        navController.navigate(Screens.CstHome.route)
    }
}