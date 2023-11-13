package com.example.shoppinglist

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.snapTo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
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
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

const val ANIM_TIMING = 500
val ADD_COLOR = Color(0xFF01B95A)
val DEL_COLOR = Color(0xFFDD0021)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ShoppingScreen(navController : NavController, vm : PurchaseViewModel){
    // STRUTTURE DATI
    val items by vm.itemsLiveData.observeAsState()

    // Dialog filtri e operazioni di filtraggio
    val (openFilterDialog, setOpen) = remember { mutableStateOf(false) }
    val filterList = remember { mutableListOf<ItemCategory>() }

    // Progress bar
    val currentProgress = remember { Animatable(vm.getProgress()) }
    val progressScope = rememberCoroutineScope() // Create a coroutine scope

    var completed by remember{mutableStateOf(false)}

    val defaultColor = Color(95,95,95)
    val endColor = Color.Green
    val progressColor by animateColorAsState(
        if(completed) endColor else defaultColor,
        animationSpec = tween(ANIM_TIMING, easing = EaseInOut),
        label = "ColorChange"
    )
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold (
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                title = {
                    Text(text = "SHOPPING MODE",
                        maxLines = 1,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold)
                },
                actions = {
                    IconButton(onClick = { setOpen(true) } ) {
                        if(items?.isNotEmpty() == true) {
                            Icon(
                                painter = painterResource(R.drawable.baseline_filter_list_24),
                                contentDescription = "Search"
                            )
                        }
                    }
                },
                scrollBehavior = scrollBehavior
            )
            //Header(onFilter = { setOpen(true) }, hasItems = items?.isNotEmpty())
        },
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

            // Se la lista è vuota, mostra messaggio
            if(items?.isEmpty() == true || items == null){
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

            // Se la lista non è vuota, riempila
            }else {
                Column(
                    Modifier
                        .fillMaxSize()
                ) {

                    val catList: List<ItemCategory> =
                        if (filterList.isNotEmpty())
                            filterList
                        else
                            vm.getCategories()

                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        progress = currentProgress.value,
                        color = progressColor
                    )

                    LazyColumn(){
                        catList.forEach {
                            stickyHeader { CategoryHeader(label = it.name.replace("_"," ")) }

                            items( items = items!!.toList().filter{ item -> item.category == it}
                                .sortedWith(compareBy<PurchasableItem> { it.purchased }
                                    .thenBy { it.name }
                                ),
                                key={it.name}
                            ){
                                ShoppingListItem(it, vm, Modifier.animateItemPlacement(animationSpec = tween(ANIM_TIMING, easing = EaseInOut) )) {
                                    progressScope.launch {
                                        if(vm.getProgress()==1f){
                                            completed = true
                                        }else if(vm.getPurchasedNum() != vm.getTotalItems()){
                                            completed = false
                                        }

                                        currentProgress.animateTo(vm.getProgress(), animationSpec = tween(ANIM_TIMING, easing = EaseInOut))
                                    }
                                }
                            }
                        }
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
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryHeader(label: String, modifier: Modifier = Modifier){
    Text(
        text = label,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(16.dp)
    )
}

enum class DragAnchors {
    Start,
    Action,
    End,
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@Composable
fun ShoppingListItem(item: PurchasableItem, vm : PurchaseViewModel, modifier : Modifier = Modifier, updateProgress: () -> Unit){

    //val targetItem by vm.itemsLiveData.observeAsState()
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current

    val defaultActionSize = 80.dp
    val actionSizePx = with(density) { defaultActionSize.toPx() }
    val endActionSizePx = with(density) { (defaultActionSize * 2).toPx() }

    val draggableAnchors = DraggableAnchors {
        DragAnchors.Start at 0f
        DragAnchors.Action at actionSizePx
        DragAnchors.End at endActionSizePx
    }

    val state = remember {
        AnchoredDraggableState(
            initialValue = DragAnchors.Start,
            anchors = draggableAnchors,
            positionalThreshold = { distance: Float -> distance * 0.5f },
            velocityThreshold = { with(density) { 100.dp.toPx() } },
            animationSpec = tween()
        )
    }

    when(state.currentValue){
        DragAnchors.Start -> {
            //TODO
        }

        DragAnchors.Action -> {
            LaunchedEffect(state){
                //state.snapTo(DragAnchors.Start)
            }
        }

        DragAnchors.End -> {
            LaunchedEffect(state){
                state.snapTo(DragAnchors.Start)
                vm.setPurchase(item, !item.purchased)
                updateProgress()
            }

            Log.d("ShoScr", "ShoppingListItem > Updated item is $item\n(also: ${vm.getItems()})")
        }
    }

    Box(                            //Contenitore che contiene il draggabile
        modifier = modifier
            .fillMaxWidth()
            .height(45.dp)
            .clip(RectangleShape)
            .background(
                if (item.purchased)
                    DEL_COLOR
                else
                    ADD_COLOR
            )
    ){
        //TODO
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.CenterStart),
        ) {
            Box(
                modifier = Modifier
                    .width(defaultActionSize)
                    .fillMaxSize()
                    .offset {
                        IntOffset(((state.requireOffset() - actionSizePx)).roundToInt(), 0)
                    },
                contentAlignment = Alignment.Center
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        modifier = Modifier
                            .padding(top = 10.dp, bottom = 4.dp)
                            .padding(horizontal = 20.dp)
                            .size(24.dp),
                        painter =
                        if(item.purchased)
                            painterResource(R.drawable.baseline_remove_shopping_cart_24)
                        else
                            painterResource(R.drawable.baseline_shopping_cart_24),
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        }

        ListItem(
            headlineText = {
                Text(text = item.name,
                    //textDecoration = (if(targetItem?.find{it.name==item.name}?.purchased == true) TextDecoration.LineThrough else null ),
                    //fontStyle = (if(targetItem?.find{it.name==item.name}?.purchased == true) FontStyle.Italic else null )
                    textDecoration = (if(item.purchased) TextDecoration.LineThrough else null ),
                    fontStyle = (if(item.purchased) FontStyle.Italic else null )
                ) },

            modifier = Modifier
                .height(45.dp)
                .offset {
                    IntOffset(
                        x = state
                            .requireOffset()
                            .roundToInt(),
                        y = 0,
                    )
                }
                .anchoredDraggable(
                    state = state,
                    orientation = Orientation.Horizontal,
                    reverseDirection = false
                )
        )
    }
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
                            label = { Text(text = it.name.replace("_", " "))},
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