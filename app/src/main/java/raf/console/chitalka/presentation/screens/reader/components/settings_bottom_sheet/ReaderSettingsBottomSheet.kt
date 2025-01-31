package raf.console.chitalka.presentation.screens.reader.components.settings_bottom_sheet

import androidx.activity.ComponentActivity
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import raf.console.chitalka.presentation.core.components.common.LazyColumnWithScrollbar
import raf.console.chitalka.presentation.core.components.modal_bottom_sheet.ModalBottomSheet
import raf.console.chitalka.presentation.data.MainViewModel
import raf.console.chitalka.presentation.screens.reader.data.ReaderEvent
import raf.console.chitalka.presentation.screens.reader.data.ReaderViewModel
import raf.console.chitalka.presentation.screens.settings.nested.appearance.components.subcategories.ColorsSubcategory
import raf.console.chitalka.presentation.screens.settings.nested.reader.components.subcategories.FontSubcategory
import raf.console.chitalka.presentation.screens.settings.nested.reader.components.subcategories.MiscSubcategory
import raf.console.chitalka.presentation.screens.settings.nested.reader.components.subcategories.PaddingSubcategory
import raf.console.chitalka.presentation.screens.settings.nested.reader.components.subcategories.ReadingSpeedSubcategory
import raf.console.chitalka.presentation.screens.settings.nested.reader.components.subcategories.SystemSubcategory
import raf.console.chitalka.presentation.screens.settings.nested.reader.components.subcategories.TextSubcategory
import raf.console.chitalka.presentation.screens.settings.nested.reader.components.subcategories.TranslatorSubcategory

/**
 * Settings bottom sheet. Has General and Colors categories.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderSettingsBottomSheet() {
    val state = ReaderViewModel.getState()
    val mainState = MainViewModel.getState()
    val onEvent = ReaderViewModel.getEvent()
    val context = LocalContext.current

    val pagerState = rememberPagerState(state.value.currentPage) { 3 }

    val animatedScrimColor by animateColorAsState(
        targetValue = if (pagerState.currentPage == 2) Color.Transparent
        else BottomSheetDefaults.ScrimColor,
        animationSpec = tween(300)
    )
    val animatedHeight by animateFloatAsState(
        targetValue = if (pagerState.currentPage == 2) 0.6f else 0.7f,
        animationSpec = tween(300)
    )

    LaunchedEffect(pagerState.currentPage) {
        onEvent(
            ReaderEvent.OnScrollToSettingsPage(
                page = pagerState.currentPage,
                pagerState = null
            )
        )
        onEvent(
            ReaderEvent.OnShowHideMenu(
                show = pagerState.currentPage != 2,
                fullscreenMode = mainState.value.fullscreen,
                saveCheckpoint = false,
                activity = context as ComponentActivity
            )
        )
    }

    ModalBottomSheet(
        hasFixedHeight = true,
        scrimColor = animatedScrimColor,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(animatedHeight),
        dragHandle = {},
        onDismissRequest = {
            onEvent(ReaderEvent.OnShowHideSettingsBottomSheet(false))
        },
        sheetGesturesEnabled = false
    ) {
        ReaderSettingsBottomSheetTabRow(pagerState = pagerState)

        HorizontalPager(state = pagerState) { page ->
            when (page) {
                0 -> {
                    LazyColumnWithScrollbar(Modifier.fillMaxSize()) {
                        PaddingSubcategory(
                            titleColor = { MaterialTheme.colorScheme.onSurface },
                            topPadding = 22.dp,
                            bottomPadding = 0.dp
                        )
                        SystemSubcategory(
                            titleColor = { MaterialTheme.colorScheme.onSurface },
                            topPadding = 22.dp,
                            bottomPadding = 0.dp
                        )
                        ReadingSpeedSubcategory(
                            titleColor = { MaterialTheme.colorScheme.onSurface },
                            topPadding = 22.dp,
                            bottomPadding = 0.dp
                        )
                        MiscSubcategory(
                            titleColor = { MaterialTheme.colorScheme.onSurface },
                            showDivider = false,
                            topPadding = 22.dp,
                            bottomPadding = 8.dp
                        )
                    }
                }

                1 -> {
                    LazyColumnWithScrollbar(Modifier.fillMaxSize()) {
                        FontSubcategory(
                            titleColor = { MaterialTheme.colorScheme.onSurface },
                            topPadding = 16.dp,
                            bottomPadding = 0.dp
                        )
                        TextSubcategory(
                            titleColor = { MaterialTheme.colorScheme.onSurface },
                            topPadding = 22.dp,
                            bottomPadding = 0.dp
                        )
                        TranslatorSubcategory(
                            titleColor = { MaterialTheme.colorScheme.onSurface },
                            showDivider = false,
                            topPadding = 22.dp,
                            bottomPadding = 8.dp
                        )
                    }
                }

                2 -> {
                    LazyColumnWithScrollbar(Modifier.fillMaxSize()) {
                        ColorsSubcategory(
                            showTitle = false,
                            showDivider = false,
                            backgroundColor = { MaterialTheme.colorScheme.surfaceContainer },
                            topPadding = 16.dp,
                            bottomPadding = 8.dp
                        )
                    }
                }
            }
        }
    }
}