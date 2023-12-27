package it.polito.did.g2.shopdrop.ui.login

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import it.polito.did.g2.shopdrop.MainViewModel
import it.polito.did.g2.shopdrop.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

data class User(val email : String, val password : String)

@Composable
fun LoginScreen(navController : NavController, viewModel: MainViewModel){
    var email by rememberSaveable { mutableStateOf("") }
    var emailActive by remember { mutableStateOf(false) }   //TODO: set label to null if active
    var emailError by remember { mutableStateOf(false) }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordActive by remember { mutableStateOf(false) }    //TODO: set label to null if active
    var passwordHidden by rememberSaveable { mutableStateOf(true) }
    var passwordError by remember { mutableStateOf(false) }

    val baseMod = Modifier
        .padding(vertical = 16.dp)
        .fillMaxWidth()

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    fun checkForm(navController: NavController, userData : User, viewModel: MainViewModel){

        if(userData.email.contains("@shopdrop.com")){
            Log.i("FORM_CHECK", "email contains company domain")
            /*
            if(viewModel.login(UserQuery(userData.email, userData.password, UserRole.DLV))) {
                Log.i("FORM_CHECK", "data is correct, redirecting to Delivery Home")
                navController.navigate("DeliveryHome")
            }
            */
            navController.navigate("DeliveryHome")
            /*
            else {
                Log.i("FORM_CHECK", "data is incorrect ($userData)")
                emailError = false
                passwordError = false
            }
             */
        }else{
            navController.navigate("CustomerHome")
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { snackbarData: SnackbarData ->
                    ElevatedCard(
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                        ) {
                            Icon(imageVector = Icons.Default.Warning, contentDescription = "", modifier = Modifier.padding(horizontal = 8.dp))
                            Text(text = stringResource(id = R.string.msg_developing), modifier = Modifier.padding(horizontal = 8.dp))
                            Icon(imageVector = Icons.Default.Warning, contentDescription = "", modifier = Modifier.padding(horizontal = 8.dp))
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(32.dp)
            ) {
                Text("[LOGO]")
                Text(
                    text = stringResource(R.string.title_login).capitalize(),
                    style = MaterialTheme.typography.headlineLarge
                )

                // EMAIL
                OutlinedTextField(
                    placeholder = { Text("Email") },
                    value = email,
                    leadingIcon = {
                        if (email == "")
                            Image(Icons.Outlined.Person, contentDescription = "User icon")
                        else
                            Image(Icons.Filled.Person, contentDescription = "User icon")
                    },
                    onValueChange = { email = it },
                    shape = RoundedCornerShape(16.dp),
                    keyboardOptions = KeyboardOptions(      // Per impostare la tastiera con la chiocciola
                        keyboardType = KeyboardType.Email,
                        imeAction = if (email != "" && password != "") ImeAction.Done else ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { checkForm(navController, User(email, password), viewModel) }
                    ),
                    maxLines = 1,
                    isError = emailError,
                    modifier = baseMod
                )

                // PASSWORD
                OutlinedTextField(
                    placeholder = { Text("Password") },
                    value = password,
                    leadingIcon = {
                        if (password == "")
                            Image(Icons.Outlined.Lock, contentDescription = "Password icon")
                        else
                            Image(Icons.Filled.Lock, contentDescription = "Password icon")
                    },
                    trailingIcon = {
                        IconButton(onClick = { passwordHidden = !passwordHidden }) {
                            val visibilityIcon =
                                if (passwordHidden) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                            // Please provide localized description for accessibility services
                            val description =
                                if (passwordHidden) "Show password" else "Hide password"
                            Icon(imageVector = visibilityIcon, contentDescription = description)
                        }
                    },
                    onValueChange = { password = it },
                    shape = RoundedCornerShape(16.dp),
                    modifier = baseMod,

                    visualTransformation =
                    if (passwordHidden)
                        PasswordVisualTransformation()
                    else
                        VisualTransformation.None,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Password,
                        imeAction = if (email != "" && password != "") ImeAction.Done else ImeAction.Previous
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { checkForm(navController, User(email, password), viewModel) }
                    ),
                    maxLines = 1,
                    isError = passwordError
                )

                // FORGOTTEN PASSWORD
                TextButton(
                    onClick = { performDevMsg(scope, snackbarHostState, context) }, modifier = Modifier.align(Alignment.End)
                ) {
                    Text(text = stringResource(R.string.forgot_pwd).capitalize())
                }

                Button(
                    onClick = { checkForm(navController, User(email, password), viewModel) },
                    modifier = baseMod
                ) {
                    Text(text = stringResource(R.string.btn_log_in).capitalize())
                }

                Text(stringResource(R.string.alternative_login).capitalize())
                Row {
                    Button(onClick = { performDevMsg(scope, snackbarHostState, context) }) {
                        Text(text = "F")
                    }

                    Button(onClick = { performDevMsg(scope, snackbarHostState, context) }) {
                        Text(text = "X")
                    }

                    Button(onClick = { performDevMsg(scope, snackbarHostState, context) }) {
                        Text(text = "G")
                    }
                }
            }
        }
    }
}

fun performDevMsg(scope: CoroutineScope, snackbarHostState: SnackbarHostState, context: Context) {
    scope.launch{
        snackbarHostState.currentSnackbarData?.dismiss()
        snackbarHostState.showSnackbar(
            message = "",
            duration = SnackbarDuration.Short)
    }
}