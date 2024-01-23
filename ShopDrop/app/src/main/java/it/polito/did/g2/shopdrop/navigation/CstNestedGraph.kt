package it.polito.did.g2.shopdrop.navigation

import androidx.compose.material3.Text
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import it.polito.did.g2.shopdrop.MainViewModel

fun NavGraphBuilder.cstGraph(navController: NavController, viewModel: MainViewModel){
    navigation(startDestination = Screens.CstHomeScreen.route, route = Screens.CstRoute.route){
        composable(route = Screens.CstHomeScreen.route){
            //TODO
            Text("CST HOME SCREEN")
        }

        composable(route = Screens.CstCartScreen.route){
            //TODO
            Text("CST CART SCREEN")
        }

        composable(route = Screens.CstProfileScreen.route){
            //TODO
            Text("CST PROFILE SCREEN")
        }

        composable(route = Screens.CstOrderDetailScreen.route){
            //TODO
            Text("CST ORDER DETAIL SCREEN")
        }

        composable(route = Screens.CstCameraScreen.route){
            //TODO
            Text("CST CAMERA SCREEN")
        }
    }
}