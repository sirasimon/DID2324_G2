package it.polito.did.g2.shopdrop.ui.cst.orders

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.ShoppingBasket
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import it.polito.did.g2.shopdrop.MainViewModel
import it.polito.did.g2.shopdrop.R
import it.polito.did.g2.shopdrop.data.OrderStateName
import it.polito.did.g2.shopdrop.data.TabScreen
import it.polito.did.g2.shopdrop.navigation.Screens
import it.polito.did.g2.shopdrop.ui.cst.common.BottomBar
import it.polito.did.g2.shopdrop.ui.cst.common.ScanButton

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CSTOrderDetail(navController : NavController, viewModel : MainViewModel, orderID : String?){
    val currentTab = TabScreen.PROFILE

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    val order = viewModel.ordersList.value?.find { it.id == orderID }

    val lastState : OrderStateName? = order?.stateList?.map { it.state }?.sortedBy { it.ordinal }?.last()

    /*
    Text("TODO!!!")
    Button(
        onClick = { navController.navigate(Screens.CstCamera.route+"/$orderID") }
    ){
        Text("CLICK TO COLLECT")
    }
    */

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        //modifier = Modifier.fillMaxWidth()
                    ){
                        Text(
                            text = stringResource(id = R.string.order_dated).capitalize() + " " + viewModel.getDateString(order?.stateList?.find{it.state==OrderStateName.CREATED}?.timestamp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.headlineSmall,
                        )
                        Text("#$orderID", fontSize = 10.sp)
                    }
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
        bottomBar = { BottomBar(currentTab, navController, viewModel) },
        floatingActionButton = {
            when (lastState) {
                OrderStateName.CREATED -> CancelButton {
                    if (orderID != null) {
                        viewModel.cancelOrder(orderID)
                        navController.navigateUp()
                        navController.navigate(Screens.CstOrderDetail.route+"$orderID")
                    }
                }
                OrderStateName.AVAILABLE -> ScanButton(true, navController, orderID)
                OrderStateName.CANCELLED -> CancelledLabel()
                else -> null
            }
        },
        floatingActionButtonPosition =
            when (lastState) {
                OrderStateName.CREATED -> FabPosition.Center
                OrderStateName.CANCELLED -> FabPosition.Center
                else -> FabPosition.End
            }
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
                    OrderStateList(msg = stringResource(id = R.string.order_state_created).capitalize(), time = if(i<timestamps.size) viewModel.getTimeString(timestamps[i]) else null)
                    PathLine(done = i++<timestamps.size)
                    if(lastState!=OrderStateName.CANCELLED){
                        OrderStateList(msg = stringResource(id = R.string.order_state_received).capitalize(), time = if(i<timestamps.size) viewModel.getTimeString(timestamps[i]) else null)
                        PathLine(done = i++<timestamps.size)
                        OrderStateList(msg = stringResource(id = R.string.order_state_carried).capitalize(), time = if(i<timestamps.size) viewModel.getTimeString(timestamps[i]) else null)
                        PathLine(done = i++<timestamps.size)
                        OrderStateList(msg = stringResource(id = R.string.order_state_available).capitalize(), time = if(i<timestamps.size) viewModel.getTimeString(timestamps[i]) else null)
                        PathLine(done = i++<timestamps.size)
                        OrderStateList(msg = stringResource(id = R.string.order_state_collected).capitalize(), time = if(i<timestamps.size) viewModel.getTimeString(timestamps[i]) else null)
                    }else{
                        OrderStateList(msg = stringResource(id = R.string.order_state_cancelled).capitalize(), time = if(i<timestamps.size) viewModel.getTimeString(timestamps[i]) else null)
                    }
                }

                Spacer(Modifier.height(32.dp))

                //LUOGO DI RITIRO
                Text(text = stringResource(id = R.string.title_collection_point).capitalize(), style = MaterialTheme.typography.titleSmall)
                Card(
                    Modifier
                        .fillMaxWidth()
                ){

                    Column(Modifier.padding(8.dp)){
                        Text(
                            viewModel.lockersList.value?.find { it.id == order?.lockerID }?.name
                                ?: "[LOCKER NAME]",
                            fontWeight = FontWeight.SemiBold
                        )

                        Row() {
                            Text(
                                viewModel.lockersList.value?.find { it.id == order?.lockerID }?.address
                                    ?: "[LOCKER ADDRESS]",
                                maxLines = 1,
                                softWrap = true
                            )
                        }
                    }
                }

                Spacer(Modifier.height(32.dp))

                //  OGGETTI ORDINATI
                var expanded by remember { mutableStateOf(false) }

                val fontHeight = 16.sp
                val lineHeight = 25.dp
                val baseHight = 50
                val totalListHeight = lineHeight.value * (order?.items?.size?:0) + baseHight + 16

                Text(stringResource(id = R.string.items_ordered).capitalize(), style = MaterialTheme.typography.titleSmall)

                Card(Modifier.fillMaxWidth()){
                    Column(
                        verticalArrangement = Arrangement.Top,
                        modifier = Modifier
                            //.background(Color.Cyan)
                            .animateContentSize()   //deve andare prima dell'altezza
                            .height(if (expanded) totalListHeight.dp else baseHight.dp)
                            .fillMaxWidth()
                    ){
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .height(baseHight.dp)
                                .fillMaxWidth()
                                //.background(Color.Green)
                                .padding(8.dp)
                                .clickable { expanded = !expanded }
                        ){
                            Row {
                                Icon(Icons.Filled.ShoppingBasket, contentDescription = null)
                                Text("${order?.items?.size} ${stringResource(id = if(order?.items?.size==1) R.string.txt_item else R.string.txt_items)}", Modifier.padding(horizontal = 8.dp))
                            }

                            TextButton(onClick = { expanded = ! expanded }) {
                                Text(if(expanded) stringResource(id = R.string.hide).capitalize() else stringResource(id = R.string.show).capitalize())
                            }
                        }

                        Column(
                            verticalArrangement = Arrangement.Top,
                            modifier = Modifier
                                .fillMaxWidth()
                                //.background(Color.Red)
                                .padding(8.dp)
                        ){
                            order?.items?.forEach {
                                val subTot : Float = viewModel.prodsList.value?.filter { si -> si.name == it.key }?.map{ si -> si.price}?.sum()?.times(it.value) ?: 0f
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(lineHeight)
                                ){
                                    Row{
                                        Text(text = it.key.capitalize(), maxLines = 1, softWrap = true, fontSize = fontHeight)

                                        if(it.value!=1)
                                            Text(text = "x${it.value}", fontWeight = FontWeight.SemiBold, fontSize = fontHeight, modifier = Modifier.padding(horizontal = 8.dp))
                                    }

                                    Text(text = "${String.format("%.2f", subTot)} €", textAlign = TextAlign.End, fontSize = fontHeight, modifier = Modifier.fillMaxWidth(.3f))
                                }
                            }
                        }
                    }
                }
            }
        }

        if(lastState==OrderStateName.CANCELLED){
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color(0x80000000)))
        }

    }
}

@Composable
fun CancelButton(onClick: () -> Unit){
    TextButton(onClick = onClick) {
        Text(text = stringResource(id = R.string.btn_cancel_order).capitalize(), color = Color.Red, fontSize = 18.sp)
    }
}

@Composable
fun CancelledLabel(){
    TextButton(
        onClick = {},
        enabled = false
    ) {
        Text(text = stringResource(id = R.string.btn_cancelled_order).capitalize(), color = Color.Red, fontSize = 18.sp)
    }
}

@Composable
fun PathLine(done : Boolean){
    VerticalDivider(color = if(done) Color.Green else Color.Gray, modifier = Modifier
        .height(36.dp)
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
        Text(text = time?: stringResource(id = R.string.in_progress), fontStyle = if(time!=null) FontStyle.Normal else FontStyle.Italic)
    }
}