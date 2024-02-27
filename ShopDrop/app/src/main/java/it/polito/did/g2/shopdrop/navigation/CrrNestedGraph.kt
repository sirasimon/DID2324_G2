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
import it.polito.did.g2.shopdrop.ui.crr.CrrDeposited
import it.polito.did.g2.shopdrop.ui.crr.collection.CrrCollected
import it.polito.did.g2.shopdrop.ui.crr.collection.CrrCollectionCamera
import it.polito.did.g2.shopdrop.ui.crr.delivey.CRRDeliveryScreen
import it.polito.did.g2.shopdrop.ui.crr.depositing.CRRBeforeDeposit
import it.polito.did.g2.shopdrop.ui.crr.depositing.CRRDepositing
import it.polito.did.g2.shopdrop.ui.crr.depositing.CrrDepositCameraLocker
import it.polito.did.g2.shopdrop.ui.crr.depositing.CrrDepositCameraOrder
import it.polito.did.g2.shopdrop.ui.crr.home.CRRHomeScreen
import it.polito.did.g2.shopdrop.ui.crr.orders.CRROrderDetail
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

        //HOME AND PROFILE
        composable(route = Screens.CrrHomeScreen.route){
            CRRHomeScreen(navController, viewModel)
        }

        composable(route = Screens.CrrProfileScreen.route){
            CRRProfileScreen(navController, viewModel)
        }

        //ORDER DETAIL
        composable(route = Screens.CrrOrderDetail.route+"/{orderID}",
            arguments = listOf(navArgument("orderID") { type = NavType.StringType })){
            CRROrderDetail(navController, viewModel, it.arguments?.getString("orderID"))
        }

        composable(route = Screens.CrrCollectionCamera.route+"/{orderID}",
            arguments = listOf(navArgument("orderID") { type = NavType.StringType })){
            CrrCollectionCamera(navController, viewModel, it.arguments?.getString("orderID"))
        }

        composable(route = Screens.CrrDepositCameraLocker.route+"/{orderID}",
            arguments = listOf(navArgument("orderID") { type = NavType.StringType })){
            CrrDepositCameraLocker(navController, viewModel, it.arguments?.getString("orderID")!!)
        }

        composable(route = Screens.CrrDepositCameraOrder.route+"/{orderID}",
            arguments = listOf(navArgument("orderID") { type = NavType.StringType })){
            CrrDepositCameraOrder(navController, viewModel, it.arguments?.getString("orderID")!!)
        }

        composable(route = Screens.CrrBeforeDeposit.route+"/{orderID}",
            arguments = listOf(navArgument("orderID") { type = NavType.StringType })){
            CRRBeforeDeposit(navController, it.arguments?.getString("orderID")!!)
        }

        composable(route = Screens.CrrDepositing.route+"/{orderID}",
            arguments = listOf(navArgument("orderID") { type = NavType.StringType })){
            CRRDepositing(navController, viewModel, it.arguments?.getString("orderID")!!)
        }

        composable(route = Screens.CrrDepositedScreen.route+"/{orderID}",
            arguments = listOf(navArgument("orderID") { type = NavType.StringType })){
            CrrDeposited(navController, viewModel, it.arguments?.getString("orderID")!!)
        }

        composable(route = Screens.CrrCollectedScreen.route+"/{orderID}",
            arguments = listOf(navArgument("orderID") { type = NavType.StringType })){

            CrrCollected(viewModel, navController, it.arguments?.getString("orderID")!!)
        }

        composable(route = Screens.CrrDeliveryScreen.route){
            //TODO
            CRRDeliveryScreen(navController = navController, viewModel =viewModel )
        }
    }
}