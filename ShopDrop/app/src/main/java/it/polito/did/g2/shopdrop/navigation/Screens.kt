package it.polito.did.g2.shopdrop.navigation

const val ROOT_ROUTE = "root"
const val ADM_ROUTE = "adm"
const val CST_ROUTE = "cst"
const val CRR_ROUTE = "crr"

sealed class Screens(val route: String) {
    // LOGIN SCREEN
    object ScreenLoginRoute : Screens("Login")

    // CUSTOMER SCREENS
    object CstHomeScreen : Screens("CstHomeScreen")
    object CstCategoryScreen : Screens("CstCategoryScreen")


    object CstCartScreen : Screens("CstCartScreen")
    object CstProfileScreen : Screens("CstProfileScreen")
    object CstOrderDetailScreen : Screens("CstOrderDetailScreen")
    object CstCameraScreen : Screens("CstProfileScreen")

    // CARRIER SCREENS
    object CrrHomeScreen : Screens("CrrHomeScreen")
    object CrrCameraScreen : Screens("CrrCameraScreen")
    object CrrProfileScreen : Screens("CrrProfileScreen")

    //ADMIN SCREENS
    object AdmHomeScreen : Screens("AdmHomeScreen")
}