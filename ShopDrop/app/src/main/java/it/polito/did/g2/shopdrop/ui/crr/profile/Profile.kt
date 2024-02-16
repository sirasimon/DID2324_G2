package it.polito.did.g2.shopdrop.ui.crr.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import it.polito.did.g2.shopdrop.MainViewModel
import it.polito.did.g2.shopdrop.navigation.Screens


@Composable
fun CRRProfileScreen(navController: NavController, viewModel: MainViewModel) {


    Scaffold(
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            //TODO: ID card

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(onClick = {navController.navigate(Screens.CrrHomeScreen.route)}){
                    Text(text = "Go to home")
                }
            }
        }
    }
}