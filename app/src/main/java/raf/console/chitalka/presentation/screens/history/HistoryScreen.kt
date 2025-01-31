package raf.console.chitalka.presentation.screens.history

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.DeleteSweep
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import raf.console.chitalka.R
import raf.console.chitalka.presentation.core.components.common.AnimatedVisibility
import raf.console.chitalka.presentation.core.components.common.IconButton
import raf.console.chitalka.presentation.core.components.common.SearchTextField
import raf.console.chitalka.presentation.core.components.common.Snackbar
import raf.console.chitalka.presentation.core.components.placeholder.EmptyPlaceholder
import raf.console.chitalka.presentation.core.components.top_bar.TopAppBar
import raf.console.chitalka.presentation.core.components.top_bar.TopAppBarData
import raf.console.chitalka.presentation.core.navigation.LocalNavigator
import raf.console.chitalka.presentation.core.navigation.NavigationIconButton
import raf.console.chitalka.presentation.core.navigation.Screen
import raf.console.chitalka.presentation.screens.history.components.HistoryDeleteWholeHistoryDialog
import raf.console.chitalka.presentation.screens.history.components.HistoryItem
import raf.console.chitalka.presentation.screens.history.data.HistoryEvent
import raf.console.chitalka.presentation.screens.history.data.HistoryViewModel
import raf.console.chitalka.presentation.screens.library.data.LibraryEvent
import raf.console.chitalka.presentation.screens.library.data.LibraryViewModel
import raf.console.chitalka.presentation.screens.settings.components.SettingsCategoryTitle
import raf.console.chitalka.presentation.ui.DefaultTransition
import raf.console.chitalka.presentation.ui.Transitions

