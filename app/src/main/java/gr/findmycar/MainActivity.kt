package gr.findmycar

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import gr.findmycar.ui.pages.LoginPage
import gr.findmycar.ui.theme.AppTheme
import androidx.navigation.compose.composable
import gr.findmycar.ViewModels.AuthViewModel
import gr.findmycar.ViewModels.ParkingLotViewModel
import gr.findmycar.ViewModels.SettingsViewModel
import gr.findmycar.ui.pages.HomePage
import gr.findmycar.ui.pages.SettingsPage
import kotlinx.coroutines.runBlocking

val Context.datastore : DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val settingsStore = SettingsStore(applicationContext)

        val authViewModel : AuthViewModel by viewModels()
        val parkingLotViewModel : ParkingLotViewModel by viewModels()
        val settingsViewModel : SettingsViewModel by viewModels()

        runBlocking { settingsStore.reloadViewModels(settingsViewModel) }

        setContent() {
            AppTheme(
                dynamicColor = settingsViewModel.dynamicTheme
            ) {
                val navController = rememberNavController()

                var start = "login"

                NavHost(navController = navController, startDestination = start) {
                    composable("login") { LoginPage(navController, authViewModel) }
                    composable("home") { HomePage(navController, authViewModel, parkingLotViewModel) }
                    composable("settings") { SettingsPage(navController, authViewModel, settingsViewModel) }
                }
            }
        }
    }
}

