package raf.console.chitalka.presentation.screens.history.data

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Immutable
import androidx.compose.ui.focus.FocusRequester
import raf.console.chitalka.domain.model.Book
import raf.console.chitalka.domain.model.History
import raf.console.chitalka.domain.util.OnNavigate

@Immutable
sealed class HistoryEvent {
    data object OnRefreshList : HistoryEvent()
    data object OnLoadList : HistoryEvent()
    data object OnShowHideDeleteWholeHistoryDialog : HistoryEvent()
    data class OnDeleteWholeHistory(val refreshList: () -> Unit) : HistoryEvent()
    data class OnDeleteHistoryElement(
        val historyToDelete: History,
        val snackbarState: SnackbarHostState,
        val refreshList: () -> Unit,
        val context: Context
    ) : HistoryEvent()

    data class OnSearchQueryChange(val query: String) : HistoryEvent()
    data object OnSearch : HistoryEvent()
    data object OnSearchShowHide : HistoryEvent()
    data class OnRequestFocus(val focusRequester: FocusRequester) : HistoryEvent()
    data class OnUpdateBook(val book: Book) : HistoryEvent()
    data class OnNavigateToReaderScreen(
        val onNavigate: OnNavigate,
        val book: Book
    ) : HistoryEvent()

    data object OnUpdateScrollOffset : HistoryEvent()
}