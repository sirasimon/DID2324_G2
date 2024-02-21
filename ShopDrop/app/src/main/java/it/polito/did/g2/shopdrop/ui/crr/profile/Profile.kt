package it.polito.did.g2.shopdrop.ui.crr.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Headset
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import it.polito.did.g2.shopdrop.MainViewModel
import it.polito.did.g2.shopdrop.R
import it.polito.did.g2.shopdrop.navigation.Screens


@Composable
fun CRRProfileScreen(navController: NavController, viewModel: MainViewModel) {
    val screenHeight = with(LocalDensity.current) { LocalConfiguration.current.screenHeightDp.dp }
    val profileHeight = (screenHeight * 0.25f)

    Scaffold(
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
                    Spacer(modifier = Modifier.height(16.dp))
                    ActionCard(
                        title = "Info Personali",
                        icon = Icons.Default.Person
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ActionCard(
                        title = "Storico Consegne",
                        icon = Icons.Default.History
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ActionCard(
                        title = "Assistenza",
                        icon = Icons.Default.Headset
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ActionCard(
                        title = "Impostazioni",
                        icon = Icons.Default.Settings
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    SignOutButton()
                Button(onClick = {navController.navigate(Screens.CrrHomeScreen.route)}){
                    Text(text = "Go to home")
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
        Column {
            Text(
                text = "Giovanni#237739",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(Color.Red, CircleShape)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "attivo",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun ActionCard(title: String, icon: ImageVector) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Handle card click */ },

    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.weight(1f),
                color = Color.Black

            )
            Icon(
                imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = null,
                tint = Color.Black

            )
        }
    }
}

@Composable
fun SignOutButton() {
    Text(
        text = "Sign out",
        style = MaterialTheme.typography.titleSmall,
        color = Color.Red,
        modifier = Modifier
            .fillMaxWidth(0.5f)
            .padding(24.dp)
            .clickable { /* Handle sign out click */ }


    )
}
