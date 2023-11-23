package it.polito.did.s306067.shopdrop_client

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController

enum class TabScreen{
    HOME,
    CART,
    PROFILE
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientHomeScreen(navController : NavController){
    var currentTab = TabScreen.HOME

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomBar(currentTab, navController) },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        //floatingActionButton = { AddButton(onClick = {/*TODO*/}) },
        //floatingActionButtonPosition = FabPosition.End
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            Text(text = "CLIENT HOME")

            //TODO Searchbar
            //TODO Pending orders
            //TODO controllare il vm che ci siano ordini pendenti ed eventualmente mostrarli
            //TODO demo oggetti acquistabili
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientCartScreen(navController: NavController){
    var currentTab = TabScreen.CART

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomBar(currentTab, navController) },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        //floatingActionButton = { AddButton(onClick = {/*TODO*/}) },
        //floatingActionButtonPosition = FabPosition.End
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            Text(text = "CLIENT CART")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientProfileScreen(navController: NavController){
    var currentTab = TabScreen.PROFILE

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomBar(currentTab, navController) },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        //floatingActionButton = { AddButton(onClick = {/*TODO*/}) },
        //floatingActionButtonPosition = FabPosition.End
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            Text(text = "CLIENT PROFILE")
        }
    }
}

@Composable
fun TopBar(){
    //TODO
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomBar(currentTab : TabScreen, navController: NavController/*, vm : ViewModel*/){
    NavigationBar {
        NavigationBarItem(
            selected = currentTab==TabScreen.HOME,
            enabled = currentTab!=TabScreen.HOME,
            onClick = { if(currentTab!=TabScreen.HOME) navController.navigate("ClientHome") },
            icon = { Icon(Icons.Filled.Home, "Homepage") },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = currentTab==TabScreen.CART,
            enabled = currentTab!=TabScreen.CART,
            onClick = { if(currentTab!=TabScreen.CART) navController.navigate("ClientCart") },
            icon =
            {
                BadgedBox(
                    badge = {
                        Badge{
                            val badgeNumber = "0" /*TODO here read to VM items in cart*/
                            Text(badgeNumber)
                        }
                    }
                ) {
                    Icon(Icons.Filled.ShoppingCart, "Shopping Cart")
                }
            },
            label = { Text("Cart") }
        )
        NavigationBarItem(
            selected = currentTab==TabScreen.PROFILE,
            enabled = currentTab!=TabScreen.PROFILE,
            onClick = { if(currentTab!=TabScreen.PROFILE) navController.navigate("ClientProfile") },
            icon = { Icon(Icons.Filled.Person, "Profile") },
            label = { Text("Profile") }
        )
    }
}