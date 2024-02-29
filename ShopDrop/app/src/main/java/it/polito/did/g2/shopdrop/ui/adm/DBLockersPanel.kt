package it.polito.did.g2.shopdrop.ui.adm

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import it.polito.did.g2.shopdrop.MainViewModel

@Composable
fun DBLockersPanel(viewModel: MainViewModel, navController: NavController) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .verticalScroll(rememberScrollState())
    ){
        viewModel.lockersList.observeAsState().value?.forEach {
            Card(
                Modifier.fillMaxWidth()
            ){
                Column(
                    Modifier.fillMaxWidth().padding(16.dp)
                ){
                    Row( Modifier.fillMaxWidth()) {
                        Text("${it.id} – ${it.name?.uppercase()}")
                    }

                    Text(it.address)

                    HorizontalDivider(thickness = 2.dp, modifier = Modifier.padding(vertical = 8.dp))

                    var spacing = 90.dp

                    Row(modifier = Modifier.fillMaxWidth()){
                        Text("Online", textAlign = TextAlign.Center, modifier = Modifier.width(spacing))
                        Text("Working", textAlign = TextAlign.Center, modifier = Modifier.width(spacing))
                        Text("Empty", textAlign = TextAlign.Center, modifier = Modifier.width(spacing))
                        Text("Full", textAlign = TextAlign.Center, modifier = Modifier.width(spacing))
                    }
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()){
                        //Text("${it.isOnline}", modifier = Modifier.width(spacing))
                        RadioButton(selected = it.isOnline, onClick = { /*TODO*/ }, modifier = Modifier.width(spacing))
                        //Text("${it.isWorking}", modifier = Modifier.width(spacing))
                        RadioButton(selected = it.isWorking, onClick = { /*TODO*/ }, modifier = Modifier.width(spacing))
                        //Text("${it.isEmpty}", modifier = Modifier.width(spacing))
                        RadioButton(selected = it.isEmpty, onClick = { /*TODO*/ }, modifier = Modifier.width(spacing))
                        //Text("${it.isFull}", modifier = Modifier.width(spacing))
                        RadioButton(selected = it.isFull, onClick = { /*TODO*/ }, modifier = Modifier.width(spacing))
                    }

                    HorizontalDivider(thickness = 2.dp, modifier = Modifier.padding(vertical = 8.dp))
                    Text("Total compartments: ${it.compartments?.size}")

                    spacing = 50.dp
                    Row(modifier = Modifier.fillMaxWidth()){
                        Text("#", modifier = Modifier.width(20.dp))
                        Text("Wkg", textAlign = TextAlign.Center, modifier = Modifier.width(spacing))
                        Text("Ept", textAlign = TextAlign.Center, modifier = Modifier.width(spacing))
                        Text("Opn", textAlign = TextAlign.Center, modifier = Modifier.width(spacing))
                        Text("Avb", textAlign = TextAlign.Center, modifier = Modifier.width(spacing))
                        Text("oID", modifier = Modifier.width(spacing))
                    }

                    it.compartments?.forEach {
                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
                            Text("${it.id}", modifier = Modifier.width(20.dp))
                            RadioButton(selected = it.isWorking, onClick = { /*TODO*/ }, modifier = Modifier.width(spacing))
                            RadioButton(selected = it.isEmpty, onClick = { /*TODO*/ }, modifier = Modifier.width(spacing))
                            RadioButton(selected = it.isOpen, onClick = { /*TODO*/ }, modifier = Modifier.width(spacing))
                            RadioButton(selected = it.isAvailable, onClick = { /*TODO*/ }, modifier = Modifier.width(spacing))
                            Text("${it.orderID}")
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}