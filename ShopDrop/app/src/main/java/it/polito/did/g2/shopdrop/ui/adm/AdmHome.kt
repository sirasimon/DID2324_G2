@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package it.polito.did.g2.shopdrop.ui.adm

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesomeMosaic
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.StoreMallDirectory
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import it.polito.did.g2.shopdrop.MainViewModel
import it.polito.did.g2.shopdrop.R
import it.polito.did.g2.shopdrop.navigation.Screens
import kotlinx.coroutines.launch

enum class AdminViews { USR, ORD, LKR, STO, PRD, DBG }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ADMHomeScreen(navController : NavController, viewModel: MainViewModel){

    var checked by remember { mutableStateOf(false) }
    var dialogOpen by remember { mutableStateOf(false) }
    val viewNav = rememberNavController()

    var onClick : (()->Unit) = {}

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    //DRAWER
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val drawerScope = rememberCoroutineScope()

    var currentView by remember {
        mutableStateOf(AdminViews.PRD)
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            ModalDrawerSheet {
                Text("COLLECTIONS", modifier = Modifier.padding(16.dp))

                enumValues<AdminViews>().toList().filter { it!=AdminViews.DBG }.forEach {
                    NavigationDrawerItem(
                        label = { Text(getTitle(it).capitalize()) },
                        icon = { Icon(getIcon(it), contentDescription = null) },
                        selected = it==currentView,
                        onClick = {
                            currentView = it
                            drawerScope.launch {
                                drawerState.apply {
                                    close()
                                }
                            }
                            viewNav.navigate(it.toString())
                        }
                    )
                }

                HorizontalDivider(thickness = 2.dp)

                Text("DEBUG", modifier = Modifier.padding(16.dp))
                NavigationDrawerItem(
                    label = { Text(getTitle(AdminViews.DBG).capitalize()) },
                    icon = { Icon(getIcon(AdminViews.DBG), contentDescription = null) },
                    selected = AdminViews.DBG==currentView,
                    onClick = {
                        currentView = AdminViews.DBG
                        drawerScope.launch {
                            drawerState.apply {
                                close()
                            }
                        }
                        viewNav.navigate(AdminViews.DBG.toString())
                    }
                )

                HorizontalDivider(thickness = 2.dp)

                TextButton(
                    onClick = {
                        viewModel.logout()
                        navController.navigate(Screens.Login.route)
                    }
                ) {
                    Text(stringResource(R.string.btn_log_out).capitalize())
                }
            }
        },
    ) {
        Scaffold(
            topBar = {
                     ADMTopBar(title = getTitle(currentView), icon = getIcon(currentView)) {
                         drawerScope.launch {
                             drawerState.apply {
                                 if(isClosed) open() else close()
                             }
                         }
                     }
            },
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
        ) { paddingValues ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                color = MaterialTheme.colorScheme.background
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {

                    NavHost(navController = viewNav, startDestination = "USR"){
                        
                        composable("PRD") {
                            ProductsScreen(viewModel)
                        }

                        composable("USR") {
                            UsersScreen(viewModel)
                        }

                        composable("ORD") {
                            OrdersScreen(viewModel)
                        }

                        composable("STO") {
                            StoresScreen(viewModel)
                        }

                        composable("LKR") {
                            LockersScreen(viewModel)
                        }

                        composable("DBG"){
                            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                                Button(
                                    onClick = {
                                        onClick = { viewModel.debugDBReset(MainViewModel.db_reset.ORDERS) }
                                        dialogOpen = true
                                    }) {
                                    Text("RESET ORDERS DB")
                                }
                            }
                        }
                    }
                }

                if(dialogOpen){
                    Dialog(onDismissRequest = { dialogOpen=false; onClick = {} }) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(375.dp)
                                .padding(16.dp),
                            shape = RoundedCornerShape(16.dp),
                        ){
                            Text(text = "ARE YOU SURE???")
                            Row {
                                Button(onClick = { dialogOpen=false; onClick = {} }) {
                                    Text(text = "CANCEL")
                                }

                                Button(onClick = onClick) {
                                    Text(text = "PROCEED")
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
fun ADMTopBar(title: String, icon: ImageVector, openMenu: ()-> Unit){
    CenterAlignedTopAppBar(
        title = {
            Text(title.uppercase())
        },
        navigationIcon = {
            IconButton(
                onClick = openMenu
            ) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = null
                )
            }
        },
        actions = {
            Icon(
                imageVector = icon,
                contentDescription = null
            )
        }
    )
}

@Composable
fun ProductsScreen(viewModel: MainViewModel) {
    Text("PRODUCTS")
}

@Composable
fun StoresScreen(viewModel: MainViewModel) {
    
    viewModel.storesList.observeAsState().value?.forEach { 
        ListItem(headlineContent = { Text("${it.id} @ ${it.address}") })
        HorizontalDivider(thickness = 2.dp)
    }
}

@Composable
fun UsersScreen(viewModel: MainViewModel) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)) {
        Text("Total users: ${viewModel.usersList.observeAsState().value?.size}")

        viewModel.usersList.observeAsState().value?.forEach {
            Card(Modifier.fillMaxWidth()) {

            }
        }
    }
}

