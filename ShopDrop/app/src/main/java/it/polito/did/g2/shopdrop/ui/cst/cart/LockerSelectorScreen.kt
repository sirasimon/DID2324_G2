package it.polito.did.g2.shopdrop.ui.cst.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.FmdGood
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import it.polito.did.g2.shopdrop.MainViewModel
import it.polito.did.g2.shopdrop.R
import it.polito.did.g2.shopdrop.ui.cst.common.TopBar
import it.polito.did.g2.shopdrop.ui.cst.common.WIPMessage
import it.polito.did.g2.shopdrop.ui.cst.common.performDevMsg

enum class ViewTab {MAP, LIST}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LockerSelectorScreen(navController: NavController, viewModel: MainViewModel){
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    val listViewNC = rememberNavController()
    var selectedTab by remember { mutableStateOf(0) }

    //LOCKER SELECTOR
    var selectedLocker by remember{mutableStateOf(viewModel.currOrder.value?.lockerID)}

    // SNACKBAR
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    Scaffold(
        topBar = { TopBar({navController.navigateUp()}, stringResource(id = R.string.title_select_locker), scrollBehavior) },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        floatingActionButton = { FabSelectLocker(navController, viewModel, selectedLocker) },
        floatingActionButtonPosition = FabPosition.Center,
        snackbarHost = { WIPMessage(snackbarHostState) }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = "",
                    placeholder = { Text(text = stringResource(R.string.placeholder_search_locker).capitalize(), fontStyle = FontStyle.Italic)},
                    enabled = false,
                    onValueChange = {},
                    shape = RoundedCornerShape(8.dp),
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )

                SecondaryTabRow(
                    selectedTabIndex = ViewTab.MAP.ordinal
                ) {
                    Tab(
                        selected = selectedTab == ViewTab.MAP.ordinal,
                        onClick = { selectedTab = ViewTab.MAP.ordinal; listViewNC.navigate(ViewTab.MAP.toString()) },
                        text = { Text(stringResource(id = R.string.tab_map).capitalize(), style = MaterialTheme.typography.headlineSmall) }
                    )

                    Tab(
                        selected = selectedTab == ViewTab.LIST.ordinal,
                        onClick = { selectedTab = ViewTab.LIST.ordinal; listViewNC.navigate(ViewTab.LIST.toString()) },
                        text = { Text(stringResource(id = R.string.tab_list).capitalize(), style = MaterialTheme.typography.headlineSmall) }
                    )
                }

                Column(Modifier.fillMaxWidth()){
                    NavHost(navController = listViewNC, startDestination = ViewTab.MAP.toString()){
                        composable(ViewTab.MAP.toString()){
                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier
                                        .background(Color.Yellow)
                                        .fillMaxWidth()
                                        .height(400.dp)
                                        .clickable {
                                            performDevMsg(
                                                scope,
                                                snackbarHostState,
                                                context
                                            )
                                        }
                                ){
                                    Text("[LOCKER MAP]")
                                }

                                Row(Modifier.padding(horizontal = 16.dp, vertical = 24.dp)){
                                    Icon(Icons.Outlined.Lock, contentDescription = null)
                                    Text(
                                        viewModel.lockersList.value?.find { it.id == selectedLocker }?.name?:"[LOCKER NAME]",
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Row(Modifier.padding(horizontal = 16.dp)){
                                    Icon(Icons.Outlined.FmdGood, contentDescription = null)
                                    Text(
                                        viewModel.lockersList.value?.find { it.id == selectedLocker }?.address?:"[LOCKER ADDRESS]",
                                        maxLines = 1,
                                        softWrap = true
                                    )
                                }
                                Spacer(Modifier.height(4.dp))
                                Row(Modifier.padding(horizontal = 16.dp)){
                                    Icon(Icons.Outlined.AccessTime, contentDescription = null)
                                    Text("${stringResource(id = R.string.available_in).capitalize()} 40-45 ${stringResource(id = R.string.minutes)}")
                                }
                            }
                        }

                        composable(ViewTab.LIST.toString()){
                            Column(Modifier.fillMaxWidth()) {
                                viewModel.lockersList.observeAsState().value?.forEach {
                                    ListItem(
                                        headlineContent = {
                                            Column(){
                                                Text("${it.id} | ${it.name?.uppercase()}")
                                                Text(it.address, maxLines = 1, softWrap = true)
                                            }
                                        },
                                        trailingContent = {
                                            RadioButton(
                                                selected = it.id == selectedLocker,
                                                onClick = { selectedLocker = it.id },
                                                enabled = it.isWorking && !it.isFull
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FabSelectLocker(navController: NavController, viewModel: MainViewModel, targetLocker: String?) {
    ExtendedFloatingActionButton(
        onClick = {
            if(targetLocker!=null){
                viewModel.setNewLocker(targetLocker)
                navController.navigateUp()
            }
                  },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
    ) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()){
            Text(text= stringResource(id = R.string.btn_select_locker).capitalize())
            Text(stringResource(id = R.string.btn_order_now).capitalize())
        }
    }
}