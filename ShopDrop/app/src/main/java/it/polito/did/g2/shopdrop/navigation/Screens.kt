package it.polito.did.g2.shopdrop.navigation

const val ROOT_ROUTE = "root"
const val ADM_ROUTE = "adm"
const val CST_ROUTE = "cst"
const val CRR_ROUTE = "crr"

sealed class Screens(val route: String) {
    // LOGIN SCREEN
    object ScreenLoginRoute : Screens("Login")

    // CUSTOMER SCREENS ############################################################################
    object CstHome : Screens("CstHomeScreen")
    object CstCategory : Screens("CstCategoryScreen")


    object CstCart : Screens("CstCartScreen")
    object CstCheckout : Screens("CstCheckoutScreen")

    object CstProfile : Screens("CstProfileScreen")
    object CstCameraScreen : Screens("CstProfileScreen")

    // Orders
    object CstOrderHistory : Screens("CstOrdersOverviewScreen")
    object CstOrderDetail : Screens("CstOrderDetailScreen")

    // CARRIER SCREENS #############################################################################
    object CrrHomeScreen : Screens("CrrHomeScreen")
    object CrrCameraScreen : Screens("CrrCameraScreen")
    object CrrProfileScreen : Screens("CrrProfileScreen")

    //ADMIN SCREENS ################################################################################
    object AdmHomeScreen : Screens("AdmHomeScreen")
}