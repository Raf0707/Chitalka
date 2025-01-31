package raf.console.chitalka.presentation.screens.reader.components.app_bar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import raf.console.chitalka.R
import raf.console.chitalka.domain.util.Direction
import raf.console.chitalka.presentation.core.components.common.IconButton
import raf.console.chitalka.presentation.core.util.calculateProgress
import raf.console.chitalka.presentation.core.util.noRippleClickable
import raf.console.chitalka.presentation.screens.history.data.HistoryEvent
import raf.console.chitalka.presentation.screens.history.data.HistoryViewModel
import raf.console.chitalka.presentation.screens.library.data.LibraryEvent
import raf.console.chitalka.presentation.screens.library.data.LibraryViewModel
import raf.console.chitalka.presentation.screens.reader.data.ReaderEvent
import raf.console.chitalka.presentation.screens.reader.data.ReaderViewModel
import raf.console.chitalka.presentation.ui.Colors
import raf.console.chitalka.presentation.ui.HorizontalExpandingTransition

/**
 * Reader bottom bar.
 * Has a slider to change progress.
 */
@Composable
fun ReaderBottomBar() {
    val state = ReaderViewModel.getState()
    val onEvent = ReaderViewModel.getEvent()
    val onLibraryEvent = LibraryViewModel.getEvent()
    val onHistoryEvent = HistoryViewModel.getEvent()

    val bookProgress by remember {
        derivedStateOf {
            "${state.value.book.progress.calculateProgress(2)}%"
        }
    }
    val chapterProgress by remember {
        derivedStateOf {
            if (state.value.currentChapter == null) return@derivedStateOf ""
            " (${state.value.currentChapterProgress.calculateProgress(2)}%)"
        }
    }
    val progress by remember {
        derivedStateOf {
            "$bookProgress$chapterProgress"
        }
    }

    val arrowDirection by remember {
        derivedStateOf {
            val checkpoint = state.value.checkpoint.first
            val index = state.value.listState.firstVisibleItemIndex

            when {
                checkpoint > index -> Direction.END
                checkpoint < index -> Direction.START
                else -> Direction.NEUTRAL
            }
        }
    }
    val checkpointProgress by remember {
        derivedStateOf {
            (state.value.checkpoint.first / state.value.text.lastIndex.toFloat()) * 0.987f
        }
    }

    Column(
        Modifier
            .fillMaxWidth()
            .background(Colors.readerSystemBarsColor)
            .noRippleClickable(onClick = {})
            .navigationBarsPadding()
            .padding(horizontal = 18.dp)
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = progress,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleLarge
        )
        Row(
            modifier = Modifier.padding(top = 3.dp, bottom = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalExpandingTransition(
                visible = arrowDirection == Direction.START,
                startDirection = true
            ) {
                IconButton(
                    icon = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = R.string.checkpoint_back_content_desc,
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.primary,
                    disableOnClick = false
                ) {
                    onEvent(
                        ReaderEvent.OnRestoreCheckpoint { book ->
                            onLibraryEvent(LibraryEvent.OnUpdateBook(book))
                            onHistoryEvent(HistoryEvent.OnUpdateBook(book))
                        }
                    )
                }
            }

            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.CenterStart
            ) {
                BottomBarSlider()

                if (arrowDirection != Direction.NEUTRAL) {
                    SliderIndicator(progress = checkpointProgress)
                }
            }

            HorizontalExpandingTransition(
                visible = arrowDirection == Direction.END,
                startDirection = false
            ) {
                IconButton(
                    icon = Icons.AutoMirrored.Default.ArrowForward,
                    contentDescription = R.string.checkpoint_forward_content_desc,
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.primary,
                    disableOnClick = false
                ) {
                    onEvent(
                        ReaderEvent.OnRestoreCheckpoint { book ->
                            onLibraryEvent(LibraryEvent.OnUpdateBook(book))
                            onHistoryEvent(HistoryEvent.OnUpdateBook(book))
                        }
                    )
                }
            }
        }
    }
}

/**
 * Bottom Bar Slider.
 * Has semi-transparent track color.
 */
@Composable
private fun BottomBarSlider() {
    val state = ReaderViewModel.getState()
    val onEvent = ReaderViewModel.getEvent()
    val onLibraryEvent = LibraryViewModel.getEvent()
    val onHistoryEvent = HistoryViewModel.getEvent()

    Slider(
        value = state.value.book.progress,
        enabled = !state.value.lockMenu,
        onValueChange = {
            if (state.value.listState.layoutInfo.totalItemsCount > 0) {
                onEvent(ReaderEvent.OnScroll(it))
                onEvent(
                    ReaderEvent.OnChangeProgress(
                        progress = it,
                        firstVisibleItemIndex = state.value.listState.firstVisibleItemIndex,
                        firstVisibleItemOffset = 0,
                        refreshList = { book ->
                            onLibraryEvent(LibraryEvent.OnUpdateBook(book))
                            onHistoryEvent(HistoryEvent.OnUpdateBook(book))
                        }
                    )
                )
            }
        },
        colors = SliderDefaults.colors(
            inactiveTrackColor = MaterialTheme.colorScheme.secondary.copy(0.15f),
            disabledActiveTrackColor = MaterialTheme.colorScheme.primary,
            disabledThumbColor = MaterialTheme.colorScheme.primary,
            disabledInactiveTrackColor = MaterialTheme.colorScheme.secondary.copy(0.15f),
        )
    )
}

/**
 * Slider Indicator.
 * Shows an indicator at desired progress.
 */
@Composable
private fun SliderIndicator(progress: Float) {
    Row(Modifier.fillMaxWidth()) {
        Spacer(
            modifier = Modifier.fillMaxWidth(progress)
        )
        Box(
            Modifier
                .width(4.dp)
                .height(16.dp)
                .clip(RoundedCornerShape(0.5.dp))
                .background(
                    MaterialTheme.colorScheme.onPrimary.copy(0.6f)
                )
        )
    }
}