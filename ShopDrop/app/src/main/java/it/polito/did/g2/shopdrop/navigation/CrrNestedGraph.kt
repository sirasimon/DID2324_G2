package it.polito.did.g2.shopdrop.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import it.polito.did.g2.shopdrop.MainViewModel
import it.polito.did.g2.shopdrop.ui.crr.CrrCamera
import it.polito.did.g2.shopdrop.ui.crr.CrrCollected
import it.polito.did.g2.shopdrop.ui.crr.CrrDepositCamera
import it.polito.did.g2.shopdrop.ui.crr.CrrDeposited
import it.polito.did.g2.shopdrop.ui.crr.delivey.CRRDeliveryScreen
import it.polito.did.g2.shopdrop.ui.crr.home.CRRHomeScreen
import it.polito.did.g2.shopdrop.ui.crr.profile.CRRProfileScreen

@RequiresApi(Build.VERSION_CODES.O)
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

        composable(route = Screens.CrrCollectionCameraScreen.route+"/{orderID}",
            arguments = listOf(navArgument("orderID") { type = NavType.StringType })){
            CrrCamera(navController, viewModel, it.arguments?.getString("orderID"))
        }

        composable(route = Screens.CrrDepositCameraScreen.route+"/{orderID}",
            arguments = listOf(navArgument("orderID") { type = NavType.StringType })){
            CrrDepositCamera(navController, viewModel, it.arguments?.getString("orderID")!!)
        }

        composable(route = Screens.CrrDepositedScreen.route+"/{orderID}",
            arguments = listOf(navArgument("orderID") { type = NavType.StringType })){
            CrrDeposited(navController, viewModel, it.arguments?.getString("orderID")!!)
        }

        composable(route = Screens.CrrCollectedScreen.route){
            CrrCollected(navController)
        }
    }
}