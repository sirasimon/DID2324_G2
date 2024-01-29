package it.polito.did.g2.shopdrop.ui.cst.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.polito.did.g2.shopdrop.MainViewModel
import it.polito.did.g2.shopdrop.R
import it.polito.did.g2.shopdrop.data.StoreItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmSheet(
    item : StoreItem?,
    viewModel: MainViewModel,
    onDismiss: () -> Unit,
    bottomSheetState: SheetState,
    bottomSheetScope: CoroutineScope
) {
    val quantity = mutableIntStateOf(viewModel.cart.value?.get(item?.name) ?: 0)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = bottomSheetState,
        dragHandle = { Text(text = "Aggiungi", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 8.dp)) }
    ) {
        // SHEET CONTENT

        // Product name
        Text(
            text = item?.name?.capitalize()?:"[NULL]",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            lineHeight = 24.sp,
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        // Product price
        Text(
            text = item?.price.toString()+" €",
            fontWeight = FontWeight.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(Modifier.height(16.dp))

        Card(shape = CircleShape, modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .height(48.dp)){
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize()
            ){
                Text(
                    text = stringResource(id = R.string.txt_quantity).capitalize(),
                    modifier = Modifier
                        .padding(horizontal = 16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)){
                    if(quantity.value!=0) {
                        IconButton(
                            onClick = { if (quantity.value > 0) quantity.value-- },
                            enabled = quantity.value > 0
                        ) {
                            Image(
                                painter = painterResource(R.drawable.btn_sub),
                                contentDescription = "Decrease quantity"
                            )
                        }
                    }
                    Text("${quantity.value}")
                    IconButton(onClick = { quantity.value++ }) {
                        Image(painter = painterResource(R.drawable.btn_add), contentDescription = "increase quantity")
                    }
                }
            }
        }

        Text("[Breve descrizione]", Modifier.padding(horizontal = 16.dp))

        Button(
            onClick = {
                viewModel.modifyCart(item, quantity.value)
                bottomSheetScope.launch { bottomSheetState.hide() }
                    .invokeOnCompletion {
                        if(!bottomSheetState.isVisible){
                            onDismiss()
                        }
                    }
                      },
            modifier = Modifier
                .padding(vertical = 24.dp, horizontal = 64.dp)
                .fillMaxWidth()
        ){
                Text( String.format("Aggiungi per %.2f €", (quantity.value.toFloat())*(item?.price ?: 0f)) )
        }



        Spacer(modifier = Modifier.height(16.dp))

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
