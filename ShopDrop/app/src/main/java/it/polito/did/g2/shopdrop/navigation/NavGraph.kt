package it.polito.did.g2.shopdrop.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import it.polito.did.g2.shopdrop.MainViewModel
import it.polito.did.g2.shopdrop.ui.login.LoginScreen

@Composable
fun Nav(viewModel: MainViewModel){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screens.ScreenLoginRoute.route){
        //AUTH BRANCH
        composable(route = Screens.ScreenLoginRoute.route){
            LoginScreen(navController, viewModel)
        }

        //CUSTOMER BRANCH (CST)
        cstGraph(navController, viewModel)

        //CARRIER BRANCH (CRR)
        crrGraph(navController, viewModel)

        //ADMIN BRANCH (ADM)
        admGraph(navController, viewModel)

    }
}