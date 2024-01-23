package it.polito.did.g2.shopdrop.navigation

import androidx.compose.material3.Text
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import it.polito.did.g2.shopdrop.MainViewModel

fun NavGraphBuilder.crrGraph(navController: NavController, viewModel: MainViewModel){
    navigation(startDestination = Screens.CrrHomeScreen.route, route = Screens.CrrRoute.route){
        composable(route = Screens.CrrHomeScreen.route){
            //TODO
            Text("CRR HOME SCREEN")
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