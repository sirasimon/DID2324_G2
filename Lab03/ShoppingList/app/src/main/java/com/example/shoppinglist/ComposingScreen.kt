package com.example.shoppinglist

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComposingScreen(navController : NavController, vm : PurchaseViewModel){

    var (openDialog, setOpen) = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val items by vm.itemsLiveData.observeAsState()

    Scaffold (
        topBar = { Header(onClick = { navController.navigate("ShoppingScreen") }) },
        snackbarHost = {SnackbarHost(hostState = snackbarHostState)}
    ){
        paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(Modifier.fillMaxSize()
                .verticalScroll(rememberScrollState())){

                //Prendi gli elementi inseriti e conservati nel vm, passali al modulo ComposingListItem e costruisci le voci
                items?.forEach { i ->
                    ComposingListItem(i, vm) { msg ->
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = msg,
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                }

                AddNewItemButton{setOpen(true)}

                if(openDialog){
                    AddItemDialog(
                        closeDialog = { setOpen(false) },
                        vm = vm,
                        snackbarMsg = {msg -> scope.launch { snackbarHostState.showSnackbar(message = msg, duration = SnackbarDuration.Short) }}
                    )
                }
            }
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComposingListItem(item : PurchasableItem, vm : PurchaseViewModel, msg : (String) -> Unit){
    ListItem(
        modifier = Modifier.height(50.dp),
        headlineText = { Text("${item.name}") },
        leadingContent = {
            Icon(
                //Icons.Filled.Clear,
                painter = painterResource(R.drawable.baseline_circle_24),
                tint = catColors[item.category]!!.bg,
                contentDescription = "Localized description",
                modifier = Modifier.height(16.dp).width(16.dp)
            )
        },
        supportingText = {
            BadgedBox(badge = {}) {
                Badge(
                    containerColor = catColors[item.category]!!.bg,
                    contentColor = catColors[item.category]!!.txt){
                    Text("${item.category}",
                        modifier = Modifier.semantics {
                            contentDescription = "Category of item is ${item.category}"
                        }
                    )
                }
            }
        },
        trailingContent = {
            IconButton({
                vm.deleteItem(item)
                msg("Deleting item ${item.name} from category ${item.category}")
            }){
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemDialog(closeDialog: () -> Unit,
                  vm: PurchaseViewModel,
                  snackbarMsg : (String) -> Unit
){
    var itemDescription by remember {mutableStateOf("") }
    var categoryExpanded by remember { mutableStateOf(false) }
    var categorySelected by remember { mutableStateOf("") }

    val categoryList = enumValues<ItemCategory>().map{it.toString()}.toList()

    Dialog(onDismissRequest = { closeDialog() }){
        Log.d("ADDING ITEM", "AddItemDialog > It opens")
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                //.wrapContentHeight()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                // Item description
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                    horizontalArrangement = Arrangement.Center){

                    TextField(
                        value = itemDescription,
                        onValueChange = {
                            itemDescription = it
                        },
                        label = {Text("Item description")}
                    )
                }

                // COMBO BOX
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                    horizontalArrangement = Arrangement.Center
                ){
                    //ComboBox(enumValues<ItemCategory>().map{it.toString()}.toList(),ItemCategory.NONE.toString(), {})
                    Box(modifier = Modifier
                        .fillMaxWidth()){

                        ExposedDropdownMenuBox(
                            expanded = categoryExpanded,
                            onExpandedChange = {categoryExpanded = !categoryExpanded}
                        ){
                            TextField(
                                value = categorySelected,
                                onValueChange = {},
                                readOnly = true,
                                label = {Text("Item category")},
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                                modifier = Modifier.menuAnchor()
                            )

                            ExposedDropdownMenu(
                                expanded = categoryExpanded,
                                onDismissRequest = { categoryExpanded = false },
                                modifier = Modifier
                                    .verticalScroll(rememberScrollState())
                                    .height(150.dp)
                            ) {
                                categoryList.forEach { item ->
                                    DropdownMenuItem(
                                        text = { Text(text = item) },
                                        onClick = {
                                            categorySelected = item
                                            categoryExpanded = false
                                        },
                                        modifier = Modifier.height(30.dp)
                                    )
                                }
                            }
                        }
                    }
                }


                // Ultima riga di bottoni
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(
                        onClick = { closeDialog() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Dismiss")
                    }
                    TextButton(
                        onClick = {
                            vm.addItem(PurchasableItem(itemDescription, ItemCategory.valueOf(categorySelected), false))
                            closeDialog()
                            snackbarMsg("Added new item $itemDescription in category $categorySelected")
                                  },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Confirm")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewItemButton(setOpen: () -> Unit){
    Box(modifier = Modifier
        .fillMaxWidth()
        .clickable(onClick = setOpen)){
        ListItem(
            headlineText = {
                Text(text = "Add new item", fontStyle = FontStyle.Italic)
            },
            leadingContent = {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "Add new item"
                )
            },
        )
        Divider()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Header(onClick : () -> Unit){
    CenterAlignedTopAppBar(
        title = {
            Text(text = "COMPOSING MODE",
                maxLines = 1,
                textAlign = TextAlign.Center)
        },
        navigationIcon = {
            IconButton(onClick = onClick ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back to shopping mode"
                )
            }
        },
    )
}