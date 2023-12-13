package it.polito.did.s306067.shopdrop_client.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import it.polito.did.s306067.shopdrop_client.R
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmSheet(
    onDismiss: () -> Unit,
    bottomSheetState: SheetState,
    bottomSheetScope: CoroutineScope
) {
    var count = mutableIntStateOf(0)
    var price = 11.35f

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = bottomSheetState,
        dragHandle = { Text("Aggiungi", Modifier.padding(vertical = 8.dp)) }
    ) {
        // Sheet content

        Text("[Nome prodotto]", Modifier.padding(horizontal = 16.dp))
        Text("$price", Modifier.padding(horizontal = 16.dp))

        Card(shape = CircleShape, modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .height(48.dp)){
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxSize()){
                Text(text = "Quantità", modifier = Modifier
                    .background(Color.Cyan)
                    .padding(horizontal = 16.dp))
                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(Color.Red)
                        .padding(horizontal = 16.dp)){
                    IconButton(onClick = { if(count.value>0) count.value-- }) {
                        Icon(painter = painterResource(R.drawable.outline_do_disturb_on_24), contentDescription = null)
                    }
                    Text("${count.value}")
                    IconButton(onClick = { count.value++ }) {
                        Icon(painter = painterResource(R.drawable.round_add_circle_outline_24), contentDescription = null)
                    }
                }
            }
        }

        Text("[Breve descrizione]", Modifier.padding(horizontal = 16.dp))

        Button(onClick = {/*TODO*/}, modifier = Modifier
            .padding(vertical = 24.dp, horizontal = 64.dp)
            .fillMaxWidth()){
            Text(String.format("Aggiungi per %.2f €", price*count.value))
        }

        /*
        Button(modifier = Modifier.padding(horizontal = 16.dp),
            onClick = {
            bottomSheetScope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                if (!bottomSheetState.isVisible)
                    onDismiss
            }
        }) {
            Text("Hide bottom sheet")
        }
        */
    }
}