package it.polito.did.g2.shopdrop.ui.cst

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import it.polito.did.g2.shopdrop.navigation.Screens

@Composable
fun CSTCollectionDone(navController: NavController){
    Button(onClick = { navController.navigate(Screens.CstHome.route) }) {
        Text("GO BACK HOME")
    }
}