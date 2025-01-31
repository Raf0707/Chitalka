package raf.console.chitalka

import android.annotation.SuppressLint
import android.database.CursorWindow
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import raf.console.chitalka.presentation.core.components.navigation_bar.NavigationBar
import raf.console.chitalka.presentation.core.components.navigation_rail.NavigationRail
import raf.console.chitalka.presentation.core.navigation.NavigationHost
import raf.console.chitalka.presentation.core.navigation.Screen
import raf.console.chitalka.presentation.data.MainEvent
import raf.console.chitalka.presentation.data.MainViewModel
import raf.console.chitalka.presentation.screens.about.AboutScreenRoot
import raf.console.chitalka.presentation.screens.about.nested.credits.CreditsScreenRoot
import raf.console.chitalka.presentation.screens.about.nested.license_info.LicenseInfoScreenRoot
import raf.console.chitalka.presentation.screens.about.nested.licenses.LicensesScreenRoot
import raf.console.chitalka.presentation.screens.book_info.BookInfoScreenRoot
import raf.console.chitalka.presentation.screens.browse.BrowseScreenRoot
import raf.console.chitalka.presentation.screens.browse.data.BrowseViewModel
import raf.console.chitalka.presentation.screens.help.HelpScreenRoot
import raf.console.chitalka.presentation.screens.history.HistoryScreenRoot
import raf.console.chitalka.presentation.screens.history.data.HistoryViewModel
import raf.console.chitalka.presentation.screens.library.LibraryScreenRoot
import raf.console.chitalka.presentation.screens.library.data.LibraryViewModel
import raf.console.chitalka.presentation.screens.reader.ReaderScreenRoot
import raf.console.chitalka.presentation.screens.settings.SettingsScreenRoot
import raf.console.chitalka.presentation.screens.settings.data.SettingsViewModel
import raf.console.chitalka.presentation.screens.settings.nested.appearance.AppearanceSettingsRoot
import raf.console.chitalka.presentation.screens.settings.nested.browse.BrowseSettingsRoot
import raf.console.chitalka.presentation.screens.settings.nested.general.GeneralSettingsRoot
import raf.console.chitalka.presentation.screens.settings.nested.reader.ReaderSettingsRoot
import raf.console.chitalka.presentation.screens.start.StartScreenRoot
import raf.console.chitalka.presentation.ui.BookStoryTheme
import raf.console.chitalka.presentation.ui.Transitions
import raf.console.chitalka.presentation.ui.isDark
import raf.console.chitalka.presentation.ui.isPureDark
import java.lang.reflect.Field


@Suppress("unused")
@SuppressLint("DiscouragedPrivateApi")
@AndroidEntryPoint
class Activity : AppCompatActivity() {
    // Initializing all required viewModels on app startup
    private val mainViewModel: MainViewModel by viewModels()
    private val libraryViewModel: LibraryViewModel by viewModels()
    private val historyViewModel: HistoryViewModel by viewModels()
    private val browseViewModel: BrowseViewModel by viewModels()
    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // New Cursor size for Room
        try {
            val field: Field = CursorWindow::class.java.getDeclaredField("sCursorWindowSize")
            field.isAccessible = true
            field.set(null, 100 * 1024 * 1024)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Initializing all variables
        mainViewModel.onEvent(
            MainEvent.OnInit(
                libraryViewModelReady = libraryViewModel.isReady,
                settingsViewModelReady = settingsViewModel.isReady
            )
        )

        // Splash screen
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                !mainViewModel.isReady.value
            }
        }

        // Edge to edge
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val state = MainViewModel.getState()
            val isLoaded = mainViewModel.isReady.collectAsStateWithLifecycle()

            val density = LocalDensity.current
            val imeInsets = WindowInsets.ime
            val focusManager = LocalFocusManager.current
            val isKeyboardVisible by remember {
                derivedStateOf {
                    imeInsets.getBottom(density) > 0
                }
            }

            LaunchedEffect(isKeyboardVisible) {
                if (!isKeyboardVisible) {
                    focusManager.clearFocus()
                }
            }

            if (isLoaded.value) {
                BookStoryTheme(
                    theme = state.value.theme,
                    isDark = state.value.darkTheme.isDark(),
                    isPureDark = state.value.pureDark.isPureDark(this),
                    themeContrast = state.value.themeContrast
                ) {
                    NavigationHost(
                        startScreen = if (state.value.showStartScreen) Screen.Start
                        else Screen.Library
                    ) {
                        navigation(
                            screens = arrayOf(
                                Screen.Library.getRoute(),
                                Screen.History.getRoute(),
                                Screen.Browse.getRoute()
                            ),
                            bottomBar = {
                                NavigationBar()
                            },
                            navigationRail = {
                                NavigationRail()
                            }
                        ) {
                            // Library
                            composable<Screen.Library>(
                                enterAnim = Transitions.FadeTransitionIn,
                                exitAnim = Transitions.FadeTransitionOut
                            ) {
                                LibraryScreenRoot()
                            }

                            // History
                            composable<Screen.History>(
                                enterAnim = Transitions.FadeTransitionIn,
                                exitAnim = Transitions.FadeTransitionOut
                            ) {
                                HistoryScreenRoot()
                            }

                            // Browse
                            composable<Screen.Browse>(
                                enterAnim = Transitions.FadeTransitionIn,
                                exitAnim = Transitions.FadeTransitionOut
                            ) {
                                BrowseScreenRoot()
                            }
                        }

                        // Book Info
                        composable<Screen.BookInfo> {
                            BookInfoScreenRoot(it)
                        }

                        // Reader
                        composable<Screen.Reader> {
                            ReaderScreenRoot(it)
                        }

                        // Settings
                        composable<Screen.Settings> {
                            SettingsScreenRoot()
                        }

                        // Nested settings categories
                        composable<Screen.Settings.General> {
                            GeneralSettingsRoot()
                        }
                        composable<Screen.Settings.Appearance> {
                            AppearanceSettingsRoot()
                        }
                        composable<Screen.Settings.ReaderSettings> {
                            ReaderSettingsRoot()
                        }
                        composable<Screen.Settings.BrowseSettings> {
                            BrowseSettingsRoot()
                        }

                        // About screen
                        composable<Screen.About> {
                            AboutScreenRoot()
                        }

                        // Nested about categories
                        composable<Screen.About.Licenses> {
                            LicensesScreenRoot()
                        }
                        composable<Screen.About.LicenseInfo> {
                            LicenseInfoScreenRoot(it)
                        }
                        composable<Screen.About.Credits> {
                            CreditsScreenRoot()
                        }

                        // Help screen
                        composable<Screen.Help> {
                            HelpScreenRoot(it)
                        }

                        // Start screen
                        composable<Screen.Start>(enterAnim = Transitions.FadeTransitionIn) {
                            StartScreenRoot()
                        }
                    }
                }
            }
        }
    }
}