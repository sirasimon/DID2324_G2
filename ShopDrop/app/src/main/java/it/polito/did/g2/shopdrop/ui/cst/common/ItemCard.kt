package it.polito.did.g2.shopdrop.ui.cst.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

//TODO: DA CANCELLARE

@Composable
fun ItemCard(onClick: () -> Unit){
    Card(modifier = Modifier
        .size(width = 160.dp, height = 200.dp)
        .padding(8.dp)
        .clickable(onClick = onClick)) {
        Text(text = "test")
    }
}