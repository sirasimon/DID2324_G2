package it.polito.did.g2.shopdrop

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import it.polito.did.g2.shopdrop.ui.camera.CameraScreen
import it.polito.did.g2.shopdrop.ui.cart.CartScreen
import it.polito.did.g2.shopdrop.ui.home.CSTHomeScreen
import it.polito.did.g2.shopdrop.ui.home.CarrierHomeScreen
import it.polito.did.g2.shopdrop.ui.home.CategorySection
import it.polito.did.g2.shopdrop.ui.login.LoginScreen
import it.polito.did.g2.shopdrop.ui.orders.COrderDetailScreen
import it.polito.did.g2.shopdrop.ui.orders.COrderListScreen
import it.polito.did.g2.shopdrop.ui.profile.CProfileScreen
import it.polito.did.g2.shopdrop.ui.theme.ShopDropTheme
import it.polito.did.g2.shopdrop.ui.unlocker.UnlockScreen

class MainActivity : ComponentActivity() {
    private val vm by viewModels<MainViewModel>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = application.getSharedPreferences("shopdrop_pref", Context.MODE_PRIVATE)

        vm.debugInit()

        setContent {
            ShopDropTheme {
                // A surface container using the 'background' color from the theme
                Navigation(vm)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        vm.debugSetDefault()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(viewModel: MainViewModel){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "CustomerHome"
        /*
        if(viewModel.loadUserInfo()!=null){
            "CSTHomeScreen"
        }else{
            "Login"
        }
        */
    ){
        composable("Login") {
            LoginScreen(navController = navController, viewModel = viewModel)
        }
        composable("CustomerHome") {
            CSTHomeScreen(navController = navController, viewModel = viewModel)
        }
        composable("CarrierHome") {
            CarrierHomeScreen(navController = navController)
        }
        composable("ClientCart"){
            CartScreen(navController = navController, viewModel = viewModel)
        }
        composable("ClientProfile"){
            CProfileScreen(navController = navController, viewModel)
        }
        composable("CategorySection"){
            CategorySection(navController = navController, viewModel = viewModel)
        }
        composable("COrderDetailScreen"){
            COrderDetailScreen(navController = navController, vm = viewModel)
        }
        composable("COrderListScreen"){
            COrderListScreen(navController = navController, viewModel)
        }
        composable("CameraScreen"){
            CameraScreen(navController = navController, viewModel)
        }

        composable("UnlockScreen"){
            UnlockScreen(navController = navController)
        }
    }
}