@Composable
fun OrdersScreen(viewModel: MainViewModel) {
    viewModel.ordersList.observeAsState().value?.forEach {
        Card(Modifier.fillMaxWidth()){
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                Text("${it.id}")
                Text("${it.carrierID}")
            }
            HorizontalDivider(thickness = 2.dp)

            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                Text("${it.storeID}")
                Text("->")
                Text("${it.lockerID}")
                Text("->")
                Text("${it.customerID}")
            }
        }
    }
}

@Composable
fun LockersScreen(viewModel: MainViewModel) {

    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)) {
        viewModel.lockersList.observeAsState().value?.forEach {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ){
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp))
                {
                    Row(Modifier.fillMaxWidth()) {
                        Text("${it.id} – ${it.name?.uppercase()}")
                    }

                    Text(it.address)

                    HorizontalDivider(thickness = 2.dp, modifier = Modifier.padding(vertical = 8.dp))

                    var spacing = 90.dp

                    Row(modifier = Modifier.fillMaxWidth()){
                        Text("Online", textAlign = TextAlign.Center, modifier = Modifier.width(spacing))
                        Text("Working", textAlign = TextAlign.Center, modifier = Modifier.width(spacing))
                        Text("Empty", textAlign = TextAlign.Center, modifier = Modifier.width(spacing))
                        Text("Full", textAlign = TextAlign.Center, modifier = Modifier.width(spacing))
                    }
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()){
                        //Text("${it.isOnline}", modifier = Modifier.width(spacing))
                        RadioButton(selected = it.isOnline, onClick = { /*TODO*/ }, modifier = Modifier.width(spacing))
                        //Text("${it.isWorking}", modifier = Modifier.width(spacing))
                        RadioButton(selected = it.isWorking, onClick = { /*TODO*/ }, modifier = Modifier.width(spacing))
                        //Text("${it.isEmpty}", modifier = Modifier.width(spacing))
                        RadioButton(selected = it.isEmpty, onClick = { /*TODO*/ }, modifier = Modifier.width(spacing))
                        //Text("${it.isFull}", modifier = Modifier.width(spacing))
                        RadioButton(selected = it.isFull, onClick = { /*TODO*/ }, modifier = Modifier.width(spacing))
                    }

                    HorizontalDivider(thickness = 2.dp, modifier = Modifier.padding(vertical = 8.dp))
                    Text("Total compartments: ${it.compartments?.size}")

                    spacing = 50.dp
                    Row(modifier = Modifier.fillMaxWidth()){
                        Text("#", modifier = Modifier.width(20.dp))
                        Text("Wkg", textAlign = TextAlign.Center, modifier = Modifier.width(spacing))
                        Text("Ept", textAlign = TextAlign.Center, modifier = Modifier.width(spacing))
                        Text("Opn", textAlign = TextAlign.Center, modifier = Modifier.width(spacing))
                        Text("Avb", textAlign = TextAlign.Center, modifier = Modifier.width(spacing))
                        Text("oID", modifier = Modifier.width(spacing))
                    }

                    it.compartments?.forEach {
                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
                            Text("${it.id}", modifier = Modifier.width(20.dp))
                            /*
                            Text("${it.isWorking}", modifier = Modifier.width(spacing))
                            Text("${it.isEmpty}", modifier = Modifier.width(spacing))
                            Text("${it.isOpen}", modifier = Modifier.width(spacing))
                            Text("${it.isAvailable}", modifier = Modifier.width(spacing))
                            Text("${it.orderID}")
                            */
                            RadioButton(selected = it.isWorking, onClick = { /*TODO*/ }, modifier = Modifier.width(spacing))
                            RadioButton(selected = it.isEmpty, onClick = { /*TODO*/ }, modifier = Modifier.width(spacing))
                            RadioButton(selected = it.isOpen, onClick = { /*TODO*/ }, modifier = Modifier.width(spacing))
                            RadioButton(selected = it.isAvailable, onClick = { /*TODO*/ }, modifier = Modifier.width(spacing))
                            Text("${it.orderID}")
                        }
                    }
                }
            }
        }

    }
}

fun getTitle(v : AdminViews): String{
    return when(v){
        AdminViews.DBG -> "debug"
        AdminViews.LKR -> "lockers"
        AdminViews.ORD -> "orders"
        AdminViews.PRD -> "products"
        AdminViews.STO -> "stores"
        AdminViews.USR -> "users"
    }
}

fun getIcon(v : AdminViews): ImageVector {
    return when(v){
        AdminViews.DBG -> Icons.Filled.Computer
        AdminViews.LKR -> Icons.Filled.AutoAwesomeMosaic
        AdminViews.ORD -> Icons.Filled.DeliveryDining
        AdminViews.PRD -> Icons.Filled.Fastfood
        AdminViews.STO -> Icons.Filled.StoreMallDirectory
        AdminViews.USR -> Icons.Filled.Person
    }
}