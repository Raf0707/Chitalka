package raf.console.chitalka.presentation.screens.book_info.data

import androidx.compose.runtime.Immutable
import raf.console.chitalka.domain.model.Book
import raf.console.chitalka.domain.model.Category
import raf.console.chitalka.domain.model.Chapter
import raf.console.chitalka.presentation.core.constants.Constants
import raf.console.chitalka.presentation.core.constants.provideEmptyBook

@Immutable
data class BookInfoState(
    val book: Book = Constants.provideEmptyBook(),

    val checkingForUpdate: Boolean = false,
    val updating: Boolean = false,

    val showConfirmTextUpdateDialog: Boolean = false,
    val updatedText: List<String>? = null,
    val updatedChapters: List<Chapter>? = null,

    val editTitle: Boolean = false,
    val hasTitleFocused: Boolean = false,
    val titleValue: String = "",

    val editAuthor: Boolean = false,
    val hasAuthorFocused: Boolean = false,
    val authorValue: String = "",

    val editDescription: Boolean = false,
    val hasDescriptionFocused: Boolean = false,
    val descriptionValue: String = "",

    val showChangeCoverBottomSheet: Boolean = false,
    val canResetCover: Boolean = false,

    val showDetailsBottomSheet: Boolean = false,

    val showDeleteDialog: Boolean = false,
    val showMoveDialog: Boolean = false,
    val selectedCategory: Category = Category.READING,

    val showMoreBottomSheet: Boolean = false,
)
