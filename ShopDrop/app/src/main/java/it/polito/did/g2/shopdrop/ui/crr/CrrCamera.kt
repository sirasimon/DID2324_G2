package it.polito.did.g2.shopdrop.ui.crr

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import it.polito.did.g2.shopdrop.MainViewModel
import it.polito.did.g2.shopdrop.R
import it.polito.did.g2.shopdrop.navigation.Screens
import it.polito.did.g2.shopdrop.ui.common.CameraBase

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CrrCamera(navController : NavController, viewModel: MainViewModel, orderID : String?){

    val context = LocalContext.current

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted -> hasCameraPermission=granted }
    )

    LaunchedEffect(key1 = true){
        launcher.launch(Manifest.permission.CAMERA)
    }


    Box(){
        CameraBase(
            message = stringResource(id = R.string.crr_scan_parcel),
            checkValue = orderID?:"",
            { navController.navigateUp() }
        ) {
            navController.navigate(Screens.CrrCollectedScreen.route+"/$orderID")
            viewModel.crrHasCollected(orderID!!)
        }
    }
}