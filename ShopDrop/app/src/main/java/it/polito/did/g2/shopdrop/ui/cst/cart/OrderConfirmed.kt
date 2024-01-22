package it.polito.did.g2.shopdrop.ui.cst.cart

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import it.polito.did.g2.shopdrop.R

@Composable
fun OrderConfirmed(navController: NavController){

    Column(){
        Text("[Qui immagine]")
        Text(stringResource(id = R.string.title_order_done).capitalize())
        Text(stringResource(id = R.string.txt_order_done).capitalize())
        ExtendedFloatingActionButton(onClick = { navController.navigate("COrderListScreen") }, modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)) {
            Text(stringResource(id = R.string.btn_my_orders).capitalize())
        }
        TextButton(onClick = { navController.navigate("CustomerHome") }) {
            Text(stringResource(id = R.string.btn_my_orders).capitalize())
        }
    }
}