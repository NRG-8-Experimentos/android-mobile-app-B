package com.example.synhub.shared.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import nrg.inc.synhub.R
import com.example.synhub.shared.icons.lockSVG
import com.example.synhub.shared.icons.personSVG
import com.example.synhub.shared.viewmodel.LogInViewModel
import nrg.inc.synhub.shared.components.LanguageSwitchingButtons
import kotlin.math.log


@Composable
fun Login(nav: NavHostController) {
    Scaffold (
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFFFFFFFF)
    ){
        innerPadding -> LoginScreen(modifier =Modifier.padding(innerPadding), nav)
    }
}

@Composable
fun LoginScreen(modifier: Modifier, nav: NavHostController , loginViewModel: LogInViewModel = viewModel()){

    var txtUser by remember { mutableStateOf("") }
    var txtPass by remember { mutableStateOf("") }

    var shouldNavigate by remember { mutableStateOf(false) }
    var isLeader by remember { mutableStateOf(false) }
    val loginSuccess by loginViewModel.loginSuccess.collectAsState()
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val userNotLeader= stringResource(id = R.string.user_not_leader)
    val incorrectCredentials= stringResource(id = R.string.invalid_credentials)

    LaunchedEffect(loginSuccess) {
        //println("LoginScreen: loginSuccess cambió a $loginSuccess")
        if (loginSuccess == true) {
            //println("LoginScreen: Intentando obtener detalles de líder...")
            isLeader = loginViewModel.getLeaderDetails()
            //println("LoginScreen: ¿Es líder? $isLeader")
            shouldNavigate = isLeader
            //println("LoginScreen: shouldNavigate actualizado a $shouldNavigate")
            if (isLeader) {
                shouldNavigate = false
                isLeader = false
                loginViewModel.resetLoginState()
                nav.navigate("Home"){
                    popUpTo("Login") { inclusive = true }
                }
            } else {
                // Reinicia estados si no es líder
                shouldNavigate = false
                isLeader = false
                loginViewModel.resetLoginState()
                //println("LoginScreen: Usuario no es líder, estados reiniciados")
                errorMessage = userNotLeader
                showErrorDialog = true
            }
            // Reinicia el estado de loginSuccess para permitir nuevos intentos
            loginViewModel.resetLoginState()
        } else if (loginSuccess == false) {
            shouldNavigate = false
            isLeader = false
            loginViewModel.resetLoginState()
            //println("LoginScreen: Estados reiniciados tras fallo de login")
            // Mostrar dialog de error o mensaje al usuario
            errorMessage = incorrectCredentials
            showErrorDialog = true
        }
    }

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text(text = stringResource(id = R.string.error_title), textAlign = TextAlign.Center, color = Color(0xFF1A4E85), fontWeight = FontWeight.Bold) },
            text = { Text(text = errorMessage, textAlign = TextAlign.Center, fontSize = 16.sp)},
            confirmButton = {
                TextButton(onClick = { showErrorDialog = false }) {
                    Text(stringResource(id = R.string.accept), color = Color(0xFF1A4E85))
                }
            }
        )
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp)
            .padding(vertical = 40.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "SynHub",
            fontSize = 70.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A4E85),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 20.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.synhub_logo),
            contentDescription = null,
            modifier = Modifier
                .height(200.dp)
        )
        Text(
            text = stringResource(id = R.string.login_message),
            fontSize = 20.sp,
            color = Color(0xFF000000),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        OutlinedTextField(
            value = txtUser,
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = stringResource(id = R.string.insert_user_label))},
            placeholder = { Text(text = stringResource(id = R.string.user_placeholder))},
            leadingIcon = {
                Icon(
                    imageVector = personSVG,
                    tint = Color.Gray,
                    contentDescription = ""
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF3F3F3),
                unfocusedContainerColor = Color.White,
                cursorColor = Color.Cyan
            ),
            onValueChange = {txtUser=it}
        )

        OutlinedTextField(
            value = txtPass,
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = stringResource(id = R.string.insert_password_label))},
            placeholder = { Text(text = stringResource(id = R.string.password_placeholder))},
            leadingIcon = {
                Icon(
                    imageVector = lockSVG,
                    tint = Color.Gray,
                    contentDescription = ""
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF3F3F3),
                unfocusedContainerColor = Color.White,
                cursorColor = Color.Cyan
            ),
            visualTransformation = PasswordVisualTransformation(),
            onValueChange = {txtPass=it}
        )

        ElevatedButton(
            colors = ButtonDefaults.buttonColors(Color(0xFF4A90E2)),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier,
            onClick = {
                loginViewModel.signIn(txtUser, txtPass)
                //println("LoginScreen: Intentando iniciar sesión con usuario: $txtUser")
            }
        ) {
            Text(
                text = stringResource(id = R.string.sign_in), fontSize = 20.sp,
                color = Color.White, fontWeight = FontWeight.Bold
            )
        }

        ElevatedButton(
            colors = ButtonDefaults.buttonColors(Color(0xFFFFFFFF)),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier,
            onClick = {
                nav.navigate("Register")
            }
        ) {
            Text(
                text = stringResource(id = R.string.sign_up), fontSize = 20.sp,
                color = Color.Black, fontWeight = FontWeight.Bold
            )

        }


    }
}