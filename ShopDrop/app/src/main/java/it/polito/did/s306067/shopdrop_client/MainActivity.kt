package it.polito.did.s306067.shopdrop_client

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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

    @Composable
    fun Navigation(){
        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = "ClientHome"){
            composable("ClientHome") {
                ClientHomeScreen(navController = navController)
            }
            composable("ClientCart"){
                ClientCartScreen(navController = navController)
            }
            composable("ClientProfile"){
                ClientProfileScreen(navController = navController)
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ShopDropTheme {

    }
}