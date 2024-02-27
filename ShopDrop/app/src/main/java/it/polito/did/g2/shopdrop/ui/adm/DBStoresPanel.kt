package it.polito.did.g2.shopdrop.ui.adm

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import it.polito.did.g2.shopdrop.MainViewModel

@Composable
fun DBStoresPanel(viewModel: MainViewModel, navController: NavController){
    viewModel.storesList.observeAsState().value?.forEach {
        ListItem(headlineContent = { Text("${it.id} @ ${it.address}") })
        HorizontalDivider(thickness = 2.dp)
    }
}