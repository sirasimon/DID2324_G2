package it.polito.did.g2.shopdrop.ui.common

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.util.Size
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material.icons.filled.QrCodeScanner
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import it.polito.did.g2.shopdrop.QrCodeAnalyzer
import it.polito.did.g2.shopdrop.R

@Composable
fun CameraBase(message: String, checkValue: String?, onBack: ()->Unit, action: (String) -> Unit){
    var code by remember{ mutableStateOf("") }

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

    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember {
        ProcessCameraProvider.getInstance(context)
    }

    var isTorchOn by remember{ mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Blue)
    ) {
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
                        previewView.height
                    )
                )
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            imageAnalysis.setAnalyzer(
                ContextCompat.getMainExecutor(it),
                QrCodeAnalyzer { result ->

                    Log.i("CAMERA", "CAMERA HA LETTO $result")

                    code = result

                    Log.i("CAMERA", "CODE ASSUME VALORE DI RESULT ($code)")

                    /*
                    //TODO Forse è qui il pezzo dove intervenire
                    if (checkValue!=null && code == checkValue) {
                        Log.i("CAMERA", "CODE È UGUALE A CHECKVALUE QUINDI ESEGUO L'AZIONE")
                        action(code)
                    }
                    */

                    /*
                    if(checkValue==null)    action(code)
                    else if(code == checkValue) action(code)
                    */

                    if(checkValue==null || checkValue==code) {
                        Log.i("CAMERA", "CODE È UGUALE A CHECKVALUE QUINDI ESEGUO L'AZIONE")
                        action(code)
                    }

                }
            )
            try {
                cameraProviderFuture.get().bindToLifecycle(
                    lifecycleOwner,
                    selector,
                    preview,
                    imageAnalysis
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
            previewView
        })

        // BOTTONI SUPERIORI
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ){
            FloatingActionButton(
                shape = CircleShape,
                onClick = onBack
            ){
                Image(
                    painter = painterResource(id = R.drawable.btn_back),
                    contentDescription = "back"
                )
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
                }
            ){
                Icon(Icons.Filled.FlashlightOn, contentDescription = stringResource(id = R.string.btn_flashlight))
            }
        }

        Row(
            Modifier.align(Alignment.TopCenter)
        ){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Spacer(Modifier.height(100.dp))
                Text(
                    message,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    modifier = Modifier.width(300.dp)
                )
                Spacer(Modifier.height(16.dp))
                Icon(
                    Icons.Filled.QrCodeScanner,
                    tint = Color.White,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp)
                )
            }
        }
    }
}