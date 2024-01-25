package it.polito.did.g2.shopdrop.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.Text
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import it.polito.did.g2.shopdrop.MainViewModel
import it.polito.did.g2.shopdrop.data.StoreItemCategory
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

        composable(route = Screens.CstCart.route){
            //TODO
            Text("CST CART SCREEN")
        }

        composable(route = Screens.CstProfile.route){
            CSTProfileScreen(navController, viewModel)
        }

        composable(route = Screens.CstOrderDetail.route){
            //TODO
            //Text("CST ORDER DETAIL SCREEN")
        }

        composable(route= Screens.CstOrdersOverview.route){
            CstOrdersHistory(navController, viewModel)
        }

        /*
        composable(route = Screens.CstCameraScreen.route){
            //TODO
            //Text("CST CAMERA SCREEN")
        }
         */
    }
}