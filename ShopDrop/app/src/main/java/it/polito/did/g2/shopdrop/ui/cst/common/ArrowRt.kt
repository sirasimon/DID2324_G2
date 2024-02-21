package it.polito.did.g2.shopdrop.ui.cst.common

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import it.polito.did.g2.shopdrop.R

@Composable
fun ArrowRt(){
    Image(painter = painterResource(id = R.drawable.btn_back), modifier = Modifier.graphicsLayer { rotationY = 180f }, contentDescription = null)
}