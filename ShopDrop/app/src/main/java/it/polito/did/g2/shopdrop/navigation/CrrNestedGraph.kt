package it.polito.did.g2.shopdrop.navigation

import androidx.compose.material3.Text
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import it.polito.did.g2.shopdrop.MainViewModel
import it.polito.did.g2.shopdrop.ui.crr.delivey.CRRDeliveryScreen

import it.polito.did.g2.shopdrop.ui.crr.home.CRRHomeScreen
import it.polito.did.g2.shopdrop.ui.crr.profile.CRRProfileScreen

fun NavGraphBuilder.crrNavGraph(
    navController: NavHostController,
    viewModel: MainViewModel
){
    navigation(
        startDestination = Screens.CrrHomeScreen.route,
        route = CRR_ROUTE
    ){
        composable(route = Screens.CrrHomeScreen.route){
            //TODO
            CRRHomeScreen(navController, viewModel)
        }
        composable(route = Screens.CrrDeliveryScreen.route){
            //TODO
           CRRDeliveryScreen(navController = navController, viewModel =viewModel )
        }

        composable(route = Screens.CrrProfileScreen.route){
            //TODO
            CRRProfileScreen(navController, viewModel)
        }

        composable(route = Screens.CrrCameraScreen.route){
            //TODO
            Text("CRR CAMERA SCREEN")
        }
    }
}