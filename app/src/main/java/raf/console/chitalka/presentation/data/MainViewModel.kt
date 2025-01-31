package raf.console.chitalka.presentation.data

import androidx.compose.runtime.Composable
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import raf.console.chitalka.domain.use_case.data_store.ChangeLanguage
import raf.console.chitalka.domain.use_case.data_store.GetAllSettings
import raf.console.chitalka.domain.use_case.data_store.SetDatastore
import raf.console.chitalka.domain.use_case.remote.CheckForUpdates
import raf.console.chitalka.domain.util.UIViewModel
import raf.console.chitalka.presentation.core.constants.Constants
import raf.console.chitalka.presentation.core.constants.DataStoreConstants
import raf.console.chitalka.presentation.core.constants.provideFonts
import raf.console.chitalka.presentation.core.constants.provideMainState
import raf.console.chitalka.presentation.screens.settings.nested.browse.data.toBrowseFilesStructure
import raf.console.chitalka.presentation.screens.settings.nested.browse.data.toBrowseLayout
import raf.console.chitalka.presentation.screens.settings.nested.browse.data.toBrowseSortOrder
import raf.console.chitalka.presentation.screens.settings.nested.reader.data.toReaderScreenOrientation
import raf.console.chitalka.presentation.screens.settings.nested.reader.data.toTextAlignment
import raf.console.chitalka.presentation.ui.toDarkTheme
import raf.console.chitalka.presentation.ui.toPureDark
import raf.console.chitalka.presentation.ui.toTheme
import raf.console.chitalka.presentation.ui.toThemeContrast
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val stateHandle: SavedStateHandle,

    private val setDatastore: SetDatastore,
    private val changeLanguage: ChangeLanguage,
    private val checkForUpdates: CheckForUpdates,
    private val getAllSettings: GetAllSettings
) : UIViewModel<MainState, MainEvent>() {

    companion object {
        @Composable
        fun getState() = getState<MainViewModel, MainState, MainEvent>()

        @Composable
        fun getEvent() = getEvent<MainViewModel, MainState, MainEvent>()
    }

    private val _isReady = MutableStateFlow(false)
    val isReady = _isReady.asStateFlow()

    private val isSettingsReady = MutableStateFlow(false)
    private val isViewModelReady = MutableStateFlow(false)

    private val _state: MutableStateFlow<MainState> = MutableStateFlow(
        stateHandle[Constants.provideMainState()] ?: MainState()
    )
    override val state = _state.asStateFlow()

    override fun onEvent(event: MainEvent) {
        when (event) {
            is MainEvent.OnInit -> init(event)

            is MainEvent.OnChangeLanguage -> handleLanguageUpdate(event)

            is MainEvent.OnChangeDarkTheme -> handleDatastoreUpdate(
                key = DataStoreConstants.DARK_THEME,
                value = event.value,
                updateState = {
                    it.copy(darkTheme = toDarkTheme())
                }
            )

            is MainEvent.OnChangePureDark -> handleDatastoreUpdate(
                key = DataStoreConstants.PURE_DARK,
                value = event.value,
                updateState = {
                    it.copy(pureDark = toPureDark())
                }
            )

            is MainEvent.OnChangeThemeContrast -> handleDatastoreUpdate(
                key = DataStoreConstants.THEME_CONTRAST,
                value = event.value,
                updateState = {
                    it.copy(themeContrast = toThemeContrast())
                }
            )

            is MainEvent.OnChangeTheme -> handleDatastoreUpdate(
                key = DataStoreConstants.THEME,
                value = event.value,
                updateState = {
                    it.copy(theme = toTheme())
                }
            )

            is MainEvent.OnChangeFontFamily -> handleDatastoreUpdate(
                key = DataStoreConstants.FONT,
                value = event.value,
                updateState = {
                    it.copy(
                        fontFamily = Constants.provideFonts(withRandom = true)
                            .find { font -> font.id == event.value }?.id
                            ?: Constants.provideFonts(withRandom = false)[0].id
                    )
                }
            )

            is MainEvent.OnChangeFontStyle -> handleDatastoreUpdate(
                key = DataStoreConstants.IS_ITALIC,
                value = event.value,
                updateState = {
                    it.copy(isItalic = this)
                }
            )

            is MainEvent.OnChangeFontSize -> handleDatastoreUpdate(
                key = DataStoreConstants.FONT_SIZE,
                value = event.value,
                updateState = {
                    it.copy(fontSize = this)
                }
            )

            is MainEvent.OnChangeLineHeight -> handleDatastoreUpdate(
                key = DataStoreConstants.LINE_HEIGHT,
                value = event.value,
                updateState = {
                    it.copy(lineHeight = this)
                }
            )

            is MainEvent.OnChangeParagraphHeight -> handleDatastoreUpdate(
                key = DataStoreConstants.PARAGRAPH_HEIGHT,
                value = event.value,
                updateState = {
                    it.copy(paragraphHeight = this)
                }
            )

            is MainEvent.OnChangeParagraphIndentation -> handleDatastoreUpdate(
                key = DataStoreConstants.PARAGRAPH_INDENTATION,
                value = event.value,
                updateState = {
                    it.copy(paragraphIndentation = this)
                }
            )

            is MainEvent.OnChangeShowStartScreen -> handleDatastoreUpdate(
                key = DataStoreConstants.SHOW_START_SCREEN,
                value = event.value,
                updateState = {
                    it.copy(showStartScreen = this)
                }
            )

            is MainEvent.OnChangeCheckForUpdates -> handleDatastoreUpdate(
                key = DataStoreConstants.CHECK_FOR_UPDATES,
                value = event.value,
                updateState = {
                    it.copy(checkForUpdates = this)
                }
            )

            is MainEvent.OnChangeSidePadding -> handleDatastoreUpdate(
                key = DataStoreConstants.SIDE_PADDING,
                value = event.value,
                updateState = {
                    it.copy(sidePadding = this)
                }
            )

            is MainEvent.OnChangeDoubleClickTranslation -> handleDatastoreUpdate(
                key = DataStoreConstants.DOUBLE_CLICK_TRANSLATION,
                value = event.value,
                updateState = {
                    it.copy(doubleClickTranslation = this)
                }
            )

            is MainEvent.OnChangeFastColorPresetChange -> handleDatastoreUpdate(
                key = DataStoreConstants.FAST_COLOR_PRESET_CHANGE,
                value = event.value,
                updateState = {
                    it.copy(fastColorPresetChange = this)
                }
            )

            is MainEvent.OnChangeBrowseFilesStructure -> handleDatastoreUpdate(
                key = DataStoreConstants.BROWSE_FILES_STRUCTURE,
                value = event.value,
                updateState = {
                    it.copy(browseFilesStructure = toBrowseFilesStructure())
                }
            )

            is MainEvent.OnChangeBrowseLayout -> handleDatastoreUpdate(
                key = DataStoreConstants.BROWSE_LAYOUT,
                value = event.value,
                updateState = {
                    it.copy(browseLayout = toBrowseLayout())
                }
            )

            is MainEvent.OnChangeBrowseAutoGridSize -> handleDatastoreUpdate(
                key = DataStoreConstants.BROWSE_AUTO_GRID_SIZE,
                value = event.value,
                updateState = {
                    it.copy(browseAutoGridSize = this)
                }
            )

            is MainEvent.OnChangeBrowseGridSize -> handleDatastoreUpdate(
                key = DataStoreConstants.BROWSE_GRID_SIZE,
                value = event.value,
                updateState = {
                    it.copy(browseGridSize = this)
                }
            )

            is MainEvent.OnChangeBrowsePinFavoriteDirectories -> handleDatastoreUpdate(
                key = DataStoreConstants.BROWSE_PIN_FAVORITE_DIRECTORIES,
                value = event.value,
                updateState = {
                    it.copy(browsePinFavoriteDirectories = this)
                }
            )

            is MainEvent.OnChangeBrowseSortOrder -> handleDatastoreUpdate(
                key = DataStoreConstants.BROWSE_SORT_ORDER,
                value = event.value,
                updateState = {
                    it.copy(browseSortOrder = toBrowseSortOrder())
                }
            )

            is MainEvent.OnChangeBrowseSortOrderDescending -> handleDatastoreUpdate(
                key = DataStoreConstants.BROWSE_SORT_ORDER_DESCENDING,
                value = event.value,
                updateState = {
                    it.copy(browseSortOrderDescending = this)
                }
            )

            is MainEvent.OnChangeBrowseIncludedFilterItem -> handleBrowseIncludedFilterItemUpdate(
                event
            )

            is MainEvent.OnChangeTextAlignment -> handleDatastoreUpdate(
                key = DataStoreConstants.TEXT_ALIGNMENT,
                value = event.value,
                updateState = {
                    it.copy(textAlignment = toTextAlignment())
                }
            )

            is MainEvent.OnChangeDoublePressExit -> handleDatastoreUpdate(
                key = DataStoreConstants.DOUBLE_PRESS_EXIT,
                value = event.value,
                updateState = {
                    it.copy(doublePressExit = this)
                }
            )

            is MainEvent.OnChangeLetterSpacing -> handleDatastoreUpdate(
                key = DataStoreConstants.LETTER_SPACING,
                value = event.value,
                updateState = {
                    it.copy(letterSpacing = this)
                }
            )

            is MainEvent.OnChangeAbsoluteDark -> handleDatastoreUpdate(
                key = DataStoreConstants.ABSOLUTE_DARK,
                value = event.value,
                updateState = {
                    it.copy(absoluteDark = this)
                }
            )

            is MainEvent.OnChangeCutoutPadding -> handleDatastoreUpdate(
                key = DataStoreConstants.CUTOUT_PADDING,
                value = event.value,
                updateState = {
                    it.copy(cutoutPadding = this)
                }
            )

            is MainEvent.OnChangeFullscreen -> handleDatastoreUpdate(
                key = DataStoreConstants.FULLSCREEN,
                value = event.value,
                updateState = {
                    it.copy(fullscreen = this)
                }
            )

            is MainEvent.OnChangeKeepScreenOn -> handleDatastoreUpdate(
                key = DataStoreConstants.KEEP_SCREEN_ON,
                value = event.value,
                updateState = {
                    it.copy(keepScreenOn = this)
                }
            )

            is MainEvent.OnChangeVerticalPadding -> handleDatastoreUpdate(
                key = DataStoreConstants.VERTICAL_PADDING,
                value = event.value,
                updateState = {
                    it.copy(verticalPadding = this)
                }
            )

            is MainEvent.OnChangeHideBarsOnFastScroll -> handleDatastoreUpdate(
                key = DataStoreConstants.HIDE_BARS_ON_FAST_SCROLL,
                value = event.value,
                updateState = {
                    it.copy(hideBarsOnFastScroll = this)
                }
            )

            is MainEvent.OnChangePerceptionExpander -> handleDatastoreUpdate(
                key = DataStoreConstants.PERCEPTION_EXPANDER,
                value = event.value,
                updateState = {
                    it.copy(perceptionExpander = this)
                }
            )

            is MainEvent.OnChangePerceptionExpanderPadding -> handleDatastoreUpdate(
                key = DataStoreConstants.PERCEPTION_EXPANDER_PADDING,
                value = event.value,
                updateState = {
                    it.copy(perceptionExpanderPadding = this)
                }
            )

            is MainEvent.OnChangePerceptionExpanderThickness -> handleDatastoreUpdate(
                key = DataStoreConstants.PERCEPTION_EXPANDER_THICKNESS,
                value = event.value,
                updateState = {
                    it.copy(perceptionExpanderThickness = this)
                }
            )

            is MainEvent.OnChangeCheckForTextUpdate -> handleDatastoreUpdate(
                key = DataStoreConstants.CHECK_FOR_TEXT_UPDATE,
                value = event.value,
                updateState = {
                    it.copy(checkForTextUpdate = this)
                }
            )

            is MainEvent.OnChangeCheckForTextUpdateToast -> handleDatastoreUpdate(
                key = DataStoreConstants.CHECK_FOR_TEXT_UPDATE_TOAST,
                value = event.value,
                updateState = {
                    it.copy(checkForTextUpdateToast = this)
                }
            )

            is MainEvent.OnChangeScreenOrientation -> handleDatastoreUpdate(
                key = DataStoreConstants.SCREEN_ORIENTATION,
                value = event.value,
                updateState = {
                    it.copy(screenOrientation = toReaderScreenOrientation())
                }
            )

            is MainEvent.OnChangeCustomScreenBrightness -> handleDatastoreUpdate(
                key = DataStoreConstants.CUSTOM_SCREEN_BRIGHTNESS,
                value = event.value,
                updateState = {
                    it.copy(customScreenBrightness = this)
                }
            )

            is MainEvent.OnChangeScreenBrightness -> handleDatastoreUpdate(
                key = DataStoreConstants.SCREEN_BRIGHTNESS,
                value = event.value.toDouble(),
                updateState = {
                    it.copy(screenBrightness = this.toFloat())
                }
            )
        }
    }

    private fun init(event: MainEvent.OnInit) {
        viewModelScope.launch(Dispatchers.Main) {
            val settings = getAllSettings.execute()

            // All additional execution
            changeLanguage.execute(settings.language)

            if (settings.checkForUpdates) {
                viewModelScope.launch(Dispatchers.IO) {
                    checkForUpdates.execute(
                        postNotification = true
                    )
                }
            }
            /* - - - - - - - - - - - */

            updateStateWithSavedHandle { settings }
            isSettingsReady.update { true }
        }

        viewModelScope.launch(Dispatchers.Main) {
            combine(
                event.libraryViewModelReady,
                event.settingsViewModelReady
            ) { (libraryViewModelReady, settingsViewModelReady) ->
                libraryViewModelReady && settingsViewModelReady
            }.collectLatest { ready ->
                isViewModelReady.update {
                    ready
                }
            }
        }

        val isReady = combine(
            isViewModelReady,
            isSettingsReady
        ) { values ->
            values.all { it }
        }

        viewModelScope.launch(Dispatchers.Main) {
            isReady.first { bool ->
                if (bool) {
                    _isReady.update {
                        true
                    }
                }
                bool
            }
        }
    }

    private fun handleLanguageUpdate(event: MainEvent.OnChangeLanguage) {
        viewModelScope.launch(Dispatchers.Main.immediate) {
            changeLanguage.execute(event.value)
            updateStateWithSavedHandle {
                it.copy(language = event.value)
            }
        }
    }

    private fun handleBrowseIncludedFilterItemUpdate(
        event: MainEvent.OnChangeBrowseIncludedFilterItem
    ) {
        val set = _state.value.browseIncludedFilterItems.toMutableSet()
        if (!set.add(event.value)) {
            set.remove(event.value)
        }
        handleDatastoreUpdate(
            key = DataStoreConstants.BROWSE_INCLUDED_FILTER_ITEMS,
            value = set,
            updateState = {
                it.copy(browseIncludedFilterItems = toList())
            }
        )
    }

    /**
     * Handles and updates Datastore.
     */
    private fun <V> handleDatastoreUpdate(
        key: Preferences.Key<V>,
        value: V,
        updateState: V.(MainState) -> MainState
    ) {
        viewModelScope.launch(Dispatchers.Main.immediate) {
            setDatastore.execute(key = key, value = value)
            updateStateWithSavedHandle {
                value.updateState(it)
            }
        }
    }

    /**
     * Updates [MainState] along with [SavedStateHandle].
     */
    private suspend fun updateStateWithSavedHandle(
        function: (MainState) -> MainState
    ) {
        withContext(Dispatchers.Main.immediate) {
            _state.update {
                stateHandle[Constants.provideMainState()] = function(it)
                function(it)
            }
        }
    }
}