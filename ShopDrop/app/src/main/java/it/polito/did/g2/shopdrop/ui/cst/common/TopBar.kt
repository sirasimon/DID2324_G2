package it.polito.did.g2.shopdrop.ui.cst.common

import androidx.compose.foundation.Image
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import it.polito.did.g2.shopdrop.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(onBack: (()->Unit)?,
           screenTitle: String,
           scrollBehavior: TopAppBarScrollBehavior
){

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(
                text = screenTitle.capitalize(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            if(onBack!=null){
                IconButton(onClick = { onBack() }) {
                    Image(
                        painter = painterResource(id = R.drawable.btn_back),
                        contentDescription = "Localized description"
                    )
                }
            }
        },
        scrollBehavior = scrollBehavior,
    )
}