@Composable
fun HistoryScreenRoot() {
    val onEvent = HistoryViewModel.getEvent()

    LaunchedEffect(Unit) {
        onEvent(HistoryEvent.OnUpdateScrollOffset)
    }

    HistoryScreen()
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterialApi::class
)
@Composable
private fun HistoryScreen() {
    val context = LocalContext.current
    val state = HistoryViewModel.getState()
    val onEvent = HistoryViewModel.getEvent()
    val onLibraryEvent = LibraryViewModel.getEvent()
    val onNavigate = LocalNavigator.current

    val refreshState = rememberPullRefreshState(
        refreshing = state.value.isRefreshing,
        onRefresh = {
            onEvent(HistoryEvent.OnRefreshList)
        }
    )
    val focusRequester = remember { FocusRequester() }
    val snackbarState = remember { SnackbarHostState() }

    if (state.value.showDeleteWholeHistoryDialog) {
        HistoryDeleteWholeHistoryDialog()
    }

    Scaffold(
        Modifier
            .fillMaxSize()
            .pullRefresh(refreshState),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            TopAppBar(
                scrollBehavior = null,
                isTopBarScrolled = state.value.listState.canScrollBackward,

                shownTopBar = when {
                    state.value.showSearch -> 1
                    else -> 0
                },
                topBars = listOf(
                    TopAppBarData(
                        contentID = 0,
                        contentNavigationIcon = {},
                        contentTitle = {
                            Text(stringResource(id = R.string.history_screen))
                        },
                        contentActions = {
                            IconButton(
                                icon = Icons.Default.Search,
                                contentDescription = R.string.search_content_desc,
                                disableOnClick = true,
                            ) {
                                onEvent(HistoryEvent.OnSearchShowHide)
                            }
                            IconButton(
                                icon = Icons.Outlined.DeleteSweep,
                                contentDescription = R.string.delete_whole_history_content_desc,
                                disableOnClick = false,
                                enabled = !state.value.isLoading
                                        && !state.value.isRefreshing
                                        && state.value.history.isNotEmpty()
                                        && !state.value.showDeleteWholeHistoryDialog
                            ) {
                                onEvent(HistoryEvent.OnShowHideDeleteWholeHistoryDialog)
                            }
                            NavigationIconButton()
                        }
                    ),

                    TopAppBarData(
                        contentID = 1,
                        contentNavigationIcon = {
                            IconButton(
                                icon = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = R.string.exit_search_content_desc,
                                disableOnClick = true
                            ) {
                                onEvent(HistoryEvent.OnSearchShowHide)
                            }
                        },
                        contentTitle = {
                            SearchTextField(
                                modifier = Modifier
                                    .focusRequester(focusRequester)
                                    .onGloballyPositioned {
                                        onEvent(HistoryEvent.OnRequestFocus(focusRequester))
                                    },
                                query = state.value.searchQuery,
                                onQueryChange = {
                                    onEvent(HistoryEvent.OnSearchQueryChange(it))
                                },
                                onSearch = {
                                    onEvent(HistoryEvent.OnSearch)
                                }
                            )
                        },
                        contentActions = {
                            NavigationIconButton()
                        },
                    )
                )
            )
        },
        bottomBar = {
            Snackbar(snackbarState = snackbarState)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            DefaultTransition(visible = !state.value.isLoading) {
                LazyColumn(
                    Modifier
                        .fillMaxSize(),
                    state = state.value.listState,
                ) {
                    item {
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    state.value.history.forEachIndexed { index, groupedHistory ->
                        item(key = groupedHistory.title) {
                            if (index > 0) {
                                Spacer(modifier = Modifier.height(8.dp))
                            }

                            SettingsCategoryTitle(
                                modifier = Modifier.animateItem(),
                                title = when (groupedHistory.title) {
                                    "today" -> stringResource(id = R.string.today)
                                    "yesterday" -> stringResource(id = R.string.yesterday)
                                    else -> groupedHistory.title
                                },
                                padding = 16.dp
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        items(
                            groupedHistory.history,
                            key = { it.id }
                        ) {
                            HistoryItem(
                                modifier = Modifier.animateItem(),
                                history = it,
                                isOnClickEnabled = !state.value.isRefreshing,
                                onBodyClick = {
                                    onNavigate {
                                        navigate(
                                            Screen.BookInfo(
                                                bookId = it.bookId,
                                                startUpdate = false
                                            )
                                        )
                                    }
                                },
                                onTitleClick = {
                                    onEvent(
                                        HistoryEvent.OnNavigateToReaderScreen(
                                            onNavigate = onNavigate,
                                            book = it.book!!
                                        )
                                    )
                                },
                                isDeleteEnabled = !state.value.isRefreshing,
                                onDeleteClick = {
                                    onEvent(
                                        HistoryEvent.OnDeleteHistoryElement(
                                            historyToDelete = it,
                                            snackbarState = snackbarState,
                                            context = context,
                                            refreshList = {
                                                onLibraryEvent(
                                                    LibraryEvent.OnLoadList
                                                )
                                            }
                                        )
                                    )
                                }
                            )
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }

            AnimatedVisibility(
                visible = !state.value.isLoading
                        && state.value.history.isEmpty()
                        && !state.value.isRefreshing,
                modifier = Modifier.align(Alignment.Center),
                enter = Transitions.DefaultTransitionIn,
                exit = fadeOut(tween(0))
            ) {
                EmptyPlaceholder(
                    modifier = Modifier.align(Alignment.Center),
                    message = stringResource(id = R.string.history_empty),
                    icon = painterResource(id = R.drawable.empty_history)
                )
            }

            PullRefreshIndicator(
                state.value.isRefreshing,
                refreshState,
                Modifier.align(Alignment.TopCenter),
                backgroundColor = MaterialTheme.colorScheme.inverseSurface,
                contentColor = MaterialTheme.colorScheme.inverseOnSurface
            )
        }
    }

    BackHandler {
        if (state.value.showSearch) {
            onEvent(HistoryEvent.OnSearchShowHide)
            return@BackHandler
        }

        onNavigate {
            navigate(Screen.Library)
        }
    }
}