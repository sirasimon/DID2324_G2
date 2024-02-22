package it.polito.did.g2.shopdrop.ui.adm

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import it.polito.did.g2.shopdrop.MainViewModel

@Composable
fun ADMCameraTest(navController: NavController, viewModel: MainViewModel){


    Column(){
        Button(
            onClick = { navController.navigateUp() }
        ){
            Text("Back")
        }
    }
}