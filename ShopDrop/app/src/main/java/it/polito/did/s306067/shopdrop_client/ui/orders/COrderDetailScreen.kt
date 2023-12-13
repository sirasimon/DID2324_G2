package it.polito.did.s306067.shopdrop_client.ui.orders

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import it.polito.did.s306067.shopdrop_client.ui.common.BottomBar
import it.polito.did.s306067.shopdrop_client.ui.common.TabScreen
import it.polito.did.s306067.shopdrop_client.ui.common.TopBar

data class UpdateInfo(val stateName : String, val time : String, val state : Boolean = false)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun COrderDetailScreen(navController : NavController){
    var currentTab = TabScreen.HOME

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    val updateList = listOf(
        UpdateInfo("Ordine confermato", "19:15",true),
        UpdateInfo("Spedito", "19:22",true),
        UpdateInfo("In transito", "19:27",true),
        UpdateInfo("Consegnato", "In corso",false)
    )

    Scaffold(
        topBar = { TopBar(currentTab, scrollBehavior = scrollBehavior) },
        bottomBar = { BottomBar(currentTab, navController) },
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            LazyColumn {
                //items()
            }
            
            Column(modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
            ) {
                Text(text = "ORDER DETAIL")
            }
        }
    }
}