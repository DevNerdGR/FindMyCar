package gr.findmycar.ui.pages

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import gr.findmycar.R
import gr.findmycar.ViewModels.AuthState
import gr.findmycar.ViewModels.AuthViewModel
import gr.findmycar.ui.clearFocusOnKeyboardDismiss
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPage(navController: NavController, authViewModel: AuthViewModel) {
    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current
    val toastStr = stringResource(R.string.failedLoginMessage)

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Authenticated -> navController.navigate("home")
            is AuthState.Error -> Toast.makeText(context, toastStr, Toast.LENGTH_SHORT).show()
            else -> Unit
        }
    }


    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(30.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var email by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }
            Image(
                painter = painterResource(R.drawable.ic_launcher),
                contentDescription = "Icon"
            )
            Text(
                text = stringResource(R.string.loginTitleTemplate).format(stringResource(R.string.app_name)),
                style = MaterialTheme.typography.headlineLarge
            )
            HorizontalDivider()
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                },
                placeholder = {
                    Text(stringResource(R.string.emailPlaceholder))
                },
                singleLine = true,
                modifier = Modifier
                    .clearFocusOnKeyboardDismiss()
            )
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                },
                placeholder = {
                    Text(stringResource(R.string.passwordPlaceholder))
                },
                singleLine = true,
                modifier = Modifier
                    .clearFocusOnKeyboardDismiss()
            )
            Button(
                enabled = (authState.value != AuthState.Loading) && !email.isEmpty() && !password.isEmpty(),
                onClick = {
                    authViewModel.login(email, password)
                }
            ) {
                Text(stringResource(R.string.loginButton))
            }
            val tooltipState = rememberTooltipState(isPersistent = true)
            val scope = rememberCoroutineScope()
            TooltipBox(
                positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                tooltip = {
                    PlainTooltip {
                        Text(stringResource(R.string.noAccountExplaination))
                    }
                },
                state = tooltipState
            ) {
                TextButton(
                    onClick = {
                        scope.launch {
                            tooltipState.show()
                        }
                    }
                ) {
                    Text(
                        stringResource(R.string.noAccountButton),
                        modifier = Modifier
                            .padding(10.dp)
                    )
                }
            }

        }
    }
}

