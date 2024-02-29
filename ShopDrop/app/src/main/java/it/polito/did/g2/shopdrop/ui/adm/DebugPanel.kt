package it.polito.did.g2.shopdrop.ui.adm

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesomeMosaic
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import it.polito.did.g2.shopdrop.MainViewModel
import it.polito.did.g2.shopdrop.navigation.Screens

@Composable
fun DebugPanel(viewModel: MainViewModel, navController: NavController) {
    var dialogOpen by remember { mutableStateOf(false) }
    var onClick : (()->Unit) = {}

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            onClick = {
                onClick = { viewModel.debugDBReset(MainViewModel.db_reset.ORDERS) ; dialogOpen = false}
                dialogOpen = true
            }) {
            Text("RESET ORDERS DB")
        }

        Button(
            onClick = {
                navController.navigate(Screens.AdmCameraTest.route)
            }) {
            Icon(Icons.Filled.CameraAlt , contentDescription = null)
            Text("TEST CAMERA", Modifier.padding(horizontal = 8.dp))
            Icon(Icons.Filled.CameraAlt , contentDescription = null)
        }

        Button(
            onClick = {
                navController.navigate(Screens.AdmCollectionTest.route)
            }) {
            Icon(Icons.Filled.AutoAwesomeMosaic , contentDescription = null)
            Text("TEST COLLECTION PROCEDURE")
            Icon(Icons.Filled.AutoAwesomeMosaic , contentDescription = null)
        }

        Spacer(Modifier.height(32.dp))
        Text("v. ${viewModel.bldNum}", fontSize = 10.sp, color = Color.Gray)


        if(dialogOpen){
            Dialog(onDismissRequest = { dialogOpen=false; onClick = {} }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(375.dp)
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                ){
                    Text(text = "ARE YOU SURE???")
                    Row {
                        Button(onClick = { dialogOpen=false; onClick = {} }) {
                            Text(text = "CANCEL")
                        }

                        Button(onClick = onClick) {
                            Text(text = "PROCEED")
                        }
                    }
                }
            }
        }
    }
}