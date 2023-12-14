package it.polito.did.s306067.shopdrop_client

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import it.polito.did.s306067.shopdrop_client.ui.cart.CCartScreen
import it.polito.did.s306067.shopdrop_client.ui.home.CHomeScreen
import it.polito.did.s306067.shopdrop_client.ui.home.CategorySection
import it.polito.did.s306067.shopdrop_client.ui.orders.COrderDetailScreen
import it.polito.did.s306067.shopdrop_client.ui.orders.COrderListScreen
import it.polito.did.s306067.shopdrop_client.ui.profile.CProfileScreen
import it.polito.did.s306067.shopdrop_client.ui.theme.ShopDropTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShopDropTheme {
                // A surface container using the 'background' color from the theme
                Navigation()
            }
        }
    }
}

@Composable
fun Navigation(){
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
            COrderDetailScreen(navController = navController)
        }

        composable("COrderListScreen"){
            COrderListScreen(navController = navController)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ShopDropTheme {
        Navigation()
    }
}