package it.polito.did.g2.shopdrop.ui.crr.depositing

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import it.polito.did.g2.shopdrop.R
import it.polito.did.g2.shopdrop.navigation.Screens
import it.polito.did.g2.shopdrop.ui.common.DoneScreen

@Composable
fun CRRDepositDone(navController: NavController){

    DoneScreen(message = stringResource(id = R.string.deposit_done).capitalize(),
        buttonTxt = stringResource(id = R.string.btn_goto_home)
    ) {
        navController.navigate(Screens.CrrHomeScreen.route)
    }
}