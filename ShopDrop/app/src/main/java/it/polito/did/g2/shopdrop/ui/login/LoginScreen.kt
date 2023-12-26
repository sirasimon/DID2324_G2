package it.polito.did.g2.shopdrop.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

data class User(val email : String, val password : String)

@Composable
fun LoginScreen(navController : NavController){
    var email by rememberSaveable { mutableStateOf("") }
    var emailActive by remember { mutableStateOf(false) }   //TODO: set label to null if active
    var password by rememberSaveable { mutableStateOf("") }
    var passwordActive by remember { mutableStateOf(false) }    //TODO: set label to null if active
    var passwordHidden by rememberSaveable { mutableStateOf(true) }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text("[LOGO]")
            Text("Login")

            // EMAIL
            TextField(
                label = { Text("Email") },
                value = email,
                leadingIcon = {
                    Image(Icons.Filled.Person, contentDescription = "User icon")
                },
                onValueChange = { email = it }
            )

            // PASSWORD
            TextField(
                label = {
                    Text("Password")
                },
                value = password,
                leadingIcon = {
                    Image(Icons.Filled.Lock, contentDescription = "Password icon")
                },
                trailingIcon = {
                    IconButton(onClick = { passwordHidden = !passwordHidden }) {
                        val visibilityIcon =
                            if (passwordHidden) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        // Please provide localized description for accessibility services
                        val description = if (passwordHidden) "Show password" else "Hide password"
                        Icon(imageVector = visibilityIcon, contentDescription = description)
                    }
                },
                onValueChange = { password = it },
                visualTransformation =
                    if (passwordHidden) PasswordVisualTransformation() else VisualTransformation.None,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            )

            Text("Password dimenticata?")

            Button(onClick = { checkForm(navController, User(email, password)) }) {
                Text(text = "Login")
            }

            Text("Effettua il login con...")
            Row {
                //TODO Add icons and snackbar to highlight in-development functions
            }
        }
    }
}

fun checkForm(navController: NavController, userData : User){
    if(userData.email.contains("@shopdrop.com")){
        navController.navigate("DeliveryHome")
    }else{
        navController.navigate("ClientHome")
    }
}