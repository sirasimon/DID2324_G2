package it.polito.did.g2.shopdrop.navigation

import androidx.compose.material3.Text
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import it.polito.did.g2.shopdrop.MainViewModel
import it.polito.did.g2.shopdrop.ui.crr.home.CarrierHomeScreen

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
            CarrierHomeScreen(navController, viewModel)
        }

        composable(route = Screens.CrrProfileScreen.route){
            //TODO
            Text("CRR PROFILE SCREEN")
        }

        composable(route = Screens.CrrCameraScreen.route){
            //TODO
            Text("CRR CAMERA SCREEN")
        }
    }
}