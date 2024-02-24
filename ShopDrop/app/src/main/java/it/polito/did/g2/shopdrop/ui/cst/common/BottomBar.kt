package it.polito.did.g2.shopdrop.ui.cst.common

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import it.polito.did.g2.shopdrop.MainViewModel
import it.polito.did.g2.shopdrop.R
import it.polito.did.g2.shopdrop.data.TabScreen
import it.polito.did.g2.shopdrop.navigation.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomBar(currentTab : TabScreen, navController: NavController, vm : MainViewModel){
    //val hasBadge by remember{ mutableStateOf(vm.cart.value?.count() != 0) }
    val badgeNumber by vm.cart.observeAsState()

    NavigationBar() {
        NavigationBarItem(  // HOME
            selected = currentTab== TabScreen.HOME,
            onClick = { if(currentTab!= TabScreen.HOME) navController.navigate(Screens.CstHome.route) },
            icon = { if(currentTab== TabScreen.HOME) Icon(Icons.Filled.Home, stringResource(R.string.tab_home).capitalize()) else Icon(Icons.Outlined.Home, stringResource(R.string.tab_home).capitalize()) },
            label = { Text(text = stringResource(R.string.tab_home).capitalize()) }
        )

        NavigationBarItem(  // CART
            selected = currentTab== TabScreen.CART,
            onClick = { if(currentTab!= TabScreen.CART) navController.navigate(Screens.CstCart.route) },
            icon =
            {
                BadgedBox(
                    badge = {
                        if((badgeNumber?.totalItems?:0) !=0){
                            Badge{
                                Text("${badgeNumber?.totalItems}", color = Color.White)
                            }
                        }
                    }
                ) {
                    if(currentTab== TabScreen.CART)
                        Icon(Icons.Filled.ShoppingCart, stringResource(R.string.tab_cart).capitalize())
                    else
                        Icon(Icons.Outlined.ShoppingCart, stringResource(R.string.tab_cart).capitalize())
                }
            },
            label = { Text(text = stringResource(R.string.tab_cart).capitalize()) }
        )

        NavigationBarItem(  // PROFILE
            selected = currentTab== TabScreen.PROFILE,
            onClick = { if(currentTab!= TabScreen.PROFILE) navController.navigate(Screens.CstProfile.route) },
            icon = { if(currentTab== TabScreen.PROFILE) Icon(Icons.Filled.Person, stringResource(R.string.tab_profile).capitalize()) else Icon(
                Icons.Outlined.Person, stringResource(R.string.tab_profile).capitalize()) },
            label = { Text(text = stringResource(R.string.tab_profile).capitalize()) }
        )
    }
}