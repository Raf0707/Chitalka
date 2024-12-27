@file:OptIn(ExperimentalPermissionsApi::class)

package raf.console.chitalka.presentation.screens.settings.data

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import raf.console.chitalka.domain.util.ID
import raf.console.chitalka.domain.util.UIText

@Immutable
sealed class SettingsEvent {
    data class OnGeneralChangeCheckForUpdates(
        val enable: Boolean,
        val activity: ComponentActivity,
        val notificationsPermissionState: PermissionState,
        val onChangeCheckForUpdates: (Boolean) -> Unit
    ) : SettingsEvent()

    data class OnSelectPreviousPreset(
        val onSelected: suspend (name: UIText) -> Unit
    ) : SettingsEvent()

    data class OnSelectNextPreset(
        val onSelected: suspend (name: UIText) -> Unit
    ) : SettingsEvent()

    data class OnSelectColorPreset(val id: ID) : SettingsEvent()
    data class OnDeleteColorPreset(val id: ID) : SettingsEvent()
    data class OnScrollToColorPreset(val scrollTo: Int) : SettingsEvent()
    data class OnUpdateColorPresetTitle(val id: ID, val title: String) : SettingsEvent()
    data class OnShuffleColorPreset(val id: ID) : SettingsEvent()
    data class OnUpdateColorPresetColor(
        val id: ID,
        val backgroundColor: Color?,
        val fontColor: Color?
    ) : SettingsEvent()

    data class OnAddColorPreset(val backgroundColor: Color, val fontColor: Color) : SettingsEvent()
    data class OnReorderColorPresets(val from: Int, val to: Int) : SettingsEvent()
    data object OnConfirmReorderColorPresets : SettingsEvent()
}