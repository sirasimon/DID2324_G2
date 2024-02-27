@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class
)

package it.polito.did.g2.shopdrop.ui.adm

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesomeMosaic
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.StoreMallDirectory
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import it.polito.did.g2.shopdrop.MainViewModel
import it.polito.did.g2.shopdrop.R
import it.polito.did.g2.shopdrop.navigation.Screens
import kotlinx.coroutines.launch

enum class AdminViews { USR, ORD, LKR, STO, PRD, DBG }

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ADMHomeScreen(navController : NavController, viewModel: MainViewModel){

    val viewNav = rememberNavController()

    //DRAWER
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val drawerScope = rememberCoroutineScope()

    var currentView by remember {
        mutableStateOf(AdminViews.DBG)
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
            }
        ) { paddingValues ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                color = MaterialTheme.colorScheme.background
            ) {
                NavHost(navController = viewNav, startDestination = currentView.name){

                    composable(AdminViews.USR.toString()) {
                        DBUsersPanel(viewModel, navController)
                    }

                    composable(AdminViews.PRD.toString()) {
                        DBProductsPanel(viewModel, navController)
                    }

                    composable(AdminViews.ORD.toString()) {
                        DBOrdersPanel(viewModel, navController)
                    }

                    composable(AdminViews.STO.toString()) {
                        DBStoresPanel(viewModel, navController)
                    }

                    composable(AdminViews.LKR.toString()) {
                        DBLockersPanel(viewModel, navController)
                    }

                    composable(AdminViews.DBG.toString()){
                        DebugPanel(viewModel, navController)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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