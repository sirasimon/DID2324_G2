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
import it.polito.did.g2.shopdrop.data.StoreItemCategory
import it.polito.did.g2.shopdrop.ui.camera.CameraScreen
import it.polito.did.g2.shopdrop.ui.cst.cart.CSTCartScreen
import it.polito.did.g2.shopdrop.ui.cst.cart.CSTCheckoutScreen
import it.polito.did.g2.shopdrop.ui.cst.cart.LockerSelectorScreen
import it.polito.did.g2.shopdrop.ui.cst.cart.OrderSent
import it.polito.did.g2.shopdrop.ui.cst.home.CSTHomeScreen
import it.polito.did.g2.shopdrop.ui.cst.home.CategoryScreen
import it.polito.did.g2.shopdrop.ui.cst.orders.CstOrdersHistory
import it.polito.did.g2.shopdrop.ui.cst.profile.CSTProfileScreen

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.cstNavGraph(
    navController: NavHostController,
    viewModel: MainViewModel
){
    navigation(
        startDestination = Screens.CstHome.route,
        route = CST_ROUTE
    ){

        // HOME SCREENS ////////////////////////////////////////////////////////////////////////////

        composable(route = Screens.CstHome.route){
            CSTHomeScreen(navController, viewModel)
        }

        composable(route = Screens.CstCategory.route+"?categoryName={categoryName}&query={query}",
            arguments = listOf(
                navArgument(name = "categoryName"){
                    type = NavType.StringType
                    defaultValue = "NONE"
                },
                navArgument(name = "query"){
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                }
            )
        ){backstackEntry ->
            CategoryScreen(
                navController = navController,
                viewModel = viewModel,
                categoryName = enumValueOf<StoreItemCategory>(backstackEntry.arguments?.getString("categoryName")?:"NONE"),
                query = backstackEntry.arguments?.getString("query")
            )
        }

        // CART ////////////////////////////////////////////////////////////////////////////////////

        composable(route = Screens.CstCart.route){
            CSTCartScreen(navController, viewModel)
        }

        composable(route = Screens.CstCheckout.route){
            CSTCheckoutScreen(navController, viewModel)
        }

        composable(route = Screens.CstLockerSelector.route){
            LockerSelectorScreen(navController, viewModel)
        }

        composable(route = Screens.CstOrderSent.route){
            OrderSent(navController)
        }

        // PROFILE /////////////////////////////////////////////////////////////////////////////////

        composable(route = Screens.CstProfile.route){
            CSTProfileScreen(navController, viewModel)
        }

        // ORDERS //////////////////////////////////////////////////////////////////////////////////

        composable(route= Screens.CstOrderHistory.route){
            CstOrdersHistory(navController, viewModel)
        }

        composable(route = Screens.CstOrderDetail.route){
            //TODO
            //Text("CST ORDER DETAIL SCREEN")
        }

        // UNLOCK PROCEDURE ////////////////////////////////////////////////////////////////////////

        composable(route = Screens.CstCamera.route){
            //TODO
            CameraScreen(navController, viewModel)
        }
    }
}