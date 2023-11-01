package com.example.shoppinglist

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingScreen(navController : NavController, vm : PurchaseViewModel){
    var currentProgress by remember { mutableFloatStateOf(vm.getProgress()) }
    val progressScope = rememberCoroutineScope() // Create a coroutine scope


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
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    progress = currentProgress
                )

                for(cat in vm.getCategories()){
                    CategorySection(cat, vm)
                    vm.getItems()
                        .filter { item -> item.category==cat }
                        .forEach { item ->
                            ShoppingListItem(item, vm) {
                                progressScope.launch {
                                    currentProgress = vm.getProgress()
                                }
                            }
                        }
                    Divider()
                }
            }
        }
    }
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListItem(item: PurchasableItem, vm : PurchaseViewModel, updateProgress: () -> Unit){
    val (checked, setChecked) = remember{
        mutableStateOf(item.purchased)
    }

    ListItem(
        headlineText = { Text(item.name) },
        leadingContent = {
            Checkbox(checked = checked, onCheckedChange = {
                item.purchased = !checked
                setChecked(!checked)
                vm.setPurchase(item, !checked)
                updateProgress()

                Log.d("ShoScr", "ShoppingListItem > Updated item is $item\n(also: ${vm.getItems()})")})
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