package it.polito.did.g2.shopdrop.ui.cst.orders

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import it.polito.did.g2.shopdrop.MainViewModel
import it.polito.did.g2.shopdrop.R
import it.polito.did.g2.shopdrop.data.Order
import it.polito.did.g2.shopdrop.data.OrderStateName
import it.polito.did.g2.shopdrop.data.TabScreen
import it.polito.did.g2.shopdrop.navigation.Screens
import it.polito.did.g2.shopdrop.ui.cst.common.ArrowRt
import it.polito.did.g2.shopdrop.ui.cst.common.BottomBar
import it.polito.did.g2.shopdrop.ui.cst.common.TopBar

private enum class CstFilterChip{PEND, ARCH, CANC}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CstOrdersHistory(navController : NavController, viewModel : MainViewModel){
    val listNavController = rememberNavController()

    val currentTab = TabScreen.PROFILE

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    val chipLabels = mapOf(
        CstFilterChip.PEND to R.string.chip_pending,
        CstFilterChip.ARCH to R.string.chip_collected,
        CstFilterChip.CANC to R.string.chip_cancelled
    )

    Scaffold(
        topBar = { TopBar(navController, stringResource(id = R.string.title_history), scrollBehavior) },
        bottomBar = { BottomBar(currentTab, navController, viewModel) },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                var activeTab by remember { mutableStateOf(CstFilterChip.PEND) }
                Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()){

                    enumValues<CstFilterChip>().forEach {
                        FilterChip(
                            onClick = {activeTab = it; listNavController.navigate(it.toString())},
                            label = { Text(stringResource(id = chipLabels[it]!!).capitalize())},
                            selected = activeTab==it,
                            shape = RoundedCornerShape(16.dp)
                        )
                    }
                }

                Column {
                    NavHost(navController = listNavController, startDestination = CstFilterChip.PEND.toString()){
                        composable(CstFilterChip.PEND.toString()) {
                            ListedOrders(
                                viewModel.ordersList.observeAsState().value?.filter {
                                    it.customerID==viewModel.currUser.value?.uid &&
                                            it.stateList?.last()?.state != OrderStateName.COLLECTED &&
                                            it.stateList?.last()?.state != OrderStateName.CANCELLED
                                }?.toMutableList(),
                                viewModel
                            ) { id ->
                                navController.navigate(
                                    Screens.CstOrderDetail.route + "/$id"
                                )
                            }
                        }
                        composable(CstFilterChip.ARCH.toString()){
                            ListedOrders(
                                viewModel.ordersList.observeAsState().value?.filter {
                                    it.customerID==viewModel.currUser.value?.uid &&
                                            it.stateList?.last()?.state == OrderStateName.COLLECTED
                                }?.toMutableList(),
                                viewModel
                            ) {id ->
                                navController.navigate(
                                    Screens.CstOrderDetail.route + "/${id}"
                                )
                            }
                        }
                        composable(CstFilterChip.CANC.toString()){
                            ListedOrders(
                                viewModel.ordersList.observeAsState().value?.filter {
                                    it.customerID==viewModel.currUser.value?.uid &&
                                            it.stateList?.last()?.state == OrderStateName.CANCELLED
                                }?.toMutableList(),
                                viewModel
                            ) {id ->
                                navController.navigate(
                                    Screens.CstOrderDetail.route + "/${id}"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ListedOrders(list: MutableList<Order>?, viewModel: MainViewModel, onClick: (String) -> Unit) {

    Column(Modifier.fillMaxWidth()){
        if(!list.isNullOrEmpty()){
            for (item in list) {
                Card(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .height(64.dp)
                        .clickable(onClick = { onClick(item.id!!) })
                ) {
                    ListItem(
                        headlineContent = {
                            Column {
                                Text("${stringResource(id = R.string.order_dated).capitalize()} ${viewModel.getDateString( item.stateList?.first()?.timestamp!! )}")
                                Text("#${item.id} – ${item.items?.count()} ${stringResource(id = if(item.items?.count()==1) R.string.txt_item else R.string.txt_items)}")
                            }
                        },
                        trailingContent = { ArrowRt() }
                    )
                }
            }
        }else{
            Text(stringResource(id = R.string.txt_no_orders_yet))
        }
    }
}
