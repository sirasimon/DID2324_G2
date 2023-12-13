package it.polito.did.s306067.shopdrop_client.ui.profile

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.navigation.NavController
import it.polito.did.s306067.shopdrop_client.ui.common.BottomBar
import it.polito.did.s306067.shopdrop_client.ui.common.TabScreen
import it.polito.did.s306067.shopdrop_client.ui.common.TopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CProfileScreen(navController: NavController){
    var currentTab = TabScreen.PROFILE

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = { TopBar(currentTab, scrollBehavior = scrollBehavior) },
        bottomBar = { BottomBar(currentTab, navController) },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        //floatingActionButton = { AddButton(onClick = {/*TODO*/}) },
        //floatingActionButtonPosition = FabPosition.End
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            Text(text = "TODO")
        }
    }
}