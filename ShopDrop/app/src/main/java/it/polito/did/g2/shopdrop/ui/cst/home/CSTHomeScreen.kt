package it.polito.did.g2.shopdrop.ui.cst.home

import android.content.Context
import android.os.Build
import android.os.LocaleList
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.DirectionsBusFilled
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import it.polito.did.g2.shopdrop.MainViewModel
import it.polito.did.g2.shopdrop.R
import it.polito.did.g2.shopdrop.data.Order
import it.polito.did.g2.shopdrop.data.OrderStateName
import it.polito.did.g2.shopdrop.data.StoreItem
import it.polito.did.g2.shopdrop.data.TabScreen
import it.polito.did.g2.shopdrop.navigation.Screens
import it.polito.did.g2.shopdrop.ui.cst.common.BottomBar
import java.time.format.DateTimeFormatter
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CSTHomeScreen(navController : NavController, viewModel: MainViewModel){

    val currentTab = TabScreen.HOME

    var query by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    var searchBarFocussed by remember{ mutableStateOf(false) }

    //Items
    val itemList = viewModel.prodsList.observeAsState()

    //Bottom sheet
    val bottomSheetState = rememberModalBottomSheetState()
    val bottomSheetScope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    var targetItem by remember{ mutableStateOf<StoreItem?>(null) }

    Scaffold(
        bottomBar = { BottomBar(currentTab, navController, viewModel) }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                Modifier
                    .fillMaxSize()){

                // SEARCH BAR                                                                       SEARCH BAR

                OutlinedTextField(
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search icon") },
                    value = query,
                    placeholder = { Text(text = stringResource(R.string.placeholder_search_main).capitalize(),)},
                    onValueChange = {
                        searchBarFocussed = true
                        query = it
                    },
                    shape = RoundedCornerShape(32.dp),
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            searchBarFocussed = false
                            focusManager.clearFocus()
                        }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )


                Column(modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                ) {

                    // PENDING ORDERS                                                           PENDING ORDERS
                    //TODO Pending orders
                    //TODO impedire visualizzazione quando si sta cercando un prodotto
                    //TODO controllare il vm che ci siano ordini pendenti ed eventualmente mostrarli
                    if(viewModel.hasPending.value!!)    //Condizione dei pending order
                        PendingOrdersCard(navController, viewModel)

                    if(query==""){
                        itemList.value?.map{it.category}
                            ?.distinct()
                            ?.sortedBy { it.toString() }
                            ?.forEach {
                                val itList = itemList.value?.filter{ storeItem -> storeItem.category==it }

                                if(!itList.isNullOrEmpty())
                                    CategorySection(
                                        it!!,
                                        itList,
                                        navController,
                                        { item ->
                                            targetItem = item
                                            showBottomSheet = true
                                        }
                                    )
                        }
                    }else{
                        itemList.value?.filter{it.name.contains(query)}
                            ?.map{it.category}
                            ?.distinct()
                            ?.sortedBy { it.toString() }
                            ?.forEach {
                                val itList = itemList.value?.filter{ storeItem -> storeItem.category==it && storeItem.name.contains(query) }

                                if(!itList.isNullOrEmpty())
                                    CategorySection(
                                        it!!,
                                        itList,
                                        navController,
                                        { item ->
                                            targetItem = item
                                            showBottomSheet = true
                                        },
                                        query
                                    )
                            }
                    }
                }
            }
        }

        // CONFIRM SHEET                                                                            CONFIRM SHEET
        if(showBottomSheet){
            Log.d("MODAL", "Dovrebbe aprirsi qui")
            ConfirmSheet(targetItem, viewModel, {showBottomSheet=false}, bottomSheetState, bottomSheetScope)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PendingOrdersCard(navController: NavController, viewModel: MainViewModel){

    Column(modifier = Modifier.padding(vertical = 16.dp)){
        /*
        Text("Ordini recenti:",
            fontSize = 18.sp,
            modifier = Modifier.padding(16.dp)
        )
         */

        ElevatedCard(
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ){
            /*
            viewModel.pendingOrdersList.value?.forEachIndexed{i, it ->
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()){
                    OrderListItem(order = it, navController, viewModel)

                    if (i < viewModel.pendingOrdersList.value!!.size)
                        HorizontalDivider()
                }
            }

             */
        }

        /*
        Box(Modifier){
            ElevatedCard(
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .background(Color.Red)
            ){
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Cyan)){
                    ListItem(
                        headlineContent = { Text("Pacco Halloween") },
                        trailingContent = {
                            IconButton(onClick = { /*TODO*/ })
                            {
                                Icon(Icons.Filled.QrCodeScanner, contentDescription = null)
                            }
                        },    //aggiungere if per farlo apparire solo se è disponibile
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { navController.navigate("COrderDetailScreen") }
                    )
                    HorizontalDivider()
                    ListItem(
                        headlineContent = {Text("Regalo Matti")},
                        modifier = Modifier.clickable { /*TODO*/ }
                    )
                }
            }
        }
         */

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OrderListItem(order: Order, navController: NavController, viewModel: MainViewModel){
    val creationTime = order.stateList?.find { it.state==OrderStateName.CREATED }?.timestamp?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
    val icon = when(order.stateList?.last()?.state){
        OrderStateName.CREATED -> Icons.Filled.Add
        OrderStateName.RECEIVED -> Icons.Filled.Business
        OrderStateName.CARRIED -> Icons.Filled.DirectionsBusFilled
        OrderStateName.AVAILABLE -> Icons.Filled.Done
        OrderStateName.COLLECTED -> Icons.Filled.DoneAll
        else -> Icons.Filled.QuestionMark
    }

    val scannable = order.stateList?.last()?.state == OrderStateName.AVAILABLE

    ListItem(
        headlineContent = { Text(stringResource(id = R.string.order_dated).capitalize() + " " + creationTime) },
        leadingContent = { Icon(icon, contentDescription = null) },
        trailingContent = {
            if (scannable)
                IconButton(onClick = { navController.navigate(Screens.CstCamera.route) }) { //TODO: aggiungere qui l'argomento alla navigazione dell'ID dell'ordine
                    Icon(Icons.Filled.QrCodeScanner, contentDescription = null)
                }
        },
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                viewModel.targetOrderID = order.id
                navController.navigate(Screens.CstOrderDetail.route) //TODO: aggiungere qui l'argomento alla navigazione dell'ID dell'ordine
            }
    )
}

fun getSystemLanguage(context: Context): Locale {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        val localeList = LocaleList.getDefault()
        localeList[0]
    } else {
        @Suppress("DEPRECATION")
        Locale.getDefault()
    }
}
