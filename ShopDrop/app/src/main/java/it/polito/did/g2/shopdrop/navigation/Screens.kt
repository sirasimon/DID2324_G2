package it.polito.did.g2.shopdrop.navigation

const val ROOT_ROUTE = "root"
const val ADM_ROUTE = "adm"
const val CST_ROUTE = "cst"
const val CRR_ROUTE = "crr"

sealed class Screens(val route: String) {
    // LOGIN SCREEN
    object Login : Screens("Login")

    // CUSTOMER SCREENS ############################################################################
    object CstHome : Screens("CstHomeScreen")
    object CstCategory : Screens("CstCategoryScreen")


    object CstCart : Screens("CstCartScreen")
    object CstCheckout : Screens("CstCheckoutScreen")
    object CstLockerSelector : Screens("CstLockerSelectorScreen")
    object CstOrderSent : Screens("CstOrderSentScreen")

    object CstProfile : Screens("CstProfileScreen")
    object CstCamera : Screens("CstCameraScreen")
    object CstCollection : Screens("CstCollectionScreen")
    object CstCollectionDoneScreen : Screens("CstCollectionDoneScreen")

    // Orders
    object CstOrderHistory : Screens("CstOrdersOverviewScreen")
    object CstOrderDetail : Screens("CstOrderDetailScreen")

    // CARRIER SCREENS #############################################################################
    object CrrHomeScreen : Screens("CrrHomeScreen")
    object CrrProfileScreen : Screens("CrrProfileScreen")

    object CrrOrderDetail: Screens("CrrOrderDetailScreen")

    object CrrCollectionCamera : Screens("CrrCollectionCameraScreen")
    object CrrDepositCameraLocker : Screens("CrrDepositCameraLockerScreen")
    object CrrDepositCameraOrder : Screens("CrrDepositCameraOrderScreen")

    object CrrBeforeDeposit : Screens("CrrBeforeDepositScreen")
    object CrrDepositing : Screens("CrrDepositingScreen")

    object CrrDeliveryScreen: Screens("CrrDeliveryScreen")
    object CrrCollectedScreen: Screens("CrrCollectedScreen")
    object CrrDepositedScreen: Screens("CrrDepositedScreen")


    //ADMIN SCREENS ################################################################################
    object AdmHomeScreen : Screens("AdmHomeScreen")
    object AdmCameraTest : Screens("AdmCameraTest")
    object AdmCollectionTest : Screens("AdmCollectionTest")
}