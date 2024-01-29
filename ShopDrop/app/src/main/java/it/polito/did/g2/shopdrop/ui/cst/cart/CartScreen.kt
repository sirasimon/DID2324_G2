package it.polito.did.g2.shopdrop.ui.cst.cart

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import it.polito.did.g2.shopdrop.MainViewModel
import it.polito.did.g2.shopdrop.R
import it.polito.did.g2.shopdrop.data.TabScreen
import it.polito.did.g2.shopdrop.navigation.Screens
import it.polito.did.g2.shopdrop.ui.cst.common.BottomBar
import it.polito.did.g2.shopdrop.ui.cst.common.TopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CSTCartScreen(navController: NavController, viewModel: MainViewModel){
    var currentTab = TabScreen.CART
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    var subtotal = mutableStateOf(viewModel.subtot.value)

    var hasItems = viewModel.itemsInCart.value != 0



    Scaffold(
        topBar = { TopBar(navController, stringResource(id = R.string.title_cart), scrollBehavior) },
        bottomBar = { BottomBar(currentTab, navController, viewModel) },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        floatingActionButton = { if(hasItems) CheckoutButton { navController.navigate(Screens.CstCheckout.route) } },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            Column( modifier = Modifier
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())){

                if(!viewModel.cart.value.isNullOrEmpty()){
                    viewModel.cart.value!!.forEach {
                        //ProductListItem(viewModel.storeItems.value?.find { item -> item.name==it.key }, it.value)
                        ProductListItem(viewModel, it.key)
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

                        Spacer(Modifier.height(80.dp))
                    }
                }else{
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()){
                        Text(text = stringResource(id = R.string.txt_empty_cart))
                    }

                }
            }
        }
    }
}

@Composable
fun ProductListItem(viewModel: MainViewModel, itemName: String){

    val cart = viewModel.cart.observeAsState()
    val quantity = mutableIntStateOf(viewModel.cart.value?.get(itemName) ?: 0)
    val storeItem = viewModel.storeItems.value?.find { item -> item.name == itemName }

    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ){
        Row(
            Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ){
            Row(
                Modifier
                    .fillMaxWidth(3f / 4)
                    .height(70.dp)) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(storeItem?.thumbnail)
                        .listener(
                            onError = { request, result ->
                                Log.e("ASYNCIMG", "Errore nel caricamento dell'immagine (${result.throwable})")
                            }
                        )
                        .crossfade(true)
                        .build(),
                    contentScale = ContentScale.Crop,
                    contentDescription = stringResource(id = R.string.desc_item_pic_of)+itemName,
                    modifier = Modifier
                        .height(70.dp)
                        .width(70.dp)
                        .clip(RoundedCornerShape(8.dp))
                )

                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .fillMaxSize()
                ) {
                    Text(
                        text = itemName.capitalize()?:"[null]",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 1,
                        overflow = TextOverflow.Clip)
                    Text(stringResource(id = R.string.txt_quantity).capitalize()+": "+ (quantity.value), modifier = Modifier.padding(vertical = 8.dp))
                }
            }

            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(text = String.format("%.2f €", (quantity.value*(storeItem?.price?:.0f))), fontWeight = FontWeight.SemiBold, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)

                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ){

                    IconButton( onClick = {viewModel.modifyCart(storeItem, --quantity.value)} ) {
                        Image(painter = painterResource(R.drawable.btn_sub), contentDescription = "Decrease quantity")
                    }

                    IconButton(onClick = {viewModel.modifyCart(storeItem, ++quantity.value)} ) {
                        Image(painter = painterResource(R.drawable.btn_add), contentDescription = "increase quantity")
                    }
                }

            }
        }
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
fun CheckoutButton(onClick: () -> Unit){
    ExtendedFloatingActionButton(onClick = onClick, modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 32.dp)) {
        Text("CHECKOUT")
    }
}