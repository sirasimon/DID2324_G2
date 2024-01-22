package it.polito.did.g2.shopdrop.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import it.polito.did.g2.shopdrop.MainViewModel

fun NavGraphBuilder.authGraph(navController: NavController){
    navigation(startDestination = Screens.ScreenLoginRoute.route, route = Screens.AuthRoute.route){

    }
}