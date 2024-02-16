package it.polito.did.g2.shopdrop.ui.crr.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
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
import it.polito.did.g2.shopdrop.navigation.Screens

private enum class CrrFilterChip{COL, DEL}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CRRHomeScreen(navController : NavController, viewModel: MainViewModel){

    val listNavController = rememberNavController()

    val currentTab = TabScreen.PROFILE

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    val chipLabels = mapOf(
        CrrFilterChip.COL to R.string.chip_to_collect,
        CrrFilterChip.DEL to R.string.chip_to_deliver
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
                var activeTab by remember { mutableStateOf(CrrFilterChip.COL) }
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
                    NavHost(navController = listNavController, startDestination = CrrFilterChip.COL.toString()){
                        composable(CrrFilterChip.COL.toString()) {
                            //PendingScreen()
                        }
                        composable(CrrFilterChip.DEL.toString()){
                            //ArchivedScreen()
                        }
                    }

                    Button(onClick = {navController.navigate(Screens.CrrProfileScreen.route)}){
                        Text(text = "Go to profile")
                    }

                    Button(onClick = {viewModel.logout(); navController.navigate(Screens.Login.route)}){
                        Text(text = "Log out")
                    }
                }
            }
        }
    }
}