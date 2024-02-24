package it.polito.did.g2.shopdrop.ui.login

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import it.polito.did.g2.shopdrop.MainViewModel
import it.polito.did.g2.shopdrop.R
import it.polito.did.g2.shopdrop.data.users.UserQuery
import it.polito.did.g2.shopdrop.data.users.UserRole
import it.polito.did.g2.shopdrop.navigation.ADM_ROUTE
import it.polito.did.g2.shopdrop.navigation.CRR_ROUTE
import it.polito.did.g2.shopdrop.navigation.CST_ROUTE
import it.polito.did.g2.shopdrop.ui.cst.common.WIPMessage
import it.polito.did.g2.shopdrop.ui.cst.common.performDevMsg
import it.polito.did.g2.shopdrop.ui.theme.errorContainerLight
import it.polito.did.g2.shopdrop.ui.theme.secondaryLight
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class User(val email : String, val password : String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController : NavController, viewModel: MainViewModel){

    // FORM VARS
    var email by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf(false) }
    var password by remember { mutableStateOf("") }
    var passwordHidden by rememberSaveable { mutableStateOf(true) }
    var passwordError by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    val loginScope = rememberCoroutineScope()

    val baseMod = Modifier
        //.padding(vertical = 16.dp)
        .fillMaxWidth()

    // SNACKBAR
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    fun login(){
        Log.i("BTN_LOGIN", "LOGIN PRESSED")
        Log.i("BTN_LOGIN", "\tisLoading = $isLoading, setting it to true")
        Log.i("BTN_LOGIN", "\temailError = $emailError, passwordError = $passwordError")
        isLoading = true

        loginScope.launch{
            Log.i("BTN_LOGIN", "\tLaunching suspend fun of view model")
            emailError = false
            passwordError = false


            var userQuery = UserQuery(email, password)
            Log.i("BTN_LOGIN", "\tCreating login query: $userQuery")

            //viewModel.login(userQuery)
            viewModel.validateCredentials(userQuery)

            delay(1500)
            Log.i("BTN_LOGIN", "\tQuery changed to: $userQuery")

            // Arresta l'animazione di caricamento
            isLoading = false
            Log.i("BTN_LOGIN", "\tisLoading = $isLoading, setting it to false")

            if (userQuery.role!=null && userQuery.errType==null) {
                viewModel.login(userQuery.email)

                when(userQuery.role){
                    UserRole.ADM ->{
                        Log.i("BTN_LOGIN", "\tVALID CREDENTIALS: role is ${userQuery.role} (exp ADM), navigating to Carrier Home")
                        navController.navigate(ADM_ROUTE)
                    }
                    UserRole.CST -> {
                        Log.i("BTN_LOGIN", "\tVALID CREDENTIALS: role is ${userQuery.role} (exp CST), navigating to Customer Home")
                        navController.navigate(CST_ROUTE)
                    }
                    UserRole.CRR -> {
                        Log.i("BTN_LOGIN", "\tVALID CREDENTIALS: role is ${userQuery.role} (exp CRR), navigating to Carrier Home")
                        navController.navigate(CRR_ROUTE)
                    }
                    else -> {/*TODO forse qui l'alternativa all'if*/}
                }
            } else {
                // Mostra un messaggio di errore all'utente

                when(userQuery.errType){
                    UserQuery.LOGIN_ERROR_TYPE.UNKNOWN ->{
                        //TODO
                    }
                    UserQuery.LOGIN_ERROR_TYPE.NOT_FOUND ->{
                        Log.e("BTN_LOGIN", "\tEMAIL NOT FOUNT: error is ${userQuery.errType}")

                        emailError = true
                        passwordError = false
                    }
                    UserQuery.LOGIN_ERROR_TYPE.PASSWORD ->{
                        Log.e("BTN_LOGIN", "\tWRONG PASSWORD: error is ${userQuery.errType}")

                        emailError = false
                        passwordError = true
                    }

                    else -> {
                        emailError = true
                        passwordError = true
                    }
                }
            }

        }
    }

    Scaffold(
        snackbarHost = { WIPMessage(snackbarHostState)}
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            Box(Modifier.fillMaxSize()){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(32.dp)
                ) {

                    //LOGO
                    Image(painterResource(id = R.drawable.shopdrop_logo), contentDescription = null, Modifier.height(128.dp))
                    Spacer(Modifier.height(16.dp))

                    //LOGIN TITLE
                    Text(
                        text = stringResource(R.string.title_login).capitalize(),
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier.align(Alignment.Start)
                    )

                    Spacer(Modifier.height(16.dp))

                    var emlColor = if(emailError) errorContainerLight else secondaryLight

                    // EMAIL
                    OutlinedTextField(
                        placeholder = { Text("Email") },
                        value = email,
                        leadingIcon = {
                            if (email == "")
                                Icon(Icons.Outlined.Person, contentDescription = "User icon", tint = emlColor)
                            else
                                Icon(Icons.Filled.Person, contentDescription = "User icon", tint = emlColor)
                        },
                        onValueChange = { email = it },
                        shape = RoundedCornerShape(16.dp),
                        keyboardOptions = KeyboardOptions(      // Per impostare la tastiera con la chiocciola
                            keyboardType = KeyboardType.Email,
                            imeAction = if (email != "" && password != "") ImeAction.Done else ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            //onDone = { checkForm(navController, User(email, password), viewModel) }
                            onDone = { login() }
                        ),
                        maxLines = 1,
                        isError = emailError,
                        modifier = baseMod,
                        supportingText = {if(emailError) Text(stringResource(id = R.string.err_email_not_found))},
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = secondaryLight,
                            unfocusedBorderColor = secondaryLight,
                            errorPrefixColor = errorContainerLight
                        )
                    )

                    Spacer(Modifier.height(8.dp))

                    var pwdColor = if(passwordError) errorContainerLight else secondaryLight

                    // PASSWORD
                    OutlinedTextField(
                        placeholder = { Text("Password") },
                        value = password,
                        leadingIcon = {
                            if (password == "")
                                Icon(Icons.Outlined.Lock, contentDescription = "Password icon", tint = pwdColor)
                            else
                                Icon(Icons.Filled.Lock, contentDescription = "Password icon", tint = pwdColor)
                        },
                        trailingIcon = {
                            IconButton(onClick = { passwordHidden = !passwordHidden }) {
                                val visibilityIcon =
                                    if (password == "") Icons.Outlined.Visibility
                                    else if (passwordHidden) Icons.Filled.Visibility
                                    else Icons.Filled.VisibilityOff
                                val description =
                                    if (passwordHidden) "Show password" else "Hide password"
                                Icon(imageVector = visibilityIcon, contentDescription = description, tint = pwdColor)
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
                            //onDone = { checkForm(navController, User(email, password), viewModel) }
                            onDone = { login() }
                        ),
                        maxLines = 1,
                        isError = passwordError,
                        supportingText = {if(passwordError) Text(stringResource(id = R.string.err_pwd_not_valid))},
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = secondaryLight,
                            unfocusedBorderColor = secondaryLight,
                            errorPrefixColor = errorContainerLight
                        )
                    )

                    // FORGOTTEN PASSWORD
                    TextButton(
                        onClick = { performDevMsg(scope, snackbarHostState, context) },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(text = stringResource(R.string.forgot_pwd).capitalize(), color = secondaryLight)
                    }

                    Spacer(Modifier.height(32.dp))

                    // LOGIN BTN
                    Button(
                        onClick = { login() },
                        enabled = !isLoading,
                        modifier = baseMod
                    ) {
                        Text(text = stringResource(R.string.btn_log_in).capitalize())
                    }

                    Spacer(Modifier.height(32.dp))

                    Text(stringResource(R.string.alternative_login).capitalize(), Modifier.padding(vertical=16.dp))
                    Row {
                        FloatingActionButton(
                            onClick = { performDevMsg(scope, snackbarHostState, context) },
                            containerColor = Color.White,
                            modifier = Modifier.padding(4.dp)
                        ) {
                            Image(painterResource(id = R.drawable.google), contentDescription = null)
                        }
                        FloatingActionButton(
                            onClick = { performDevMsg(scope, snackbarHostState, context) },
                            containerColor = Color.White,
                            modifier = Modifier.padding(4.dp)
                        ) {
                            Image(painterResource(id = R.drawable.facebook), contentDescription = null)
                        }
                        FloatingActionButton(
                            onClick = { performDevMsg(scope, snackbarHostState, context) },
                            containerColor = Color.White,
                            modifier = Modifier.padding(4.dp)
                        ) {
                            Image(painterResource(id = R.drawable.twitter), contentDescription = null, modifier = Modifier.padding(8.dp))
                        }
                        FloatingActionButton(
                            onClick = { performDevMsg(scope, snackbarHostState, context) },
                            containerColor = Color.White,
                            modifier = Modifier.padding(4.dp)
                        ) {
                            Image(painterResource(id = R.drawable.linkedin), contentDescription = null)
                        }
                    }
                }
            }

            if(isLoading){
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(Color(0x77FFFFFF))){
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(50.dp)
                            .align(Alignment.Center)
                    )
                }
            }

        }
    }
}