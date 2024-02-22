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
import androidx.compose.material.icons.filled.QrCodeScanner
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
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CSTHomeScreen(navController : NavController, viewModel: MainViewModel){

    //viewModel.loadOrders()

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
                        // PENDING ORDERS                                                           PENDING ORDERS TODO non funziona!!!

                        if(!viewModel.pendingOrders.value.isNullOrEmpty()) {    //Condizione dei pending order
                            Log.i("PENDING ORDERS OK", "Sì, ci sono degli ordini pendenti")
                            Column(modifier = Modifier.padding(vertical = 16.dp)) {
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
                                ) {
                                    viewModel.pendingOrders.value!!.forEach {
                                        val onMore = {id : String -> navController.navigate(Screens.CstOrderDetail.route+"/$id") }
                                        val onScan = {id : String -> navController.navigate(Screens.CstCamera.route+"/$id")}
                                        PendingItemList(it, viewModel, onMore, onScan)
                                    }
                                }
                            }
                        }

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
fun PendingItemList(order: Order, viewModel: MainViewModel, onMore : (String) -> Unit, onScan: (String) -> Unit){

    val lastState = order.stateList?.last()?.state

    ListItem(
        headlineContent = {
            Column {
                Text("${stringResource(id = R.string.order_dated).capitalize()} ${viewModel.getDateString( order.stateList?.first()?.timestamp!! )}")
                Text( stringResource(id = viewModel.getOrderStateStringId(lastState) ) )
            }
        },
        leadingContent = {
            Icon(viewModel.getOrderStateIcon(lastState), contentDescription = null)
                         },
        trailingContent = {
            IconButton(
                enabled = lastState==OrderStateName.AVAILABLE,
                onClick = { onScan(order.id!!) } //TODO: aggiungere qui l'argomento alla navigazione dell'ID dell'ordine
            ){
                Icon(Icons.Filled.QrCodeScanner, contentDescription = null)
            }
        },
        modifier = Modifier.clickable { onMore(order.id!!) }
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
