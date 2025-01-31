package raf.console.chitalka.presentation.screens.browse.components.top_bar

import androidx.compose.animation.animateColorAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import raf.console.chitalka.R
import raf.console.chitalka.domain.model.SelectableFile
import raf.console.chitalka.presentation.core.components.common.IconButton
import raf.console.chitalka.presentation.core.components.common.SearchTextField
import raf.console.chitalka.presentation.core.components.top_bar.TopAppBar
import raf.console.chitalka.presentation.core.components.top_bar.TopAppBarData
import raf.console.chitalka.presentation.core.navigation.NavigationIconButton
import raf.console.chitalka.presentation.data.MainViewModel
import raf.console.chitalka.presentation.screens.browse.data.BrowseEvent
import raf.console.chitalka.presentation.screens.browse.data.BrowseViewModel
import raf.console.chitalka.presentation.screens.settings.nested.browse.data.BrowseFilesStructure
import raf.console.chitalka.presentation.screens.settings.nested.browse.data.BrowseLayout

/**
 * Browse Top Bar.
 *
 * @param filteredFiles Filtered files.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowseTopBar(filteredFiles: List<SelectableFile>) {
    val state = BrowseViewModel.getState()
    val mainState = MainViewModel.getState()
    val onEvent = BrowseViewModel.getEvent()

    val focusRequester = remember { FocusRequester() }
    val selectedDirectoryName = remember {
        mutableStateOf(state.value.selectedDirectory.name)
    }
    val inNestedDirectory = remember {
        derivedStateOf {
            state.value.inNestedDirectory
                    && mainState.value.browseFilesStructure != BrowseFilesStructure.ALL_FILES
        }
    }
    val isScrolled = remember {
        derivedStateOf {
            when (mainState.value.browseLayout) {
                BrowseLayout.LIST -> state.value.listState.canScrollBackward
                BrowseLayout.GRID -> state.value.gridState.canScrollBackward
            }
        }
    }

    LaunchedEffect(
        state.value.inNestedDirectory,
        state.value.selectedDirectory
    ) {
        if (state.value.inNestedDirectory) {
            selectedDirectoryName.value = state.value.selectedDirectory.name
        }
    }

    TopAppBar(
        scrollBehavior = null,
        isTopBarScrolled = isScrolled.value || state.value.hasSelectedItems,

        shownTopBar = when {
            state.value.hasSelectedItems -> 3
            state.value.showSearch -> 2
            state.value.inNestedDirectory -> 1
            else -> 0
        },
        topBars = listOf(
            TopAppBarData(
                contentID = 0,
                contentNavigationIcon = {},
                contentTitle = {
                    Text(
                        stringResource(id = R.string.browse_screen),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                contentActions = {
                    IconButton(
                        icon = Icons.Default.Search,
                        contentDescription = R.string.search_content_desc,
                        disableOnClick = true
                    ) {
                        onEvent(BrowseEvent.OnSearchShowHide)
                    }
                    IconButton(
                        icon = Icons.Default.FilterList,
                        contentDescription = R.string.filter_content_desc,
                        disableOnClick = false,
                        color = animateColorAsState(
                            if (mainState.value.browseIncludedFilterItems.isNotEmpty()) {
                                MaterialTheme.colorScheme.primary
                            } else LocalContentColor.current,
                            label = ""
                        ).value,
                        enabled = !state.value.showFilterBottomSheet
                    ) {
                        onEvent(BrowseEvent.OnShowHideFilterBottomSheet)
                    }
                    NavigationIconButton()
                }
            ),

            TopAppBarData(
                contentID = 1,
                contentNavigationIcon = {
                    IconButton(
                        icon = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = R.string.go_back_content_desc,
                        disableOnClick = false,
                        enabled = inNestedDirectory.value
                    ) {
                        onEvent(BrowseEvent.OnGoBackDirectory)
                    }
                },
                contentTitle = {
                    Text(
                        selectedDirectoryName.value,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                contentActions = {
                    IconButton(
                        icon = Icons.Default.Search,
                        contentDescription = R.string.search_content_desc,
                        disableOnClick = true
                    ) {
                        onEvent(BrowseEvent.OnSearchShowHide)
                    }
                    IconButton(
                        icon = Icons.Default.FilterList,
                        contentDescription = R.string.filter_content_desc,
                        disableOnClick = false,
                        color = animateColorAsState(
                            if (mainState.value.browseIncludedFilterItems.isNotEmpty()) {
                                MaterialTheme.colorScheme.primary
                            } else LocalContentColor.current,
                            label = ""
                        ).value,
                        enabled = !state.value.showFilterBottomSheet
                    ) {
                        onEvent(BrowseEvent.OnShowHideFilterBottomSheet)
                    }
                    NavigationIconButton()
                }
            ),

            TopAppBarData(
                contentID = 2,
                contentNavigationIcon = {
                    IconButton(
                        icon = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = R.string.exit_search_content_desc,
                        disableOnClick = true
                    ) {
                        onEvent(BrowseEvent.OnSearchShowHide)
                    }
                },
                contentTitle = {
                    SearchTextField(
                        modifier = Modifier
                            .focusRequester(focusRequester)
                            .onGloballyPositioned {
                                onEvent(BrowseEvent.OnRequestFocus(focusRequester))
                            },
                        query = state.value.searchQuery,
                        onQueryChange = {
                            onEvent(BrowseEvent.OnSearchQueryChange(it))
                        },
                        onSearch = {
                            onEvent(BrowseEvent.OnSearch)
                        }
                    )
                },
                contentActions = {
                    NavigationIconButton()
                }
            ),

            TopAppBarData(
                contentID = 3,
                contentNavigationIcon = {
                    IconButton(
                        icon = Icons.Default.Clear,
                        contentDescription = R.string.clear_selected_items_content_desc,
                        disableOnClick = true
                    ) {
                        onEvent(BrowseEvent.OnClearSelectedFiles)
                    }
                },
                contentTitle = {
                    Text(
                        stringResource(
                            id = R.string.selected_items_count_query,
                            state.value.selectedItemsCount.coerceAtLeast(1)
                        ),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                },
                contentActions = {
                    IconButton(
                        icon = Icons.Default.SelectAll,
                        contentDescription = R.string.select_all_files_content_desc,
                        disableOnClick = false,
                    ) {
                        onEvent(
                            BrowseEvent.OnSelectFiles(
                                includedFileFormats = mainState.value.browseIncludedFilterItems,
                                files = filteredFiles
                            )
                        )
                    }
                    IconButton(
                        icon = Icons.Default.Check,
                        contentDescription = R.string.add_files_content_desc,
                        disableOnClick = false,
                        enabled = !state.value.showAddingDialog
                    ) {
                        onEvent(BrowseEvent.OnAddingDialogRequest)
                    }
                }
            )
        ),
        customContent = {
            BrowseTopBarDirectoryPath()
        }
    )
}