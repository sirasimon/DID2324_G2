package it.polito.did.g2.shopdrop.ui.unlocker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.QuestionMark
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import it.polito.did.g2.shopdrop.MainViewModel
import it.polito.did.g2.shopdrop.data.CollectionStep

@Composable
fun OpeningScreens(navController: NavController, viewModel: MainViewModel) {

    var topText = "[TOP TEXT]"
    var stepImage = "[STEP IMAGE]"
    var btmText = "[BOTTOM TEXT]"
    var timer = "XX:XX"
    var progressBarColor = Color.Green


    when(viewModel.collectionStep.value){
        CollectionStep.OPEN_OK -> {

        }
        CollectionStep.OPEN_MID -> {
            /* TODO */
        }
        CollectionStep.OPEN_LAST -> {
            /* TODO */
        }
        CollectionStep.TIMEOUT -> {
            /* TODO */
        }
        else ->{
            /*TODO*/
        }
    }

    Column(Modifier.fillMaxWidth()){
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ){
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Rounded.QuestionMark, contentDescription = null)
            }
        }
        Text(text = topText)
        Text(text = stepImage) //TODO: convertire in immagine
        Text(text = btmText)
        Text(text = timer)
        //PROGRESS BAR HERE
    }
}