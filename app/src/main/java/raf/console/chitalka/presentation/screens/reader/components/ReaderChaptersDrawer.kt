package raf.console.chitalka.presentation.screens.reader.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import raf.console.chitalka.R
import raf.console.chitalka.presentation.core.components.modal_drawer.ModalDrawer
import raf.console.chitalka.presentation.core.components.modal_drawer.ModalDrawerSelectableItem
import raf.console.chitalka.presentation.core.components.modal_drawer.ModalDrawerTitleItem
import raf.console.chitalka.presentation.core.util.calculateProgress
import raf.console.chitalka.presentation.screens.history.data.HistoryEvent
import raf.console.chitalka.presentation.screens.history.data.HistoryViewModel
import raf.console.chitalka.presentation.screens.library.data.LibraryEvent
import raf.console.chitalka.presentation.screens.library.data.LibraryViewModel
import raf.console.chitalka.presentation.screens.reader.data.ReaderEvent
import raf.console.chitalka.presentation.screens.reader.data.ReaderViewModel

/**
 * Reader Chapters Drawer.
 * Shows the list of all chapter, current chapter and lets user to go to specific chapter.
 */
@Composable
fun ReaderChaptersDrawer() {
    val state = ReaderViewModel.getState()
    val onEvent = ReaderViewModel.getEvent()
    val onLibraryEvent = LibraryViewModel.getEvent()
    val onHistoryEvent = HistoryViewModel.getEvent()

    ModalDrawer(
        show = state.value.showChaptersDrawer,
        startIndex = state.value.currentChapter?.index ?: 0,
        onDismissRequest = { onEvent(ReaderEvent.OnShowHideChaptersDrawer(false)) },
        header = {
            ModalDrawerTitleItem(
                title = stringResource(id = R.string.chapters)
            )
        }
    ) {
        items(state.value.book.chapters, key = { it.index }) { chapter ->
            val selected = rememberSaveable(state.value.currentChapter) {
                chapter.index == state.value.currentChapter?.index
            }

            ModalDrawerSelectableItem(
                selected = selected,
                onClick = {
                    onEvent(
                        ReaderEvent.OnScrollToChapter(
                            chapterStartIndex = chapter.startIndex,
                            refreshList = { book ->
                                onLibraryEvent(LibraryEvent.OnUpdateBook(book))
                                onHistoryEvent(HistoryEvent.OnUpdateBook(book))
                            }
                        )
                    )
                    onEvent(ReaderEvent.OnShowHideChaptersDrawer(false))
                }
            ) {
                Text(
                    text = chapter.title,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (selected) {
                    Spacer(modifier = Modifier.width(18.dp))
                    Text(
                        text = "${state.value.currentChapterProgress.calculateProgress(1)}%"
                    )
                }
            }
        }
    }
}