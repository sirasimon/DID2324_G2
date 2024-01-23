package it.polito.did.g2.shopdrop.navigation

sealed class Screens(val route: String) {
    // BASE ROUTES
    object AuthRoute : Screens("Auth")
    object CstRoute : Screens("Customer")
    object CrrRoute : Screens("Carrier")
    object AdmRoute : Screens( "Admin")

    object ScreenLoginRoute : Screens("Login")

    // CUSTOMER SCREENS
    object CstHomeScreen : Screens("CstHomeScreen")
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