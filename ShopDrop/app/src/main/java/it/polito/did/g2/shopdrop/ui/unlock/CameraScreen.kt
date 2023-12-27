package it.polito.did.g2.shopdrop.ui.unlock

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import it.polito.did.g2.shopdrop.MainViewModel
import it.polito.did.g2.shopdrop.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraScreen(navController : NavController, viewModel: MainViewModel){

    Box(){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Blue)
        ){
            Text(text = "CAMERA SCREEN", modifier = Modifier.align(Alignment.Center))
        }

        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth().padding(16.dp)){
            FloatingActionButton(
                shape = CircleShape,
                onClick = {navController.navigateUp()}
            ){
                Icon(Icons.Filled.ArrowBackIos, contentDescription = stringResource(id = R.string.btn_back))
            }

            FloatingActionButton(onClick = {navController.navigateUp()}, shape = CircleShape,){

                Icon(Icons.Filled.FlashlightOn, contentDescription = stringResource(id = R.string.btn_flashlight))
            }
        }
    }
}