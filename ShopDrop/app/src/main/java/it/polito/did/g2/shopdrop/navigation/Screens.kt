package it.polito.did.g2.shopdrop.navigation

sealed class Screens(val route: String) {
    // BASE ROUTES
    object AuthRoute : Screens(route = "Auth")
    object CstRoute : Screens(route = "Customer")
    object CrrRoute : Screens(route = "Carrier")
    object AdmRoute : Screens(route = "Admin")

    object ScreenLoginRoute : Screens(route = "Login")
}