package it.polito.did.s306067.shopdrop_client.ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(currentTab: TabScreen, scrollBehavior: TopAppBarScrollBehavior){

    var screenTitle = when(currentTab){
        TabScreen.PROFILE -> {
            "PROFILO"
        }

        TabScreen.HOME -> {
            "HOME"
        }

        TabScreen.CART -> {
            "CART"
        }
    }


    CenterAlignedTopAppBar(
        /*
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        */
        title = {
            Text(
                text = screenTitle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        /*navigationIcon = {
            IconButton(onClick = { /* do something */ }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Localized description"
                )
            }
        },*/
        actions = {
            if(currentTab==TabScreen.CART){
                IconButton(onClick = { /* do something */ }) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Localized description"
                    )
                }
            }
        },
        scrollBehavior = scrollBehavior,
    )
}