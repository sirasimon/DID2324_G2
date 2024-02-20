package it.polito.did.g2.shopdrop.ui.crr.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import it.polito.did.g2.shopdrop.MainViewModel
import it.polito.did.g2.shopdrop.R
import it.polito.did.g2.shopdrop.data.TabScreen
import it.polito.did.g2.shopdrop.navigation.Screens

private enum class CrrFilterChip{COLL, DELI}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CRRHomeScreen(navController : NavController, viewModel: MainViewModel){
    val screenHeight = with(LocalDensity.current) { LocalConfiguration.current.screenHeightDp.dp }
    val profileHeight = (screenHeight * 0.20f)

    val listNavController = rememberNavController()

    val currentTab = TabScreen.PROFILE

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    val chipLabels = mapOf(
        CrrFilterChip.COLL to R.string.chip_to_collect,
        CrrFilterChip.DELI to R.string.chip_to_deliver
    )

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            //TODO: ID card

            Column(horizontalAlignment = Alignment.CenterHorizontally) {






                ProfileSection(profileHeight)
                TabSection(navController)
                Button(onClick = {navController.navigate(Screens.CrrProfileScreen.route)}){
                    Text(text = "Go to profile")
                }

                TextButton(
                    onClick = {
                        viewModel.logout()
                        navController.navigate(Screens.Login.route)
                    }
                ) {
                    Text(stringResource(R.string.btn_log_out).capitalize())
                }
            }
        }




    }
}

@Composable
fun ProfileSection(profileHeight:Dp) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(profileHeight)
            .background(Color.Green),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.shopdrop_logo),
            contentDescription = null,
            modifier = Modifier
                .padding(16.dp)
                .size(64.dp),
            contentScale = ContentScale.Crop
        )
        Text(
            text = "Benvenuto Giovanni",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.weight(1f),
            color = Color.White
        )
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.padding(16.dp)
        )
    }
}





@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabSection(navController: NavController) {
    val listNavController = rememberNavController()

    val currentTab = TabScreen.PROFILE

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    val chipLabels = mapOf(
        CrrFilterChip.COLL to R.string.chip_to_collect,
        CrrFilterChip.DELI to R.string.chip_to_deliver
    )
    val selectedTabIndex = remember { mutableStateOf(0) }

    var activeTab by remember { mutableStateOf(CrrFilterChip.COLL) }
    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()){

        enumValues<CrrFilterChip>().forEach {
            FilterChip(
                onClick = {activeTab = it; listNavController.navigate(it.toString())},
                label = { Text(stringResource(id = chipLabels[it]!!).capitalize()) },
                selected = activeTab==it,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }

    Column {
        NavHost(navController = listNavController, startDestination = CrrFilterChip.COLL.toString()){
            composable(CrrFilterChip.COLL.toString()) {
                //PendingScreen()
            }
            composable(CrrFilterChip.DELI.toString()){
                //ArchivedScreen()
            }
        }






        val orders = generateOrdersList()
        LazyColumn(modifier = Modifier.fillMaxWidth()
            .padding(4.dp)) {

            items(orders) { order ->

                OrderCard(order = order,navController)
            }
        }

    }
}

@Composable
fun OrderCard(order: String,navController: NavController) {
    Card(
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth()
            .clickable(onClick = { navController.navigate(Screens.CrrDeliveryScreen.route) }),

        shape = MaterialTheme.shapes.medium,
        content = {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = null,
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = order)
            }
        }
    )
}

@Composable
fun generateOrdersList(): List<String> {
    return List(8) { "Ordine ${it + 1}" }
}

