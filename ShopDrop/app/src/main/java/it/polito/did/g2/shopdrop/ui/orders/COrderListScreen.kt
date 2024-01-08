package it.polito.did.g2.shopdrop.ui.orders

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import it.polito.did.g2.shopdrop.MainViewModel
import it.polito.did.g2.shopdrop.data.TabScreen
import it.polito.did.g2.shopdrop.ui.common.BottomBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun COrderListScreen(navController : NavController, viewModel : MainViewModel){
    val listNavController = rememberNavController()

    var state by remember { mutableStateOf(0) }
    val options = listOf("Pending", "Archived", "Cancelled")

    var currentTab = TabScreen.PROFILE

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        //topBar = { TopBar(currentTab, "...", scrollBehavior = scrollBehavior) },
        bottomBar = { BottomBar(currentTab, navController, viewModel) },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                //SOLUZIONE 1
                PrimaryTabRow(selectedTabIndex = state) {
                    options.forEachIndexed { index, title ->
                        Tab(
                            selected = state == index,
                            onClick = {
                                state = index
                                listNavController.navigate(title)
                                      },
                            text = { Text(text = title, maxLines = 2, overflow = TextOverflow.Ellipsis) }
                        )
                    }
                }

                //SOLUZIONE 2
                MultiChoiceSegmentedButtonRow {
                    options.forEachIndexed { index, label ->
                        SegmentedButton(
                            shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                            onCheckedChange = { state = index },
                            checked = index == state
                        ) {
                            Text(label)
                        }
                    }
                }

                Column {
                    NavHost(navController = listNavController, startDestination = "Pending"){
                        composable("Pending") {
                            PendingScreen()
                        }
                        composable("Archived"){
                            ArchivedScreen()
                        }
                        composable("Cancelled"){
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