package raf.console.chitalka.presentation.screens.library.components.dialog

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import raf.console.chitalka.R
import raf.console.chitalka.presentation.core.components.dialog.DialogWithContent
import raf.console.chitalka.presentation.core.util.showToast
import raf.console.chitalka.presentation.screens.browse.data.BrowseEvent
import raf.console.chitalka.presentation.screens.browse.data.BrowseViewModel
import raf.console.chitalka.presentation.screens.history.data.HistoryEvent
import raf.console.chitalka.presentation.screens.history.data.HistoryViewModel
import raf.console.chitalka.presentation.screens.library.data.LibraryEvent
import raf.console.chitalka.presentation.screens.library.data.LibraryViewModel

/**
 * Delete dialog. Deletes all selected books.
 */
@Composable
fun LibraryDeleteDialog() {
    val context = LocalContext.current
    val state = LibraryViewModel.getState()
    val onEvent = LibraryViewModel.getEvent()
    val onBrowseEvent = BrowseViewModel.getEvent()
    val onHistoryEvent = HistoryViewModel.getEvent()

    DialogWithContent(
        title = stringResource(id = R.string.delete_books),
        imageVectorIcon = Icons.Outlined.DeleteOutline,
        description = stringResource(
            id = R.string.delete_books_description,
            state.value.books.filter { it.second }.size
        ),
        actionText = stringResource(id = R.string.delete),
        onDismiss = { onEvent(LibraryEvent.OnShowHideDeleteDialog) },
        withDivider = false,
        isActionEnabled = true,
        onAction = {
            onEvent(
                LibraryEvent.OnDeleteBooks {
                    onBrowseEvent(BrowseEvent.OnLoadList)
                    onHistoryEvent(HistoryEvent.OnLoadList)
                }
            )
            context.getString(R.string.books_deleted)
                .showToast(context = context)
        }
    )
}