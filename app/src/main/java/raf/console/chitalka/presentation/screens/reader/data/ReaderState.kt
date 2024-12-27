package raf.console.chitalka.presentation.screens.reader.data

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.AnnotatedString
import raf.console.chitalka.domain.model.Book
import raf.console.chitalka.domain.model.Chapter
import raf.console.chitalka.domain.util.UIText
import raf.console.chitalka.presentation.core.constants.Constants
import raf.console.chitalka.presentation.core.constants.provideEmptyBook

@Immutable
data class ReaderState(
    val book: Book = Constants.provideEmptyBook(),
    val text: List<AnnotatedString> = emptyList(),
    val listState: LazyListState = LazyListState(),

    val currentChapter: Chapter? = null,
    val currentChapterProgress: Float = 0f,
    val showChaptersDrawer: Boolean = false,

    val errorMessage: UIText? = null,
    val loading: Boolean = true,

    val showMenu: Boolean = false,
    val checkpoint: Pair<Int, Int> = 0 to 0,
    val lockMenu: Boolean = false,

    val checkingForUpdate: Boolean = false,
    val updateFound: Boolean = false,
    val showUpdateDialog: Boolean = false,

    val showSettingsBottomSheet: Boolean = false,
    val currentPage: Int = 0,
)