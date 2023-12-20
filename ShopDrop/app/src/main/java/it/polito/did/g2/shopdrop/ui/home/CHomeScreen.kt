package it.polito.did.g2.shopdrop.ui.home

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import it.polito.did.g2.shopdrop.R
import it.polito.did.g2.shopdrop.ui.common.BottomBar
import it.polito.did.g2.shopdrop.ui.common.ItemCard
import it.polito.did.g2.shopdrop.ui.common.TabScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CHomeScreen(navController : NavController){
    var currentTab = TabScreen.HOME

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    var query by rememberSaveable { mutableStateOf("") }
    var searchBoxActive by rememberSaveable { mutableStateOf(false) }

    //Bottom sheet
    val bottomSheetState = rememberModalBottomSheetState()
    val bottomSheetScope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }


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
            Box(Modifier.fillMaxSize().semantics { isTraversalGroup = true }){
                SearchBar(  //https://www.youtube.com/watch?v=90gokceSYdM
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
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
                        if(true)    //Condizione dei pending order
                            PendingOrdersCard(navController, { showBottomSheet = true })
                        //TODO demo oggetti acquistabili

                        HomeSection(SectionName.BUY_AGAIN, navController = navController, onClick = { showBottomSheet = true })
                        HomeSection(SectionName.PROMO, navController = navController, onClick = { showBottomSheet = true })

                        Box(){
                            Text("Varie categorie")
                        }

                        Box(){
                            Text("Quando schiacci un bottone appare overlay per acquisto quantità e conferma")
                        }

                        Box(){
                            Text("Quando schiacci un bottone appare overlay per acquisto quantità e conferma")
                        }
                        Box(){
                            Text("Quando schiacci un bottone appare overlay per acquisto quantità e conferma")
                        }
                        Box(){
                            Text("Quando schiacci un bottone appare overlay per acquisto quantità e conferma")
                        }

                    }
                }
            }

        }

        if(showBottomSheet){
            Log.d("MODAL", "Dovrebbe aprirsi qui")
            ConfirmSheet({showBottomSheet=false}, bottomSheetState, bottomSheetScope)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PendingOrdersCard(navController: NavController, onClick: () -> Unit){

    Column(modifier = Modifier.padding(vertical = 16.dp)){
        /*
        Text("Ordini recenti:",
            fontSize = 18.sp,
            modifier = Modifier.padding(16.dp)
        )
         */
        Box(Modifier){
            ElevatedCard(
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ){
                Column(){
                    ListItem(
                        headlineContent = { Text("Pacco Halloween") },
                        trailingContent = {
                            IconButton(onClick = {/*TODO*/ })
                            {
                                Icon(painter = painterResource(R.drawable.baseline_qr_code_scanner_24), contentDescription = null)
                            }
                        },    //aggiungere if per farlo apparire solo se è disponibile
                        modifier = Modifier.clickable { navController.navigate("COrderDetailScreen") }
                    )
                    Divider()
                    ListItem(
                        headlineContent = {Text("Regalo Matti")},
                        modifier = Modifier.clickable { /*TODO*/ }
                    )
                }
            }
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
            sectionName = stringResource(R.string.title_promo).capitalize()
        }
    }

    Column(modifier = Modifier.padding(vertical = 16.dp)){
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom){
            Text(text = sectionName,
                fontSize = 18.sp,
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
