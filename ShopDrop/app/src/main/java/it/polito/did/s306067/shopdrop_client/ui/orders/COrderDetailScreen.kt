package it.polito.did.s306067.shopdrop_client.ui.orders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import it.polito.did.s306067.shopdrop_client.ui.common.BottomBar
import it.polito.did.s306067.shopdrop_client.ui.common.ScanButton
import it.polito.did.s306067.shopdrop_client.ui.common.TabScreen
import it.polito.did.s306067.shopdrop_client.ui.common.TopBar

data class UpdateInfo(val stateName : String, val time : String, val state : Boolean = false)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun COrderDetailScreen(navController : NavController){
    var currentTab = TabScreen.HOME

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    val updateList = listOf(
        UpdateInfo("Ordine confermato", "19:15",true),
        UpdateInfo("Spedito", "19:22",true),
        UpdateInfo("In transito", "19:27",true),
        UpdateInfo("Consegnato", "In corso",false)
    )

    Scaffold(
        topBar = { TopBar(currentTab, "Detail", scrollBehavior = scrollBehavior) },
        bottomBar = { BottomBar(currentTab, navController) },
        floatingActionButton = { ScanButton(true) },            //TODO: inviare valore true o false in base a disponibilità al ritiro
        floatingActionButtonPosition = FabPosition.End
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier
                .background(Color.Cyan)
                .fillMaxSize()
            ) {

                //  ULTIMI STATI DELL'ORDINE
                val updateScrollState = rememberScrollState()
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .verticalScroll(updateScrollState)
                    .graphicsLayer { alpha = .99f }
                    .drawWithContent {
                        val colors = listOf(Color.Transparent, Color.Black)
                        drawContent()
                        drawRect(
                            brush = Brush.verticalGradient(colors),
                            blendMode = BlendMode.DstIn
                        )
                    }
                ){
                    updateList.forEachIndexed{index, it->
                        UpdateListItem(info = it)
                        if(index == updateList.size-1)
                            LaunchedEffect(true){
                                updateScrollState.scrollTo(updateScrollState.maxValue)
                            }
                    }
                }

                /*
                val topFade = Brush.verticalGradient(0f to Color.Transparent, 0.3f to Color.Red)
                Box(modifier = Modifier
                    .fadingEdge(topFade)
                    .background(Color.Blue)
                    .size(200.dp))
                */

                //  OGGETTI ORDINATI
                Text("Oggetti ordinati")
                Card(Modifier.fillMaxWidth().padding(8.dp).height(60.dp)){
                    Text("[Espandibile o meno, con la lista degli ordini]")
                }

                Text("Luogo di ritiro")
                Card(Modifier.fillMaxWidth().padding(8.dp).height(60.dp)){
                    Text("[Indirizzo del locker]")
                }
            }
        }
    }
}

@Composable
fun UpdateListItem(info : UpdateInfo){
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
        .fillMaxWidth()
        .height(64.dp)){
        Row(){
            if(info.state)
                Icon(Icons.Filled.CheckCircle, "Done", tint = Color.Green)
            else
                Icon(Icons.Outlined.CheckCircle, "Done", tint = Color.Gray)
            Text(text = info.stateName)
        }
        Text(text = info.time)
    }
}