package it.polito.did.g2.shopdrop.ui.camera

import android.Manifest
import android.content.pm.PackageManager
import android.util.Size
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import it.polito.did.g2.shopdrop.MainViewModel
import it.polito.did.g2.shopdrop.QrCodeAnalyzer
import it.polito.did.g2.shopdrop.R
import it.polito.did.g2.shopdrop.navigation.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraScreen(navController : NavController, viewModel: MainViewModel, orderID : String){
    var code by remember{ mutableStateOf("") }
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember {
        ProcessCameraProvider.getInstance(context)
    }

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

    // TORCIA
    var isTorchOn by remember{ mutableStateOf(false) }


    Box(){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Blue)
        ){
            //Text(text = "CAMERA SCREEN", modifier = Modifier.align(Alignment.Center))
            AndroidView(modifier = Modifier.fillMaxSize(), factory = {
                val previewView = PreviewView(it)
                val preview = Preview.Builder().build()
                val selector = CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build()

                preview.setSurfaceProvider(previewView.surfaceProvider)

                val imageAnalysis = ImageAnalysis.Builder()
                    .setTargetResolution(
                        Size(
                            previewView.width,
                            previewView.height)
                    )
                    .setBackpressureStrategy(STRATEGY_KEEP_ONLY_LATEST)
                    .build()

                imageAnalysis.setAnalyzer(
                    ContextCompat.getMainExecutor(it),
                    QrCodeAnalyzer{result ->
                        code = result

                        //TODO Forse è qui il pezzo dove intervenire
                        if(viewModel.cstStartsCollection(code, orderID))
                            //navController.navigate(Screens.CstCollectionScreen.route+"/$orderID")
                            navController.navigate(Screens.CstCollection.route+"/$orderID")
                    }
                )
                try{
                    cameraProviderFuture.get().bindToLifecycle(
                        lifecycleOwner,
                        selector,
                        preview,
                        imageAnalysis
                    )
                }catch(e: Exception){
                    e.printStackTrace()
                }
                previewView
            })

            //DEBUG
            Text(text= code, modifier = Modifier
                .background(Color.White)
                .fillMaxWidth()
                .align(Alignment.Center))
        }

        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)){
            FloatingActionButton(
                shape = CircleShape,
                onClick = {navController.navigateUp()}
            ){
                Icon(Icons.Filled.ArrowBackIos, contentDescription = stringResource(id = R.string.btn_back))
            }

            FloatingActionButton(
                shape = CircleShape,
                onClick = {
                    /*
                    val cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager

                    try {
                        val cameraId = cameraManager.cameraIdList[0] // Assume che ci sia almeno una fotocamera

                        if (isTorchOn) {
                            // Spegni la torcia
                            cameraManager.setTorchMode(cameraId, false)
                            isTorchOn = false
                        } else {
                            // Accendi la torcia
                            cameraManager.setTorchMode(cameraId, true)
                            isTorchOn = true
                        }

                    } catch (e: CameraAccessException) {
                        e.printStackTrace()
                    }

                     */
                }){

                Icon(Icons.Filled.FlashlightOn, contentDescription = stringResource(id = R.string.btn_flashlight))
            }
        }
    }
}