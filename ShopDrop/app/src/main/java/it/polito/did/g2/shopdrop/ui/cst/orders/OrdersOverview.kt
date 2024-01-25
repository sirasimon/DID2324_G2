package it.polito.did.g2.shopdrop.ui.cst.orders

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import it.polito.did.g2.shopdrop.MainViewModel
import it.polito.did.g2.shopdrop.R
import it.polito.did.g2.shopdrop.data.TabScreen
import it.polito.did.g2.shopdrop.ui.cst.common.BottomBar
import it.polito.did.g2.shopdrop.ui.cst.common.TopBar

enum class FilterTab{PEND, ARCH, CANC}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CstOrdersOverview(navController : NavController, viewModel : MainViewModel){
    val listNavController = rememberNavController()

    var state by remember { mutableStateOf(0) }
    val options = listOf("Pending", "Archived", "Cancelled")

    val currentTab = TabScreen.PROFILE

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = { TopBar(navController, stringResource(id = R.string.title_history), scrollBehavior) },
        bottomBar = { BottomBar(currentTab, navController, viewModel) },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                var activeTab by remember { mutableStateOf(FilterTab.PEND) }
                Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()){
                    FilterChip(
                        onClick = {activeTab = FilterTab.PEND; listNavController.navigate(FilterTab.PEND.toString())},
                        label = { Text(stringResource(id = R.string.chip_pending).capitalize())},
                        selected = activeTab==FilterTab.PEND,
                        shape = RoundedCornerShape(16.dp)
                    )

                    FilterChip(
                        onClick = {activeTab = FilterTab.ARCH; listNavController.navigate(FilterTab.ARCH.toString())},
                        label = { Text(stringResource(id = R.string.chip_collected).capitalize())},
                        selected = activeTab==FilterTab.ARCH,
                        shape = RoundedCornerShape(16.dp)
                    )

                    FilterChip(
                        onClick = {activeTab = FilterTab.CANC; listNavController.navigate(FilterTab.CANC.toString())},
                        label = { Text(stringResource(id = R.string.chip_cancelled).capitalize())},
                        selected = activeTab==FilterTab.CANC,
                        shape = RoundedCornerShape(16.dp)
                    )
                }

                Column {
                    NavHost(navController = listNavController, startDestination = FilterTab.PEND.toString()){
                        composable(FilterTab.PEND.toString()) {
                            PendingScreen()
                        }
                        composable(FilterTab.ARCH.toString()){
                            ArchivedScreen()
                        }
                        composable(FilterTab.CANC.toString()){
                            CancelledScreen()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CancelledScreen() {
    //TODO

    Column() {
        for(i in 1..3){
            OrderListItem(label = "TEST $i") {
                Log.d("CLICK!","Cliccato su $i")
            }
        }
    }
}

@Composable
fun ArchivedScreen() {
    //TODO

    Column(){
        for(i in 4..9){
            OrderListItem(label = "TEST $i") {
                Log.d("CLICK!","Cliccato su $i")
            }
        }
    }
}

@Composable
fun PendingScreen() {
    //TODO
    Column(){
        for(i in 11..13){
            OrderListItem(label = "TEST $i") {
                Log.d("CLICK!","Cliccato su $i")
            }
        }
    }
}

@Composable
fun OrderListItem(label : String, onClick : ()->Unit){
    Card(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .height(64.dp)
            .clickable(onClick = onClick)
    ){
        Row( verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ){
            Text(text = label)
            Icon(Icons.Filled.KeyboardArrowRight, "Open")
        }
    }
}