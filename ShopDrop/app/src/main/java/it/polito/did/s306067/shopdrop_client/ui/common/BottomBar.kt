package it.polito.did.s306067.shopdrop_client.ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomBar(currentTab : TabScreen, navController: NavController/*, vm : ViewModel*/){
    var totalItem = 0

    NavigationBar() {
        NavigationBarItem(
            selected = currentTab== TabScreen.HOME,
            enabled = currentTab!= TabScreen.HOME,
            onClick = { if(currentTab!= TabScreen.HOME) navController.navigate("ClientHome") },
            icon = { if(currentTab== TabScreen.HOME) Icon(Icons.Filled.Home, "Homepage") else Icon(
                Icons.Outlined.Home, "Homepage") },
            label = { Text(text = "Home") }
        )
        NavigationBarItem(
            selected = currentTab== TabScreen.CART,
            enabled = currentTab!= TabScreen.CART,
            onClick = { if(currentTab!= TabScreen.CART) navController.navigate("ClientCart") },
            icon =
            {
                BadgedBox(
                    badge = {
                        if(totalItem!=0){
                            Badge{
                                val badgeNumber = "0" /*TODO here read to VM items in cart*/
                                Text(badgeNumber)
                            }
                        }
                    }
                ) {
                    if(currentTab== TabScreen.CART) Icon(Icons.Filled.ShoppingCart, "Homepage") else Icon(
                        Icons.Outlined.ShoppingCart, "Homepage")
                }
            },
            label = { Text(text = "Carrello") }
        )
        NavigationBarItem(
            selected = currentTab== TabScreen.PROFILE,
            enabled = currentTab!= TabScreen.PROFILE,
            onClick = { if(currentTab!= TabScreen.PROFILE) navController.navigate("ClientProfile") },
            icon = { if(currentTab== TabScreen.PROFILE) Icon(Icons.Filled.Person, "Profile") else Icon(
                Icons.Outlined.Person, "Profile") },
            label = { Text(text = "Profilo") }
        )
    }
}