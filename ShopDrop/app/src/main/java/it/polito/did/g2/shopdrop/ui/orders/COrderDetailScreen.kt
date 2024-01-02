package it.polito.did.g2.shopdrop.ui.orders

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import it.polito.did.g2.shopdrop.MainViewModel
import it.polito.did.g2.shopdrop.R
import it.polito.did.g2.shopdrop.data.Order
import it.polito.did.g2.shopdrop.data.TabScreen
import it.polito.did.g2.shopdrop.ui.common.BottomBar
import it.polito.did.g2.shopdrop.ui.common.ScanButton
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun COrderDetailScreen(navController : NavController, vm : MainViewModel){
    var currentTab = TabScreen.HOME

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    val order : Order? = vm.pendingOrdersList.value?.find{it.id==vm.targetOrderID}

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.order_dated).capitalize()+" "+ order?.stateList?.get(0)?.timestamp?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                },
                actions = {
                    if(currentTab== TabScreen.CART){
                        IconButton(onClick = { /* do something */ }) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "Localized description"
                            )
                        }
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
        bottomBar = { BottomBar(currentTab, navController) },
        floatingActionButton = { if(order?.stateList?.size==4) ScanButton(true, navController) else if(order?.stateList?.size==1) CancelButton({/*TODO*/})},
        floatingActionButtonPosition = if(order?.stateList?.size==1) FabPosition.Center else FabPosition.End
    ) { paddingValues ->

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {

            Column(modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
            ) {

                //  ULTIMI STATI DELL'ORDINE
                val timestamps = order?.stateList?.map{it.timestamp} ?: listOf()
                var i = 0

                Column(modifier = Modifier.fillMaxWidth()
                ){
                    OrderStateList(msg = stringResource(id = R.string.order_state_created).capitalize(), time = if(i<timestamps.size) timestamps[i].format(DateTimeFormatter.ofPattern("HH:mm")) else null)
                    PathLine(done = i++<timestamps.size)
                    OrderStateList(msg = stringResource(id = R.string.order_state_received).capitalize(), time = if(i<timestamps.size) timestamps[i].format(DateTimeFormatter.ofPattern("HH:mm")) else null)
                    PathLine(done = i++<timestamps.size)
                    OrderStateList(msg = stringResource(id = R.string.order_state_carried).capitalize(), time = if(i<timestamps.size) timestamps[i].format(DateTimeFormatter.ofPattern("HH:mm")) else null)
                    PathLine(done = i++<timestamps.size)
                    OrderStateList(msg = stringResource(id = R.string.order_state_available).capitalize(), time = if(i<timestamps.size) timestamps[i].format(DateTimeFormatter.ofPattern("HH:mm")) else null)
                    PathLine(done = i++<timestamps.size)
                    OrderStateList(msg = stringResource(id = R.string.order_state_collected).capitalize(), time = if(i<timestamps.size) timestamps[i].format(DateTimeFormatter.ofPattern("HH:mm")) else null)
                }

                Spacer(Modifier.height(32.dp))

                //LUOGO DI RITIRO
                Text(text = stringResource(id = R.string.title_collection_point).capitalize(), style = MaterialTheme.typography.titleSmall)
                Card(
                    Modifier
                        .fillMaxWidth()
                        .height(60.dp)){
                    Text("[Indirizzo del locker]")
                }

                Spacer(Modifier.height(32.dp))

                //  OGGETTI ORDINATI
                Text("Oggetti ordinati", style = MaterialTheme.typography.titleSmall)
                Card(
                    Modifier
                        .fillMaxWidth()
                        .height(60.dp)){
                    Text("[Espandibile o meno, con la lista degli ordini]")
                }
            }
        }
    }
}

@Composable
fun CancelButton(onClick: () -> Unit){
    TextButton(onClick = onClick) {
        Text(text = stringResource(id = R.string.btn_cancel_order), color = Color.Red, fontSize = 18.sp)
    }
}

@Composable
fun PathLine(done : Boolean){
    VerticalDivider(color = if(done) Color.Green else Color.Gray, modifier = Modifier
        .height(18.dp)
        .padding(horizontal = 10.dp))
}

@Composable
fun OrderStateList(msg : String, time : String?){
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
        .fillMaxWidth()
    ){
        Row(){
            if(time!=null)
                Icon(Icons.Filled.CheckCircle, "Done", tint = Color.Green)
            else
                Icon(Icons.Outlined.CheckCircle, "Done", tint = Color.Gray)
            Text(text = msg)
        }
        Text(text = time?:"")
    }
}

//UTILI e vecchi
//TODO Utile per la spesa sotto, qui non ha bisogno di essere scrollabile
/*              val updateScrollState = rememberScrollState()
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
                        OrderStateList(info = it)
                        if(index == updateList.size-1)
                            LaunchedEffect(true){
                                updateScrollState.scrollTo(updateScrollState.maxValue)
                            }
                    }
                }
*/

/*
                val topFade = Brush.verticalGradient(0f to Color.Transparent, 0.3f to Color.Red)
                Box(modifier = Modifier
                    .fadingEdge(topFade)
                    .background(Color.Blue)
                    .size(200.dp))
                */