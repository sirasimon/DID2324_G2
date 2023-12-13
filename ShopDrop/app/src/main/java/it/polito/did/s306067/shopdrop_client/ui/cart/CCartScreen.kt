package it.polito.did.s306067.shopdrop_client.ui.cart

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
import androidx.compose.material3.Card
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import it.polito.did.s306067.shopdrop_client.ui.common.BottomBar
import it.polito.did.s306067.shopdrop_client.ui.common.TabScreen
import it.polito.did.s306067.shopdrop_client.ui.common.TopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CCartScreen(navController: NavController){
    var currentTab = TabScreen.CART

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = { TopBar(currentTab, scrollBehavior) },
        bottomBar = { BottomBar(currentTab, navController) },
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
                //TODO SearchBar
                Card(
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                ){
                    ListItem(
                        leadingContent = {
                            Icon(Icons.Filled.ShoppingCart, contentDescription = null)
                        },
                        headlineContent = {
                            Column {
                                Text("Nome prodotto")
                                Text("Quantità: 1")
                            }
                        },
                        trailingContent = {
                            Column {
                                Text("5,00 €")
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

                Card(
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                ){
                    ListItem(
                        leadingContent = {
                            Icon(Icons.Filled.ShoppingCart, contentDescription = null)
                        },
                        headlineContent = {
                            Column {
                                Text("Nome prodotto")
                                Text("Quantità: 1")
                            }
                        },
                        trailingContent = {
                            Column {
                                Text("5,00 €")
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

                Card(
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                ){
                    ListItem(
                        leadingContent = {
                            Icon(Icons.Filled.ShoppingCart, contentDescription = null)
                        },
                        headlineContent = {
                            Column {
                                Text("Nome prodotto")
                                Text("Quantità: 1")
                            }
                        },
                        trailingContent = {
                            Column {
                                Text("5,00 €")
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

                Card(
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                ){
                    ListItem(
                        leadingContent = {
                            Icon(Icons.Filled.ShoppingCart, contentDescription = null)
                        },
                        headlineContent = {
                            Column {
                                Text("Nome prodotto")
                                Text("Quantità: 1")
                            }
                        },
                        trailingContent = {
                            Column {
                                Text("5,00 €")
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

                Card(
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                ){
                    ListItem(
                        leadingContent = {
                            Icon(Icons.Filled.ShoppingCart, contentDescription = null)
                        },
                        headlineContent = {
                            Column {
                                Text("Nome prodotto")
                                Text("Quantità: 1")
                            }
                        },
                        trailingContent = {
                            Column {
                                Text("5,00 €")
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

                ElevatedCard(
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                ){
                    ListItem(
                        leadingContent = {
                            Icon(Icons.Filled.ShoppingCart, contentDescription = null)
                        },
                        headlineContent = {
                            Column {
                                Text("Nome prodotto")
                                Text("Quantità: 1")
                            }
                        },
                        trailingContent = {
                            Column {
                                Text("5,00 €")
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

                ElevatedCard(
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                ){
                    ListItem(
                        leadingContent = {
                            Icon(Icons.Filled.ShoppingCart, contentDescription = null)
                        },
                        headlineContent = {
                            Column {
                                Text("Nome prodotto")
                                Text("Quantità: 1")
                            }
                        },
                        trailingContent = {
                            Column {
                                Text("5,00 €")
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

                ElevatedCard(
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                ){
                    ListItem(
                        leadingContent = {
                            Icon(Icons.Filled.ShoppingCart, contentDescription = null)
                        },
                        headlineContent = {
                            Column {
                                Text("Nome prodotto")
                                Text("Quantità: 1")
                            }
                        },
                        trailingContent = {
                            Column {
                                Text("5,00 €")
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

                ElevatedCard(
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                ){
                    ListItem(
                        leadingContent = {
                            Icon(Icons.Filled.ShoppingCart, contentDescription = null)
                        },
                        headlineContent = {
                            Column {
                                Text("Nome prodotto")
                                Text("Quantità: 1")
                            }
                        },
                        trailingContent = {
                            Column {
                                Text("5,00 €")
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

                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp)){
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(24.dp)){}

                    PriceItemList("Subtotale", "19,90 €")
                    PriceItemList("Spedizione", "1,50 €")
                    PriceItemList("Servizio", "2,00 €")
                    PriceListTotal("TOTALE", "23,40 €")

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