package it.polito.did.g2.shopdrop.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import it.polito.did.g2.shopdrop.ui.cst.common.ArrowRt

@Composable
fun ProfileItemList(label : String, onClick : ()->Unit, badge: Boolean = false){
    Card(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .height(64.dp)
            .clickable(onClick = onClick)
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ){
            Row(){
                Text(text = label)
                if(badge) Icon(Icons.Filled.Circle, null, tint = Color.Red, modifier = Modifier.padding(horizontal = 8.dp))
            }

            ArrowRt()
        }
    }
}