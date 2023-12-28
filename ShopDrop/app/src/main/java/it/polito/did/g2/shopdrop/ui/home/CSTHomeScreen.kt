package it.polito.did.g2.shopdrop.ui.home

import android.content.Context
import android.os.Build
import android.os.LocaleList
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsBusFilled
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import it.polito.did.g2.shopdrop.MainViewModel
import it.polito.did.g2.shopdrop.R
import it.polito.did.g2.shopdrop.data.Order
import it.polito.did.g2.shopdrop.data.OrderStateName
import it.polito.did.g2.shopdrop.data.SectionName
import it.polito.did.g2.shopdrop.data.StoreItem
import it.polito.did.g2.shopdrop.data.StoreItemCategory
import it.polito.did.g2.shopdrop.data.TabScreen
import it.polito.did.g2.shopdrop.ui.common.BottomBar
import it.polito.did.g2.shopdrop.ui.common.ItemCard
import it.polito.did.g2.shopdrop.ui.common.StoreItemCard
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CSTHomeScreen(navController : NavController, viewModel: MainViewModel){
    var currentTab = TabScreen.HOME

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    var query by rememberSaveable { mutableStateOf("") }
    var searchBoxActive by rememberSaveable { mutableStateOf(false) }

    //Bottom sheet
    val bottomSheetState = rememberModalBottomSheetState()
    val bottomSheetScope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    var targetItem by remember{ mutableStateOf<StoreItem?>(null) }

    Scaffold(
        //topBar = { TopBar(currentTab, scrollBehavior = scrollBehavior) },
        bottomBar = { BottomBar(currentTab, navController) },
        //modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        //floatingActionButton = { AddButton(onClick = {/*TODO*/}) },
        //floatingActionButtonPosition = FabPosition.End
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .semantics { isTraversalGroup = true }){
                SearchBar(  //https://www.youtube.com/watch?v=90gokceSYdM
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .semantics { traversalIndex = -1f },
                    query = query,
                    onQueryChange = { query = it },
                    onSearch = {
                        searchBoxActive = false
                        Log.d("SEARCH","Search for $query")
                        //TODO
                    },
                    active = searchBoxActive,
                    onActiveChange = {
                        searchBoxActive = it
                    },
                    placeholder = {
                        Row(){
                            Text(stringResource(R.string.placeholder_search_main).capitalize())
                            Text("[APP LOGO]")
                        } },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search icon") },
                    trailingIcon = {
                        if(searchBoxActive){
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Close icon",
                                Modifier.clickable {
                                    if(query.isNotEmpty())
                                        query = ""
                                    else
                                        searchBoxActive = false
                                }
                            )
                        }

                    }
                ){
                    //HINTS -> TODO: mettere ultime 3 ricerche e poi i risultati
                    repeat(4) { idx ->
                        val resultText = "Suggestion $idx"
                        ListItem(
                            headlineContent = { Text(resultText) },
                            supportingContent = { Text("Additional info") },
                            leadingContent = { Icon(Icons.Filled.Star, contentDescription = null) },
                            modifier = Modifier
                                .clickable {
                                    query = resultText
                                    searchBoxActive = false
                                }
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp)
                        )
                    }
                }

                if(!searchBoxActive){
                    Column(modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                    ) {

                        /*
                        BasicTextField(
                            value = TextFieldValue(query),
                            onValueChange = {it -> query=it},
                            //label = {Text("Cerca su ShopDrop")},
                            singleLine = true,
                            //leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                            shape = RoundedCornerShape(48.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .padding(horizontal = 16.dp)
                        )
                         */




                        //TODO Pending orders
                        //TODO controllare il vm che ci siano ordini pendenti ed eventualmente mostrarli
                        if(viewModel.hasPending.value!!)    //Condizione dei pending order
                            PendingOrdersCard(navController, viewModel)
                        //TODO demo oggetti acquistabili

                        /*
                        HomeSection(SectionName.BUY_AGAIN, navController = navController, onClick = { showBottomSheet = true })
                        HomeSection(SectionName.PROMO, navController = navController, onClick = { showBottomSheet = true })
                        */

                        //SEZIONI CATEGORIE MERCEOLOGICHE
                        enumValues<StoreItemCategory>().forEach {
                            val itList = viewModel.storeItems.value?.filter{storeItem -> storeItem.category==it }

                            if(!itList.isNullOrEmpty())
                                HomeCatSection(it, itList, navController = navController){ item ->
                                    targetItem = item
                                    showBottomSheet = true
                                }
                        }
                    }
                }
            }

        }

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
            viewModel.pendingOrdersList.value?.forEachIndexed{i, it ->
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()){
                    OrderListItem(order = it, navController, viewModel)

                    if (i < viewModel.pendingOrdersList.value!!.size)
                        HorizontalDivider()
                }
            }
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
                IconButton(onClick = { navController.navigate("CameraScreen") }) {
                    Icon(Icons.Filled.QrCodeScanner, contentDescription = null)
                }
        },
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                viewModel.targetOrderID = order.id
                navController.navigate("COrderDetailScreen")
            }
    )
}

@Composable
fun HomeCatSection(category: StoreItemCategory, itemList: List<StoreItem>, navController: NavController, onClick : (it: StoreItem) -> Unit){

    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom){
            Text(text = stringResource(id = category.getStringRef()).capitalize(),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(16.dp)
            )

            TextButton(
                onClick = { navController.navigate("CategorySection") },
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(text = stringResource(R.string.btn_see_more).capitalize(),
                    fontSize = 18.sp,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        Row(modifier = Modifier
            .padding(horizontal = 16.dp)
            .horizontalScroll(rememberScrollState())){

            //Text("TEST")

            val upperLimit = if(itemList.size<5) itemList.size else 5
            //Text("upper limit is $upperLimit")

            for (i in 0 until upperLimit){
                StoreItemCard(itemList[i]) {
                    onClick(it)
                }
            }


/*
            for(i in 0 until upperLimit){
                //ItemCard(itemList[i], onClick)
                Card(modifier = Modifier
                    .size(width = 160.dp, height = 200.dp)
                    .padding(8.dp)
                    .clickable(onClick = onClick)) {
                    Column {
                        Spacer(Modifier.height(160.dp))
                        Text(text = itemList[i].name)
                        Text(text = itemList[i].price.toString())
                    }
                }
            }

             */
        }
    }
}

@Composable
fun HomeSection(sectionType: SectionName, navController: NavController, onClick : () -> Unit){
    var sectionName = "[Section title]"

    when(sectionType){
        SectionName.BUY_AGAIN -> {
            sectionName = stringResource(R.string.title_buy_again).capitalize()
        }
        SectionName.PROMO -> {
            //sectionName = stringResource(R.string.title_promo).capitalize()
        }
    }

    Column(modifier = Modifier.padding(vertical = 16.dp)){
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom){
            Text(text = sectionName,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(16.dp)
            )

            TextButton(
                onClick = { navController.navigate("CategorySection") },
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(text = stringResource(R.string.btn_see_more).capitalize(),
                    fontSize = 18.sp,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        Row(modifier = Modifier
            .padding(horizontal = 16.dp)
            .horizontalScroll(rememberScrollState())){
            ItemCard(onClick)
            ItemCard(onClick)
            ItemCard(onClick)
        }

    }
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
