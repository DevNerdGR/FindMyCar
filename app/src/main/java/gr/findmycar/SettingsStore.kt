package gr.findmycar

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import gr.findmycar.ViewModels.SettingsViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "userLocalSettings")

class SettingsStore(val context: Context) {
    companion object {
        val DYNAMIC_THEME = booleanPreferencesKey("dynamicTheme")
    }

    val dynamicTheme : Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[DYNAMIC_THEME] ?: false
    }

    suspend fun saveDynamicTheme(b : Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DYNAMIC_THEME] = b
        }
    }

    suspend fun reloadViewModels(settingsViewModel: SettingsViewModel) {
        settingsViewModel.dynamicTheme = dynamicTheme.first()
    }
}