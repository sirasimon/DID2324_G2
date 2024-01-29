package it.polito.did.g2.shopdrop.ui.cst.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
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

    // SNACKBAR
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    Scaffold(
        topBar = { TopBar(navController, stringResource(id = R.string.title_select_locker), scrollBehavior) },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        floatingActionButton = { FabSelectLocker(navController, viewModel) },
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
                    .padding(16.dp)
            ) {
                Text("[Searchbar]") //TODO: search bar for locker (not working)

                SecondaryTabRow(
                    selectedTabIndex = ViewTab.MAP.ordinal,
                    indicator = {}
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
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .background(Color.LightGray, RoundedCornerShape(16.dp))
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clickable { performDevMsg(scope, snackbarHostState, context) }
                            ){
                                Text("[Locker selector]")
                            }
                        }

                        composable(ViewTab.LIST.toString()){
                            Column(Modifier.fillMaxWidth()) {
                                ListItem(
                                    headlineContent = { Text(text = "Locker #1") },
                                    trailingContent = {
                                        RadioButton(
                                            selected = true,
                                            onClick = { /*TODO*/ })
                                    },
                                    modifier = Modifier.clickable {
                                        //TODO
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

@Composable
private fun FabSelectLocker(navController: NavController, viewModel: MainViewModel) {
    ExtendedFloatingActionButton(
        onClick = { navController.navigateUp() /*TODO*/ },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
    ) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()){
            Text(text="${String.format("%.2f", viewModel.getCartTotal())} €")
            Text(stringResource(id = R.string.btn_order_now).capitalize())
        }
    }
}