package it.polito.did.g2.shopdrop.ui.unlocker

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun UnlockScreen(navController: NavController){
    Box(modifier = Modifier.fillMaxSize()){
        Button(onClick = { navController.navigateUp() }) {
            Text(text = "GO BACK")
        }
    }
}