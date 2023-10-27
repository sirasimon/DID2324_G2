package com.example.shoppinglist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComposingScreen(vm : PurchaseViewModel){

    Scaffold (
        topBar = { Header() },
        floatingActionButton = { ShoppingModeButton { vm.switchAppMode() } },
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
                for(i in 1..5){
                    ComposingListItem()
                }

                AddNewItem()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComposingListItem(){
    ListItem(
        headlineText = { Text("[Item description]") },
        leadingContent = {
            Icon(
                Icons.Filled.PlayArrow,
                contentDescription = "Localized description",
            )
        },
        supportingText = {
            BadgedBox(badge = {}) {
                Badge{
                    val badgeText = "CATEGORIA"
                    Text(badgeText,
                        modifier = Modifier.semantics {
                            contentDescription = "Category of item is $badgeText"
                        })
                }
            }
        },
        trailingContent = {
            IconButton({/*TODO*/}){
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = "Delete item",
                )
            }
        }
    )
}


@Composable
fun ShoppingModeButton(onClick : () -> Unit){
    FloatingActionButton(
        onClick = { onClick() },
        shape = CircleShape,
        modifier = Modifier
            .padding(16.dp)
    ) {
        Icon(Icons.Filled.ShoppingCart, "Go to shopping mode")
    }
}

/**
 * Dialog that opens for adding a new item
 */
@Composable
fun AddItemDialog(onDismissRequest: () -> Unit,
                  onConfirmation: () -> Unit,
                  dialogTitle: String,
                  dialogText: String,
                  icon: ImageVector
){
    AlertDialog(
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Cancel")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewItem(){
    ListItem(
        headlineText = { Text("Add new item") },
        leadingContent = {
            Icon(
                Icons.Filled.Add,
                contentDescription = "Add new item"
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Header(){
    CenterAlignedTopAppBar(
        title = {
            Text(text = "COMPOSING MODE",
                maxLines = 1,
                textAlign = TextAlign.Center)
        }
    )
}