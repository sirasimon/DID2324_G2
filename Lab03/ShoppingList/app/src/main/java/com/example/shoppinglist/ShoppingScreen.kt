package com.example.shoppinglist

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingScreen(navController : NavController, vm : PurchaseViewModel){

    Scaffold (
        topBar = { Header{navController.navigate("ComposingScreen")} },
        //floatingActionButton = { FilterButton() },
        floatingActionButtonPosition = FabPosition.End
    ){
            paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(Modifier.fillMaxSize()){
                LinearDeterminateIndicator()

                for(cat in vm.getCategories()){
                    CategorySection(cat, vm)
                }
            }
        }
    }
}

@Composable
fun LinearDeterminateIndicator() {

}

@Composable
fun CategorySection(cat: ItemCategory, vm: PurchaseViewModel){
    //TODO Prendi la lista salvata in vm, filtrala per stampare le categorie e per ogni categoria stampa le voci
    Text(text = "$cat",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.background(Color.LightGray)
            .fillMaxWidth()
            .padding(10.dp)
    )

    for(i in vm.getItems().filter { item -> item.category==cat }){
        ShoppingListItem(i)//TODO ci devo passare il purchasableItem
    }

    Divider()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListItem(item: PurchasableItem){
    val (checked, setChecked) = remember{
        mutableStateOf(item.purchased)
    }

    ListItem(
        headlineText = { Text("${item.name}") },
        leadingContent = {
            Checkbox(checked = checked, onCheckedChange = {
                item.purchased = !checked
                setChecked(!checked)
                /*TODO devo cambiare il valore dell'elemento anche*/

                Log.d("ShoScr", "ShoppingListItem > Updated item is $item")})
        }
    )
}

/*
@Composable
fun FilterButton(/*onClick : () -> Unit*/){
    FloatingActionButton(
        onClick = { /*onClick()*/ },
        shape = CircleShape,
        modifier = Modifier
            .padding(16.dp)
    ) {
        Icon(Icons.Filled.List, "Filter categories")
    }
}
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Header(onClick : () -> Unit){
    CenterAlignedTopAppBar(
        title = {
            Text(text = "SHOPPING MODE",
                maxLines = 1,
                textAlign = TextAlign.Center)
        },
        navigationIcon = {
            IconButton(onClick = onClick ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back to composing mode"
                )
            }
        },
        actions = {
            IconButton(onClick = { onClick } ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search"
                )
            }
        }
    )
}