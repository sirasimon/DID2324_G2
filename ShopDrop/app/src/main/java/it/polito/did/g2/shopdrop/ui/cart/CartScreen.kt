package it.polito.did.g2.shopdrop.ui.cart

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import it.polito.did.g2.shopdrop.MainViewModel
import it.polito.did.g2.shopdrop.R
import it.polito.did.g2.shopdrop.data.StoreItem
import it.polito.did.g2.shopdrop.data.TabScreen
import it.polito.did.g2.shopdrop.ui.common.BottomBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(navController: NavController, viewModel: MainViewModel){
    var currentTab = TabScreen.CART
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    var subtotal = mutableStateOf(viewModel.subtot.value)



    Scaffold(
        //topBar = { TopBar(currentTab = TabScreen.CART, title = stringResource(R.string.title_order_summary).capitalize(), scrollBehavior = scrollBehavior )},
        bottomBar = { BottomBar(currentTab, navController, viewModel) },
        //modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        floatingActionButton = { CheckoutButton() },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            Column( modifier = Modifier.verticalScroll(rememberScrollState())){

            //TODO: fare if per carrello vuoto
                if(!viewModel.cart.value.isNullOrEmpty()){
                    viewModel.cart.value!!.forEach {
                        ProductListItem(viewModel.storeItems.value?.find { item -> item.name==it.key }, it.value)
                    }
                }



                // TOTAL PRICE AREA

                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp)){
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(24.dp)){}

                    PriceItemList(stringResource(R.string.price_subtotal).capitalize(), String.format("%.2f €", subtotal.value))  //TODO
                    PriceItemList(stringResource(R.string.price_shipment).capitalize(), String.format("%.2f €", viewModel.shipmentFee))
                    PriceItemList(stringResource(R.string.price_service).capitalize(), String.format("%.2f €", viewModel.serviceFee))
                    PriceListTotal(stringResource(R.string.price_total).uppercase(), String.format("%.2f €", (subtotal.value?:0f)+viewModel.shipmentFee+viewModel.serviceFee))

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(80.dp)){}
                }
            }
        }
    }
}

@Composable
fun ProductListItem(item: StoreItem?, quantity: Int){
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ){
        ListItem(
            leadingContent = {
                Icon(Icons.Filled.ShoppingCart, contentDescription = null)  //TODO
            },
            headlineContent = {
                Column {
                    Text(item?.name?.capitalize()?:"[null]")
                    Text(stringResource(id = R.string.txt_quantity).capitalize()+": "+quantity)
                }
            },
            trailingContent = {
                Column {
                    Text(text = String.format("%.2f €", (quantity*(item?.price?:.0f))))
                    Row(){
                        IconButton(onClick = { /*TODO*/ }) {
                            Text("-", fontSize = 40.sp)
                        }
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(Icons.Filled.Add, null)
                        }
                    }
                }

            },    //aggiungere if per farlo apparire solo se è disponibile
        )
    }
}

@Composable
fun PriceItemList(voice : String, sum : String){
    Box(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
    ){
        Box(Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterStart
        ){
            Text(voice, Modifier.padding(horizontal = 8.dp))
        }

        Box(Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ){
            Text(sum, Modifier.padding(horizontal = 8.dp))
        }
    }
}

@Composable
fun PriceListTotal(voice : String, sum : String){
    Divider(Modifier.padding(8.dp))
    Box(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
    ){
        Box(Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterStart
        ){
            Text(text = voice,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier.padding(horizontal = 8.dp))
        }

        Box(Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ){
            Text(text = sum,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier.padding(horizontal = 8.dp))
        }
    }
}

@Composable
fun CheckoutButton(){
    ExtendedFloatingActionButton(onClick = { /*TODO*/ }, modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 32.dp)) {
        Text("CHECKOUT")
    }
}