package it.polito.did.g2.shopdrop.ui.cst.cart

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import it.polito.did.g2.shopdrop.MainViewModel
import it.polito.did.g2.shopdrop.R
import it.polito.did.g2.shopdrop.navigation.Screens
import it.polito.did.g2.shopdrop.ui.cst.common.TopBar
import it.polito.did.g2.shopdrop.ui.cst.common.WIPMessage
import it.polito.did.g2.shopdrop.ui.cst.common.performDevMsg

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CSTCheckoutScreen(navController: NavController, viewModel: MainViewModel) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    // SNACKBAR
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    Scaffold(
        topBar = { TopBar(navController, stringResource(id = R.string.title_order_summary), scrollBehavior) },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        floatingActionButton = { FabProceed(navController, viewModel) },
        floatingActionButtonPosition = FabPosition.Center,
        snackbarHost = { WIPMessage(snackbarHostState) }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(Modifier.fillMaxWidth()) {
                    if(!viewModel.cart.value.isNullOrEmpty()){
                        viewModel.cart.value!!.forEach {
                            val subTot : Float =
                                viewModel.storeItems.value?.filter { si -> si.name == it.key }?.map{ si -> si.price}?.sum()?.times(it.value) ?: 0f

                            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()){
                                Row(){
                                    Text(text = it.key.capitalize(), maxLines = 1)

                                    if(it.value!=1)
                                        Text(text = "x${it.value}", fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(horizontal = 8.dp))
                                }
                                Text(text = "${String.format("%.2f", subTot)} €", textAlign = TextAlign.End, modifier = Modifier.fillMaxWidth(.3f))
                            }
                        }
                    }else{
                        Text(text = stringResource(id = R.string.txt_something_gone_wrong))   //TODO
                    }
                }
                Spacer(Modifier.height(16.dp))
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.background(Color.LightGray, RoundedCornerShape(16.dp)).fillMaxWidth().height(200.dp).clickable { navController.navigate(Screens.CstLockerSelector.route) }
                ){
                    Text("[Locker selector]")
                }
                
                Card(
                    Modifier
                        .fillMaxWidth()
                ){
                    ListItem(
                        modifier = Modifier.height(70.dp).clickable { performDevMsg(scope, snackbarHostState, context) },
                        headlineContent = {
                            Column {
                                Text(stringResource(id = R.string.title_payment_method).capitalize())
                                
                                Row(){
                                    Text("**** 4187")
                                    Image(painter= painterResource(id = R.drawable.mastercard), contentDescription = "mastercard logo", Modifier.height(24.dp).padding(horizontal = 8.dp))
                                }
                            }
                        },
                        trailingContent = {
                            Image(painter = painterResource(id = R.drawable.btn_back), modifier = Modifier.graphicsLayer { rotationY = 180f }, contentDescription = null)
                        }
                    )
                }

                // TOTAL PRICE AREA

                Column(
                    Modifier
                        .fillMaxWidth()
                ) {
                    Spacer(Modifier.height(24.dp))

                    PriceItemList(
                        stringResource(R.string.price_subtotal).capitalize(),
                        String.format("%.2f €", viewModel.subtot.value)
                    )  //TODO
                    PriceItemList(
                        stringResource(R.string.price_shipment).capitalize(),
                        String.format("%.2f €", viewModel.shipmentFee)
                    )
                    PriceItemList(
                        stringResource(R.string.price_service).capitalize(),
                        String.format("%.2f €", viewModel.serviceFee)
                    )
                    PriceListTotal(
                        stringResource(R.string.price_total).uppercase(),
                        String.format(
                            "%.2f €",
                            (viewModel.subtot.value
                                ?: 0f) + viewModel.shipmentFee + viewModel.serviceFee
                        )
                    )

                    Spacer(Modifier.height(80.dp))
                }
            }
        }
    }
}

@Composable
private fun FabProceed(navController: NavController, viewModel: MainViewModel) {
    ExtendedFloatingActionButton(
        onClick = { navController.navigate(Screens.CstOrderSent.route) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
    ) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()){
            Text(text="${String.format("%.2f", viewModel.getCartTotal())} €")
            Text(stringResource(id = R.string.btn_order_now).capitalize())
        }
    }
}