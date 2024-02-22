package it.polito.did.g2.shopdrop.ui.crr.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import it.polito.did.g2.shopdrop.MainViewModel
import it.polito.did.g2.shopdrop.R
import it.polito.did.g2.shopdrop.data.Order
import it.polito.did.g2.shopdrop.data.OrderStateName
import it.polito.did.g2.shopdrop.navigation.Screens

private enum class CrrFilterChip{COLL, DELI}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CRRHomeScreen(navController : NavController, viewModel: MainViewModel){
    val screenHeight = with(LocalDensity.current) { LocalConfiguration.current.screenHeightDp.dp }
    val profileHeight = (screenHeight * 0.20f)

    val listNavController = rememberNavController()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    val chipLabels = mapOf(
        CrrFilterChip.COLL to R.string.chip_to_collect,
        CrrFilterChip.DELI to R.string.chip_to_deliver
    )

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            //TODO: ID card

            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                ProfileSection(profileHeight)

                val selectedTabIndex = remember { mutableStateOf(0) }

                var activeTab by remember { mutableStateOf(CrrFilterChip.COLL) }
                Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()){

                    enumValues<CrrFilterChip>().forEach {
                        FilterChip(
                            onClick = {activeTab = it; listNavController.navigate(it.toString())},
                            label = { Text(stringResource(id = chipLabels[it]!!).capitalize()) },
                            selected = activeTab==it,
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    }
                }

                Column {
                    NavHost(navController = listNavController, startDestination = CrrFilterChip.COLL.toString()){
                        composable(CrrFilterChip.COLL.toString()) {
                            viewModel.ordersList.observeAsState().value
                                ?.filter { it.carrierID == viewModel.currUser.value?.uid && it.stateList?.map{s -> s.state}?.contains(OrderStateName.RECEIVED)==true && it.stateList?.map{s -> s.state}?.contains(OrderStateName.CARRIED)==false }
                                ?.forEach{
                                    val onMore = {id : String -> /*navController.navigate(Screens.CrrOrderDetail.route+"/$id")*/ }  //TODO
                                    val onScan = {id : String -> navController.navigate(Screens.CrrCollectionCameraScreen.route+"/$id")}
                                    OrderCard(it, onMore, onScan)
                                }
                        }
                        composable(CrrFilterChip.DELI.toString()){
                            viewModel.ordersList.observeAsState().value
                                ?.filter { it.carrierID == viewModel.currUser.value?.uid && it.stateList?.map{s -> s.state}?.contains(OrderStateName.RECEIVED)==true && it.stateList?.map{s -> s.state}?.contains(OrderStateName.CARRIED)==true && it.stateList?.map{s -> s.state}?.contains(OrderStateName.AVAILABLE)==false}
                                ?.forEach{
                                    val onMore = {id : String -> /*navController.navigate(Screens.CrrOrderDetail.route+"/$id")*/ }  //TODO
                                    val onScan = {id : String -> navController.navigate(Screens.CrrDepositCameraScreen.route+"/$id")}
                                    OrderCard(it, onMore, onScan)
                                }
                        }
                    }
                }

                Button(onClick = {navController.navigate(Screens.CrrProfileScreen.route)}){
                    Text(text = "Go to profile")
                }

                TextButton(
                    onClick = {
                        viewModel.logout()
                        navController.navigate(Screens.Login.route)
                    }
                ) {
                    Text(stringResource(R.string.btn_log_out).capitalize())
                }
            }
        }

    }
}

@Composable
fun ProfileSection(profileHeight:Dp) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(profileHeight)
            .background(Color.Green),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.shopdrop_logo),
            contentDescription = null,
            modifier = Modifier
                .padding(16.dp)
                .size(64.dp),
            contentScale = ContentScale.Crop
        )
        Text(
            text = "Benvenuto Giovanni",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.weight(1f),
            color = Color.White
        )
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun OrderCard(order: Order, onMore: (String)->Unit, onScan : (String)->Unit) {
    Card(
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth()
            .clickable{onMore(order.id!!)},
        shape = MaterialTheme.shapes.medium,
        content = {
            ListItem(
                headlineContent = {
                    Column {
                        Text("${stringResource(id = R.string.order).capitalize()} # ${order.id}")
                    }
                },
                trailingContent = {
                    IconButton(
                        onClick = { onScan(order.id!!) } //TODO: aggiungere qui l'argomento alla navigazione dell'ID dell'ordine
                    ){
                        Icon(Icons.Filled.QrCodeScanner, contentDescription = null)
                    }
                }
            )
        }
    )
}

