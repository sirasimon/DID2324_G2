package it.polito.did.g2.shopdrop.ui.cst.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import it.polito.did.g2.shopdrop.R
import it.polito.did.g2.shopdrop.navigation.Screens
import it.polito.did.g2.shopdrop.ui.theme.tertiaryLight

@Composable
fun OrderSent(navController: NavController){

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ){
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .background(Color.Green)
        ){
            Image(
                painter = painterResource(id = R.drawable.order_sent),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(.9f)
            )
        }

        Column(){
            Text(
                stringResource(id = R.string.title_order_done).capitalize(),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(vertical = 16.dp)
            )
            Text(
                stringResource(id = R.string.txt_order_done).capitalize(),
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Column {
            ExtendedFloatingActionButton(
                onClick = { navController.navigate(Screens.CstOrderHistory.route) },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp)
            ) {
                Text(stringResource(id = R.string.btn_my_orders).capitalize())
            }

            ExtendedFloatingActionButton(
                onClick = { navController.navigate(Screens.CstHome.route) },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
                containerColor = tertiaryLight
            ) {
                Text(stringResource(id = R.string.btn_goto_home).capitalize())
            }
        }
    }
}