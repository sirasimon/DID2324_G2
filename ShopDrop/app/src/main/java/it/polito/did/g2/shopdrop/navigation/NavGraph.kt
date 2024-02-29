package it.polito.did.g2.shopdrop.navigation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import it.polito.did.g2.shopdrop.MainViewModel
import it.polito.did.g2.shopdrop.data.users.UserRole
import it.polito.did.g2.shopdrop.ui.login.LoginScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Nav(viewModel: MainViewModel, lifecycleOwner: LifecycleOwner){
    val navController = rememberNavController()

    val startDestination = when(viewModel.currUser.value?.role){
        UserRole.ADM -> ADM_ROUTE
        UserRole.CST -> CST_ROUTE
        UserRole.CRR -> CRR_ROUTE
        else -> Screens.Login.route
    }

    if(viewModel.currUser.value?.role!=null)
        Log.i("NAV", "Current user already loaded, so start destination is $startDestination")
    else
        Log.i("NAV", "Current user NOT loaded yet, so start destination is $startDestination")

    NavHost(
        navController = navController,
        startDestination = startDestination,
        //route = ROOT_ROUTE
        ){
        //AUTH BRANCH
        composable(route = Screens.Login.route){
            LoginScreen(navController, viewModel)
        }

        //CUSTOMER BRANCH (CST)
        cstNavGraph(navController, viewModel)

        //CARRIER BRANCH (CRR)
        crrNavGraph(navController, viewModel)

        //ADMIN BRANCH (ADM)
        admNavGraph(navController, viewModel, lifecycleOwner)
    }
}