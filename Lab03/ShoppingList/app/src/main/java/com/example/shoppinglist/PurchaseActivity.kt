package com.example.shoppinglist

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.shoppinglist.ui.theme.ShoppingListTheme

class MainActivity : ComponentActivity() {

    private val vm by viewModels<PurchaseViewModel>()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vm.addItem(PurchasableItem("PANE", ItemCategory.FORNO, false))
        Log.d("TEST", "${vm.getItems()}")

        vm.addItem(PurchasableItem("ABC", ItemCategory.FORNO, true))
        Log.d("TEST", "${vm.getItems()}")

        setContent {
            ShoppingListTheme {
                // A surface container using the 'background' color from the theme

                Navigation(vm)
            }
        }
    }
}

@Composable
fun Navigation(vm : PurchaseViewModel){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "ComposingScreen"){
        composable("ComposingScreen") {
            ComposingScreen(navController = navController, vm)
        }
        composable("ShoppingScreen") {
            ShoppingScreen(navController = navController, vm)
        }
    }
}

/*
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ShoppingListTheme {
        Navigation(vm)
    }
}

 */