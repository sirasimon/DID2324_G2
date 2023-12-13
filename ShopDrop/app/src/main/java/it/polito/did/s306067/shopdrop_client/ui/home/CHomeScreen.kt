package it.polito.did.s306067.shopdrop_client.ui.home

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import it.polito.did.s306067.shopdrop_client.R
import it.polito.did.s306067.shopdrop_client.ui.common.BottomBar
import it.polito.did.s306067.shopdrop_client.ui.common.ItemCard
import it.polito.did.s306067.shopdrop_client.ui.common.TabScreen
import it.polito.did.s306067.shopdrop_client.ui.common.TopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CHomeScreen(navController : NavController){
    var currentTab = TabScreen.HOME

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    //Bottom sheet
    val bottomSheetState = rememberModalBottomSheetState()
    val bottomSheetScope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }


    Scaffold(
        topBar = { TopBar(currentTab, scrollBehavior = scrollBehavior) },
        bottomBar = { BottomBar(currentTab, navController) },
        //modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        //floatingActionButton = { AddButton(onClick = {/*TODO*/}) },
        //floatingActionButtonPosition = FabPosition.End
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
            ) {
                Text(text = "CLIENT HOME")

                //TODO Searchbar
                //TODO Pending orders
                //TODO controllare il vm che ci siano ordini pendenti ed eventualmente mostrarli
                if(true)    //Condizione dei pending order
                    PendingOrdersCard(navController, { showBottomSheet = true })
                //TODO demo oggetti acquistabili

                HomeSection(SectionName.ALREADY_BOUGHT, navController = navController, onClick = { showBottomSheet = true })
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
    val sectionName = when(sectionType){
        SectionName.ALREADY_BOUGHT -> {
            "Acquista di nuovo"
        }
        SectionName.PROMO -> {
            "In offerta"
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
                Text(text = "Vedi altro",
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
