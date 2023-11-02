package com.example.shoppinglist

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingScreen(navController : NavController, vm : PurchaseViewModel){
    // STRUTTURE DATI

    // Dialog filtri e operazioni di filtraggio
    var (openFilterDialog, setOpen) = remember { mutableStateOf(false) }
    val filterList = remember { mutableListOf<ItemCategory>() }

    // Progress bar
    var currentProgress by remember { mutableFloatStateOf(vm.getProgress()) }
    val progressScope = rememberCoroutineScope() // Create a coroutine scope


    Scaffold (
        topBar = { Header(onFilter = { setOpen(true) }, hasItems = vm.itemsLiveData.value?.isNotEmpty())},
        floatingActionButton = { EditButton(onClick = { navController.navigate("ComposingScreen") }) },
        floatingActionButtonPosition = FabPosition.End
    ){
            paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            if(vm.itemsLiveData.value?.isEmpty() == true || vm.itemsLiveData.value == null){
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    //verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .offset(y = LocalConfiguration.current.screenHeightDp.dp / 3)
                ) {
                    Text(
                        text = "Your shopping list is empty\n\nTry adding new items\nby tapping on the edit icon below",
                        textAlign = TextAlign.Center,
                        fontStyle = FontStyle.Italic,
                        color = Color(127, 127, 127)
                    )
                }
            }else {
                Column(Modifier.fillMaxSize()) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        progress = currentProgress
                    )

                    var catList: List<ItemCategory>

                    if (filterList.isNotEmpty())
                        catList = filterList
                    else
                        catList = vm.getCategories()

                    for (cat in catList) {
                        CategorySection(cat)
                        vm.getItems()
                            .filter { item -> item.category == cat }
                            .forEach { item ->
                                ShoppingListItem(item, vm) {
                                    progressScope.launch {
                                        currentProgress = vm.getProgress()
                                    }
                                }
                            }
                        Divider()
                    }

                    if (openFilterDialog) {
                        FilterDialog(
                            closeDialog = { setOpen(false) },
                            filterList = filterList,
                            updateFilterList = { it, add ->
                                if (it != null)
                                    if (add) filterList.add(it)
                                    else filterList.remove(it)
                                else
                                    filterList.clear()
                            })
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Header(onFilter : () -> Unit, hasItems: Boolean?){
    CenterAlignedTopAppBar(
        title = {
            Text(text = "SHOPPING MODE",
                maxLines = 1,
                textAlign = TextAlign.Center)
        },
        actions = {
            IconButton(onClick = onFilter ) {
                if(hasItems!=null && hasItems) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_filter_list_24),
                        contentDescription = "Search"
                    )
                }
            }
        }
    )
}

@Composable
fun CategorySection(cat: ItemCategory){
    //TODO Prendi la lista salvata in vm, filtrala per stampare le categorie e per ogni categoria stampa le voci
    Text(text = "$cat",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = catColors[cat]!!.txt,
        modifier = Modifier
            .background(catColors[cat]!!.bg)
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
        headlineText = {
            Text(text = item.name,
                textDecoration = (if(checked) TextDecoration.LineThrough else null )
            ) },
        leadingContent = {
            Checkbox(checked = checked, onCheckedChange = {
                item.purchased = !checked
                setChecked(!checked)
                vm.setPurchase(item, !checked)
                updateProgress()

                Log.d("ShoScr", "ShoppingListItem > Updated item is $item\n(also: ${vm.getItems()})")})
        },
        modifier = Modifier.height(45.dp)
            //.draggable()
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FilterDialog(closeDialog: () -> Unit,
                 filterList: List<ItemCategory>,
                 updateFilterList: (ItemCategory?, Boolean) -> Unit){

    Dialog(onDismissRequest = { closeDialog() }){
        Log.d("FILTER", "FilterDialog > It opens")

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                    horizontalArrangement = Arrangement.Center){

                    Text("Select categories")
                }

                FlowRow(modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                    horizontalArrangement = Arrangement.Center){

                    ItemCategory.values().forEach {
                        var selected by remember { mutableStateOf(filterList.contains(it)) }

                        FilterChip(
                            selected = selected,
                            onClick = {
                                updateFilterList(it, !selected)
                                selected = !selected
                                      },
                            label = { Text(text = it.name)},
                            leadingIcon = if (filterList.contains(it)) {
                                {
                                    Icon(
                                        imageVector = Icons.Filled.Done,
                                        contentDescription = "Done icon",
                                        modifier = Modifier.size(FilterChipDefaults.IconSize)
                                    )
                                }
                            } else {
                                null
                            },
                        )
                    }


                }

                // Ultima riga di bottoni
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = {
                            updateFilterList(null, false)
                            closeDialog()
                        },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Clear all")
                    }
                    TextButton(
                        onClick = {
                            closeDialog()
                        },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Close")
                    }
                }
            }
        }
    }
}

/***
 * Bottone che permette di passare alla schermata di modifica della lista
 */
@Composable
fun EditButton(onClick : () -> Unit){
    FloatingActionButton(
        onClick = { onClick() },
        shape = CircleShape,
        modifier = Modifier
            .padding(16.dp)
    ) {
        Icon(Icons.Filled.Edit, "Go to composing mode")
    }
}