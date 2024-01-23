package it.polito.did.g2.shopdrop.navigation

import androidx.compose.material3.Text
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import it.polito.did.g2.shopdrop.MainViewModel

fun NavGraphBuilder.admGraph(navController: NavController, viewModel: MainViewModel){
    navigation(startDestination = Screens.AdmHomeScreen.route, route = Screens.AdmRoute.route){
        composable(route = Screens.AdmHomeScreen.route){
            //TODO
            Text("ADM HOME SCREEN")
        }
    }
}