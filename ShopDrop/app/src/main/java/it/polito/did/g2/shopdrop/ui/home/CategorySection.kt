package it.polito.did.g2.shopdrop.ui.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import it.polito.did.g2.shopdrop.data.TabScreen
import it.polito.did.g2.shopdrop.ui.common.BottomBar
import it.polito.did.g2.shopdrop.ui.common.ItemCard


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySection(navController : NavController){
    var currentTab = TabScreen.HOME

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    //Bottom sheet
    val bottomSheetState = rememberModalBottomSheetState()
    val bottomSheetScope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    val testList : List<Int> = listOf(1,2,3,4,5,6,7)

    Scaffold(
        //topBar = { TopBar(currentTab, "" scrollBehavior = scrollBehavior) },
        bottomBar = { BottomBar(currentTab, navController) },
        //modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        //floatingActionButton = { AddButton(onClick = {/*TODO*/}) },
        //floatingActionButtonPosition = FabPosition.End
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            //Text(text = "TEST")

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(vertical = 8.dp),
                modifier = Modifier.background(Color.Cyan)
            ){
                items(
                    count = testList.size,
                    span = { GridItemSpan(1) }
                ) {
                    ItemCard {
                        /*TODO*/
                    }
                }
            }
        }
    }

    if(showBottomSheet){
        Log.d("MODAL", "Dovrebbe aprirsi qui")
        //ConfirmSheet({showBottomSheet=false}, bottomSheetState, bottomSheetScope)
    }
}