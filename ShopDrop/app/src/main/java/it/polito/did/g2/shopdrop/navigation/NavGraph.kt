package it.polito.did.g2.shopdrop.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import it.polito.did.g2.shopdrop.MainViewModel
import it.polito.did.g2.shopdrop.data.users.UserRole
import it.polito.did.g2.shopdrop.ui.login.LoginScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Nav(viewModel: MainViewModel){
    val navController = rememberNavController()

    val startDestination = when(viewModel.currUser.value?.role){
        UserRole.ADM -> ADM_ROUTE
        UserRole.CST -> CST_ROUTE
        UserRole.CRR -> CRR_ROUTE
        else -> Screens.Login.route
    }

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
        /*
        composable(route = Screens.CstHomeScreen.route){
            CSTHomeScreen(navController, viewModel)
        }

        composable(route = Screens.CstCartScreen.route){
            //TODO
            Text("CST CART SCREEN")
        }

        composable(route = Screens.CstProfileScreen.route){
            CSTProfileScreen(navController, viewModel)
        }

        composable(route = Screens.CstOrderDetailScreen.route){
            //TODO
            Text("CST ORDER DETAIL SCREEN")
        }

        composable(route = Screens.CstCameraScreen.route){
            //TODO
            Text("CST CAMERA SCREEN")
        }
         */

        //CARRIER BRANCH (CRR)
        crrNavGraph(navController, viewModel)
        /*
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
        */

        //ADMIN BRANCH (ADM)
        //admNavGraph(navController, viewModel)
        composable(route = Screens.AdmHomeScreen.route){
            //TODO
            Text("ADM HOME SCREEN")
        }
    }
}