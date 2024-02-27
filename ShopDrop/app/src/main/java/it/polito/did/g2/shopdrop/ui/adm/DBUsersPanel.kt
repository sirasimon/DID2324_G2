package it.polito.did.g2.shopdrop.ui.adm

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import it.polito.did.g2.shopdrop.MainViewModel

@Composable
fun DBUsersPanel(viewModel: MainViewModel, navController: NavController) {
    Text("Total users: ${viewModel.usersList.observeAsState().value?.size}")

    viewModel.usersList.observeAsState().value?.forEach {
        Card(Modifier.fillMaxWidth()) {

        }
    }
}