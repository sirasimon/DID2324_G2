package it.polito.did.g2.shopdrop.ui.crr.delivey
import android.text.Layout
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Headset
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import it.polito.did.g2.shopdrop.MainViewModel
import it.polito.did.g2.shopdrop.R
import it.polito.did.g2.shopdrop.navigation.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CRRDeliveryScreen (navController: NavController,viewModel: MainViewModel){

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,


                    ) {
                        IconButton(onClick = { navController.navigate(Screens.CrrHomeScreen.route) }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")

                        }
                        Text(text = "Ordine 1", modifier = Modifier.weight(1f).padding(start=100.dp))

                    }
                },

            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            //TODO: ID card

            Column(modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
                verticalArrangement = Arrangement.Center,
               )
                { Icon(Icons.Default.Check, contentDescription = "Check", tint = Color.Green,modifier=Modifier.align(Alignment.CenterHorizontally))
                    Text(
                        text = "Da ritirare",
                        color = Color.Green,
                        style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier

                            .align(Alignment.CenterHorizontally)

                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Row() {

                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Indirizzo Supermercato",
                            color = Color.Black,
                            style = MaterialTheme.typography.titleLarge,



                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth().background(Color.LightGray).padding(4.dp)


                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Map, contentDescription = "Map", tint = Color.Black)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Carrefour Express",
                                    color = Color.Black,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    text = "Corso G.Ferraris 24",
                                    color = Color.Black,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth().background(Color.LightGray).padding(4.dp)

                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Schedule, contentDescription = "Schedule", tint = Color.Black)

                            Text(
                                text = " Arriverai tra 30-35 minuti",
                                color = Color.Black,
                                style = MaterialTheme.typography.bodySmall

                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = "Indirizzo Locker",
                        color = Color.Black,
                        style = MaterialTheme.typography.titleLarge,

                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth().background(Color.LightGray).padding(4.dp)

                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Map, contentDescription = "Map", tint = Color.Black)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Shopdrop Quadrilatero 2",
                                    color = Color.Black,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "Via Bligny 8",
                                    color = Color.Black,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.weight(3f))
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 16.dp), // Aggiungi un padding dal basso per spaziare il bottone dal fondo dello schermo
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            onClick = { /* Handle scan order click */ },
                            colors = ButtonDefaults.buttonColors(Color(0xFFFFA500)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Scansiona ordine", color = Color.White)
                        }
                    }}


}}}
