package it.polito.did.g2.shopdrop.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import it.polito.did.g2.shopdrop.MainViewModel
import it.polito.did.g2.shopdrop.ui.login.LoginScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Nav(viewModel: MainViewModel){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screens.ScreenLoginRoute.route,
        route = ROOT_ROUTE
        ){
        //AUTH BRANCH
        composable(route = Screens.ScreenLoginRoute.route){
            LoginScreen(navController, viewModel)
        }

        //CUSTOMER BRANCH (CST)
        cstNavGraph(navController, viewModel)

        //CARRIER BRANCH (CRR)
        crrNavGraph(navController, viewModel)

        //ADMIN BRANCH (ADM)
        admNavGraph(navController, viewModel)

    }
}