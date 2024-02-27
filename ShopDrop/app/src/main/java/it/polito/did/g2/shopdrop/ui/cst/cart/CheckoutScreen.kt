package it.polito.did.g2.shopdrop.ui.cst.cart

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FmdGood
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import it.polito.did.g2.shopdrop.MainViewModel
import it.polito.did.g2.shopdrop.R
import it.polito.did.g2.shopdrop.data.Fees
import it.polito.did.g2.shopdrop.navigation.Screens
import it.polito.did.g2.shopdrop.ui.cst.common.ArrowRt
import it.polito.did.g2.shopdrop.ui.cst.common.TopBar
import it.polito.did.g2.shopdrop.ui.cst.common.WIPMessage
import it.polito.did.g2.shopdrop.ui.cst.common.performDevMsg
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CSTCheckoutScreen(navController: NavController, viewModel: MainViewModel) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    // SNACKBAR
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    // RIEPILOGO OGGETTI
    var expanded by remember { mutableStateOf(false) }

    @RequiresApi(Build.VERSION_CODES.O)
    fun placeOrder(){
        viewModel.placeOrder()
        navController.navigate(Screens.CstOrderSent.route)
    }

    Scaffold(
        topBar = { TopBar({navController.navigateUp()}, stringResource(id = R.string.title_order_summary), scrollBehavior) },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        floatingActionButton = { FabProceed(viewModel.getCartTotal()) { placeOrder() } },
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
                    //.background(Color.Red)
            ) {

                val currCart = viewModel.cart.value

                val fontHeight = 16.sp
                val lineHeight = 25.dp
                val defaultUnexpH = 100f
                val totalListHeight = lineHeight.value * (currCart?.items?.size?:0)
                val maxBoxHeight = min(defaultUnexpH, totalListHeight).dp

                Log.i("SUMMARY", "Parametri box espandibile: fontHeight=$fontHeight, totalListHeight=$totalListHeight, maxBoxHeight=$maxBoxHeight")


                // LISTA OGGETTI DEL CARRELLO
                Column(
                    modifier = Modifier
                        //.background(Color.Cyan)
                        .animateContentSize()   //deve andare prima dell'altezza
                        .height(if (expanded) totalListHeight.dp else maxBoxHeight)
                        .fillMaxWidth()
                ){
                    if(!viewModel.cart.value?.items.isNullOrEmpty()){
                        viewModel.cart.value?.items?.forEach {
                            val subTot : Float =
                                viewModel.prodsList.value?.filter { si -> si.name == it.key }?.map{ si -> si.price}?.sum()?.times(it.value) ?: 0f

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
                    }else{
                        Text(text = stringResource(id = R.string.txt_something_gone_wrong))   //TODO
                    }

                }

                if(totalListHeight > defaultUnexpH){
                    if(!expanded)
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "...",
                                maxLines = 1,
                                softWrap = true,
                                fontSize = fontHeight
                            )
                        }

                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ){
                        TextButton(onClick = { expanded = !expanded }) {
                            if(expanded)
                                Text(stringResource(id = R.string.btn_see_less).capitalize())
                            else {
                                Text(stringResource(id = R.string.btn_see_more).capitalize())
                            }
                        }
                    }
                }

                Spacer(
                    Modifier
                        .height(16.dp)
                        .fillMaxWidth()
                        //.background(Color.Black)
                )

                // LOCKER PREVIEW
                Card(
                    modifier = Modifier
                        //.background(Color.LightGray, RoundedCornerShape(16.dp))
                        .fillMaxWidth()
                        //.height(200.dp)
                        .clickable { navController.navigate(Screens.CstLockerSelector.route) }
                ){
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .background(Color.Yellow)
                    ){
                        Image(painterResource(id = R.drawable.mappa), contentDescription = null, Modifier.fillMaxWidth())   //TODO clippare
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ){
                        Text(
                            text = stringResource(id = R.string.selected_locker).capitalize(),
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )

                        Text(
                            viewModel.lockersList.value?.find { it.id == viewModel.currOrder.value?.lockerID }?.name?:"[LOCKER NAME]",
                            fontWeight = FontWeight.SemiBold
                        )

                        Row(){
                            Icon(Icons.Outlined.FmdGood, contentDescription = null)
                            Text(
                                viewModel.lockersList.value?.find { it.id == viewModel.currOrder.value?.lockerID }?.address?:"[LOCKER ADDRESS]",
                                maxLines = 1,
                                softWrap = true
                            )
                        }
                    }
                }

                Spacer(
                    Modifier
                        .height(16.dp)
                        .fillMaxWidth()
                        //.background(Color.Black)
                )

                // PAYMENT METHOD OVERVIEW
                Card(
                    Modifier
                        .fillMaxWidth()
                ){
                    ListItem(
                        modifier = Modifier
                            .height(70.dp)
                            .clickable { performDevMsg(scope, snackbarHostState, context) },
                        headlineContent = {
                            Column {
                                Text(stringResource(id = R.string.title_payment_method).capitalize())
                                
                                Row(){
                                    Text("**** 4187")
                                    Image(painter= painterResource(id = R.drawable.mastercard), contentDescription = "mastercard logo",
                                        Modifier
                                            .height(24.dp)
                                            .padding(horizontal = 8.dp))
                                }
                            }
                        },
                        trailingContent = { ArrowRt() }
                    )
                }

                Spacer(
                    Modifier
                        .height(32.dp)
                        .fillMaxWidth()
                        //.background(Color.Black)
                )

                // TOTAL PRICE OVERVIEW

                Column(
                    Modifier
                        .fillMaxWidth()
                ) {
                    PriceItemList(
                        stringResource(R.string.price_subtotal).capitalize(),
                        String.format("%.2f €", viewModel.cart.value?.subtot?:0f)
                    )  //TODO
                    PriceItemList(
                        stringResource(R.string.price_shipment).capitalize(),
                        String.format("%.2f €", Fees.SHIPMENT)
                    )
                    PriceItemList(
                        stringResource(R.string.price_service).capitalize(),
                        String.format("%.2f €", Fees.SERVICE)
                    )
                    PriceListTotal(
                        stringResource(R.string.price_total).uppercase(),
                        String.format(
                            "%.2f €",
                            (viewModel.cart.value?.subtot
                                ?: 0f) + Fees.total()
                        )
                    )

                    Spacer(Modifier.height(80.dp))
                }
            }
        }
    }
}

@Composable
private fun FabProceed(total: Float = 0f, onClick: () -> Unit) {
    ExtendedFloatingActionButton(
        onClick = onClick, //navController.navigate(Screens.CstOrderSent.route)
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
    ) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()){
            Text(text="${String.format("%.2f", total)} €")   //viewModel.getCartTotal()
            Text(stringResource(id = R.string.btn_order_now).capitalize())
        }
    }
}