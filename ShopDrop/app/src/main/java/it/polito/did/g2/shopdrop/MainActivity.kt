package it.polito.did.g2.shopdrop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import it.polito.did.g2.shopdrop.ui.cart.CCartScreen
import it.polito.did.g2.shopdrop.ui.home.CHomeScreen
import it.polito.did.g2.shopdrop.ui.home.CategorySection
import it.polito.did.g2.shopdrop.ui.orders.COrderDetailScreen
import it.polito.did.g2.shopdrop.ui.orders.COrderListScreen
import it.polito.did.g2.shopdrop.ui.profile.CProfileScreen
import it.polito.did.g2.shopdrop.ui.theme.ShopDropTheme

class MainActivity : ComponentActivity() {
    private val vm by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm.debugInit()

        setContent {
            ShopDropTheme {
                // A surface container using the 'background' color from the theme
                Navigation(vm)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        vm.debugSetDefault()
    }
}

@Composable
fun Navigation(viewModel: MainViewModel){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "ClientHome"){
        composable("ClientHome") {
            CHomeScreen(navController = navController)
        }
        composable("ClientCart"){
            CCartScreen(navController = navController)
        }
        composable("ClientProfile"){
            CProfileScreen(navController = navController)
        }

        composable("CategorySection"){
            CategorySection(navController = navController)
        }

        composable("COrderDetailScreen"){
            COrderDetailScreen(navController = navController, vm = viewModel)
        }

        composable("COrderListScreen"){
            COrderListScreen(navController = navController)
        }
    }
}