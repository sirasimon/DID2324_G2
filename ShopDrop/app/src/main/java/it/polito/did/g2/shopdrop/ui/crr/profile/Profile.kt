package it.polito.did.g2.shopdrop.ui.crr.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import it.polito.did.g2.shopdrop.MainViewModel
import it.polito.did.g2.shopdrop.R
import it.polito.did.g2.shopdrop.navigation.Screens
import it.polito.did.g2.shopdrop.ui.common.ProfileItemList
import it.polito.did.g2.shopdrop.ui.cst.common.WIPMessage
import it.polito.did.g2.shopdrop.ui.cst.common.performDevMsg
import it.polito.did.g2.shopdrop.ui.theme.secondaryLight


@Composable
fun CRRProfileScreen(navController: NavController, viewModel: MainViewModel) {

    // SNACKBAR
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    val profileOptions = listOf(
        stringResource(id = R.string.crr_profile_personal_info).capitalize(),
        stringResource(id = R.string.crr_profile_delivery_history).capitalize(),
        stringResource(id = R.string.crr_profile_assistance).capitalize(),
        stringResource(id = R.string.crr_profile_settings).capitalize(),
    )

    Scaffold(
        snackbarHost = { WIPMessage(snackbarHostState) }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                ProfileHeader(
                    viewModel.currUser.value?.name?.substringBefore(" ")?:"ERR",
                    viewModel.currUser.value?.uid?:"ERR"
                ){
                    navController.navigateUp()
                }

                Spacer(Modifier.height(32.dp))

                profileOptions.forEach {
                    ProfileItemList(label = it) {
                        performDevMsg(scope, snackbarHostState, context)
                    }
                }

                Spacer(Modifier.height(32.dp))

                Text("v. ${viewModel.bldNum}", fontSize = 10.sp, color = Color.Gray)


                Spacer(Modifier.height(32.dp))

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
fun ProfileHeader(name: String, uid: String, toHome: ()->Unit ) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp)
            .clip(
                MaterialTheme.shapes.large.copy(
                    topStart = ZeroCornerSize,
                    topEnd = ZeroCornerSize
                )
            )
            .background(Color(0xFF97F0AE))
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
        ) {
            IconButton(onClick = toHome) {
                Image(
                    painter = painterResource(id = R.drawable.btn_back),
                    contentDescription = "back"
                )
            }
        }

        Box(
            modifier = Modifier
                .size(84.dp)
                .clip(CircleShape)
        ) {
            Image(
                painter = painterResource(id = R.drawable.crr_profile_pic),
                contentDescription = null,

                modifier = Modifier
                    .size(84.dp),
                contentScale = ContentScale.Crop
            )
        }

        Text(
            text = name,
            style = MaterialTheme.typography.headlineLarge,
        )

        Text("#${uid}", fontWeight = FontWeight.Bold, fontSize = 18.sp)

        Spacer(Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ){
            Icon(
                Icons.Filled.Circle,
                tint = secondaryLight,
                contentDescription = null,
                modifier = Modifier
                    .height(12.dp)
                    .padding(horizontal = 8.dp)
            )
            Text(
                stringResource(id = R.string.crr_state_active).capitalize(),
                fontSize = 12.sp
            )
        }

        Spacer(Modifier.height(8.dp))
    }
}