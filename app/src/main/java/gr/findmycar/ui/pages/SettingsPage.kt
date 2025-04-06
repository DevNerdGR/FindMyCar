package gr.findmycar.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import gr.findmycar.R
import gr.findmycar.SettingsStore
import gr.findmycar.ViewModels.AuthViewModel
import gr.findmycar.ViewModels.SettingsViewModel
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPage(navController: NavController, authViewModel: AuthViewModel, settingsViewModel: SettingsViewModel) {
    val context = LocalContext.current
    val settingsStore = SettingsStore(context)

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.settingsTitle))
                },
                modifier = Modifier.fillMaxWidth(),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
            )
        }
    ) { padding ->
       LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(20.dp)
       ) {
           item {
               Column(

               ) {
                   Text(
                       stringResource(R.string.appAppearance),
                       style = MaterialTheme.typography.titleMedium
                   )
                   Row(
                       modifier = Modifier
                           .fillParentMaxWidth(),
                       verticalAlignment = Alignment.CenterVertically,
                       horizontalArrangement = Arrangement.spacedBy(10.dp)
                   ) {
                       toggleSetting(
                           label = stringResource(R.string.dynamicThemeSetting),
                           initialState = settingsViewModel.dynamicTheme,
                           onToggle = { b ->
                               settingsViewModel.dynamicTheme = !settingsViewModel.dynamicTheme
                               runBlocking {
                                   settingsStore.saveDynamicTheme(settingsViewModel.dynamicTheme)
                               }
                           }
                       )
                   }
               }
           }
       }
    }
}

@Composable
fun toggleSetting(label : String, initialState : Boolean = false, onToggle : (Boolean) -> Unit) {
    var toggle by remember { mutableStateOf(initialState) }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            modifier = Modifier
                .weight(5f)
                .fillMaxHeight()
        )
        Switch(
            checked = toggle,
            onCheckedChange = {
                toggle = !toggle
                onToggle(toggle)
            }
        )
    }
}