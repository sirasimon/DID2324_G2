package it.polito.did.s306067.lab04

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import it.polito.did.s306067.lab04.ui.theme.Lab04Theme

class MainActivity : ComponentActivity() {

    private val vm by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vm.startConnection()

        setContent {
            Lab04Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ContenitoreDatiTest(vm)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
fun ContenitoreDatiTest(vm : MainViewModel){
    val test by vm.testText.observeAsState()
    val nick by vm.nickname.observeAsState()
    val email by vm.email.observeAsState()

    Column(){
        Text(
            text= "UID: ",
            fontWeight = FontWeight.Bold
        )
        Text(vm.getUID())
        Text(
            text= "Test: ",
            fontWeight = FontWeight.Bold
        )
        Text(test?:"null")
        //Text(vm.testText.value?:"null")
        Text(
            text= "User DB ref: ",
            fontWeight = FontWeight.Bold
        )
        Text(vm.users.toString())
        Text(
            text= "Nickname: ",
            fontWeight = FontWeight.Bold
        )
        //Text(vm.nickname.value?:"null")
        Text(nick?:"null")
        Text(
            text= "Email: ",
            fontWeight = FontWeight.Bold
        )
        Text(email?:"null")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Lab04Theme {
        Greeting("Android")
    }
}