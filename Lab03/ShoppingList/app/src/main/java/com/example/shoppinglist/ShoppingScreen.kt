package com.example.shoppinglist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.List
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingScreen(vm : PurchaseViewModel){

    Scaffold (
        topBar = { Header{vm.switchAppMode()} },
        floatingActionButton = { FilterButton() },
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

                for(i in 1..5){
                    ShoppingListItem()
                }
                Divider()
            }
        }
    }
}

@Composable
fun LinearDeterminateIndicator() {
    //TODO
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListItem(){
    ListItem(
        headlineText = { Text("[Item description]") },
        leadingContent = {
            Checkbox(checked = false, onCheckedChange = {/*TODO*/})
        }
    )
}


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
            IconButton(onClick = { onClick } ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back to composing mode",

                )
            }
        },
    )
}