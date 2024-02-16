package it.polito.did.g2.shopdrop.ui.cst.unlocker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import it.polito.did.g2.shopdrop.R

@Composable
fun UnlockScreen(navController: NavController){
    Text(stringResource(id = R.string.title_order_ready).capitalize())
    Text(stringResource(id = R.string.txt_open_locker))
    Text(stringResource(id = R.string.txt_collect_order))
    Text(stringResource(id = R.string.txt_close_locker))

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ){
        Button(onClick = { navController.navigateUp() }) {
            Text(stringResource(id = R.string.btn_cancel).capitalize())
        }

        Button(onClick = { navController.navigate("OpeningScreens") }) {
            Text(stringResource(id = R.string.btn_open).capitalize())
        }
    }
}