package raf.console.chitalka.presentation.screens.browse.data

import android.os.Environment
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Immutable
import raf.console.chitalka.domain.model.NullableBook
import raf.console.chitalka.domain.model.SelectableFile
import raf.console.chitalka.domain.util.Selected
import java.io.File

@Immutable
data class BrowseState(
    val selectableFiles: List<SelectableFile> = emptyList(),
    val listState: LazyListState = LazyListState(),
    val gridState: LazyGridState = LazyGridState(),

    val selectedDirectory: File = Environment.getExternalStorageDirectory(),
    val previousDirectory: File? = null,
    val inNestedDirectory: Boolean = false,
    val showFilterBottomSheet: Boolean = false,
    val currentPage: Int = 1,

    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val isError: Boolean = false,
    val requestPermissionDialog: Boolean = false,

    val selectedItemsCount: Int = 0,
    val hasSelectedItems: Boolean = false,

    val showSearch: Boolean = false,
    val searchQuery: String = "",
    val hasSearched: Boolean = false,
    val hasFocused: Boolean = false,

    val showAddingDialog: Boolean = false,
    val selectedBooks: List<Pair<NullableBook, Selected>> = emptyList(),
    val isBooksLoading: Boolean = false
)