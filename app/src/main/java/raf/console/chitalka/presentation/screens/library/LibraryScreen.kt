@file:OptIn(ExperimentalMaterialApi::class)

package raf.console.chitalka.presentation.screens.library

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import raf.console.chitalka.R
import raf.console.chitalka.domain.model.Book
import raf.console.chitalka.domain.model.Category
import raf.console.chitalka.domain.util.Selected
import raf.console.chitalka.presentation.core.components.common.AnimatedVisibility
import raf.console.chitalka.presentation.core.components.common.header
import raf.console.chitalka.presentation.core.components.placeholder.EmptyPlaceholder
import raf.console.chitalka.presentation.core.navigation.LocalNavigator
import raf.console.chitalka.presentation.core.navigation.Screen
import raf.console.chitalka.presentation.core.util.showToast
import raf.console.chitalka.presentation.data.MainViewModel
import raf.console.chitalka.presentation.screens.library.components.LibraryBookItem
import raf.console.chitalka.presentation.screens.library.components.LibraryTopBar
import raf.console.chitalka.presentation.screens.library.components.dialog.LibraryDeleteDialog
import raf.console.chitalka.presentation.screens.library.components.dialog.LibraryMoveDialog
import raf.console.chitalka.presentation.screens.library.data.LibraryEvent
import raf.console.chitalka.presentation.screens.library.data.LibraryViewModel
import raf.console.chitalka.presentation.ui.DefaultTransition
import raf.console.chitalka.presentation.ui.Transitions

@Composable
fun LibraryScreenRoot() {
    val state = LibraryViewModel.getState()
    val onEvent = LibraryViewModel.getEvent()

    val pagerState = rememberPagerState(
        initialPage = state.value.currentPage,
        pageCount = { Category.entries.size }
    )
    val refreshState = rememberPullRefreshState(
        refreshing = state.value.isRefreshing,
        onRefresh = {
            onEvent(LibraryEvent.OnRefreshList)
        }
    )

    LaunchedEffect(Unit) {
        onEvent(LibraryEvent.OnScrollToPage(state.value.currentPage, pagerState))
    }
    LaunchedEffect(pagerState.currentPage) {
        if (pagerState.currentPage != state.value.currentPage) {
            onEvent(LibraryEvent.OnUpdateCurrentPage(pagerState.currentPage))
        }
    }

    LibraryScreen(
        pagerState = pagerState,
        refreshState = refreshState
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun LibraryScreen(
    pagerState: PagerState,
    refreshState: PullRefreshState
) {
    val state = LibraryViewModel.getState()
    val mainState = MainViewModel.getState()
    val onEvent = LibraryViewModel.getEvent()
    val onNavigate = LocalNavigator.current

    var isScrollInProgress by remember { mutableStateOf(false) }

    if (state.value.showMoveDialog) {
        LibraryMoveDialog(pagerState = pagerState)
    }
    if (state.value.showDeleteDialog) {
        LibraryDeleteDialog()
    }

    Scaffold(
        Modifier
            .fillMaxSize()
            .pullRefresh(refreshState),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            LibraryTopBar(pagerState = pagerState)
        }
    ) { paddingValues ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            HorizontalPager(
                state = pagerState,
                userScrollEnabled = !isScrollInProgress
            ) { index ->
                val category = remember { Category.entries[index] }
                val books = remember(state.value.categorizedBooks) {
                    state.value.categorizedBooks.find {
                        it.category == category
                    }?.books?.sortedWith(
                        compareByDescending<Pair<Book, Selected>> { it.first.lastOpened }
                            .thenBy { it.first.title }
                    ) ?: emptyList()
                }
                val listState = rememberLazyGridState()

                LaunchedEffect(listState.isScrollInProgress, pagerState.currentPage) {
                    if (listState.isScrollInProgress != isScrollInProgress
                        && pagerState.currentPage == state.value.currentPage
                    ) {
                        isScrollInProgress = listState.isScrollInProgress
                    }
                }

                Box(modifier = Modifier.fillMaxSize()) {
                    DefaultTransition(visible = !state.value.isLoading) {
                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(120.dp),
                            modifier = Modifier.fillMaxSize(),
                            state = listState,
                            contentPadding = PaddingValues(horizontal = 8.dp)
                        ) {
                            header {
                                Spacer(modifier = Modifier.height(8.dp))
                            }

                            items(
                                books,
                                key = { it.first.id }
                            ) {
                                LibraryBookItem(
                                    book = it,
                                    modifier = Modifier.animateItem(
                                        fadeInSpec = null,
                                        fadeOutSpec = null
                                    ),
                                    onCoverImageClick = {
                                        if (state.value.hasSelectedItems) {
                                            onEvent(LibraryEvent.OnSelectBook(it))
                                        } else {
                                            onNavigate {
                                                navigate(
                                                    Screen.BookInfo(
                                                        bookId = it.first.id,
                                                        startUpdate = false
                                                    )
                                                )
                                            }
                                        }
                                    },
                                    onLongClick = {
                                        if (!it.second) {
                                            onEvent(LibraryEvent.OnSelectBook(it, true))
                                        }
                                    },
                                    onButtonClick = {
                                        onEvent(
                                            LibraryEvent.OnNavigateToReaderScreen(
                                                onNavigate = onNavigate,
                                                book = it.first
                                            )
                                        )
                                    }
                                )
                            }

                            header {
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }

                    AnimatedVisibility(
                        visible = !state.value.isLoading
                                && !state.value.isRefreshing
                                && books.isEmpty(),
                        modifier = Modifier.align(Alignment.Center),
                        enter = Transitions.DefaultTransitionIn,
                        exit = fadeOut(tween(0))
                    ) {
                        EmptyPlaceholder(
                            message = stringResource(id = R.string.library_empty),
                            icon = painterResource(id = R.drawable.empty_library),
                            modifier = Modifier.align(Alignment.Center),
                            actionTitle = stringResource(id = R.string.add_book)
                        ) {
                            onNavigate {
                                navigate(Screen.Browse)
                            }
                        }
                    }
                }
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

    val activity = LocalContext.current as ComponentActivity
    val scope = rememberCoroutineScope()
    var shouldExit = rememberSaveable { false }
    BackHandler {
        if (state.value.hasSelectedItems) {
            onEvent(LibraryEvent.OnClearSelectedBooks)
            return@BackHandler
        }

        if (state.value.showSearch) {
            onEvent(LibraryEvent.OnSearchShowHide)
            return@BackHandler
        }

        if (state.value.currentPage > 0) {
            onEvent(LibraryEvent.OnScrollToPage(0, pagerState))
            return@BackHandler
        }

        if (shouldExit || !mainState.value.doublePressExit) {
            activity.finish()
            return@BackHandler
        }

        activity.getString(R.string.press_again_toast)
            .showToast(context = activity, longToast = false)
        shouldExit = true

        scope.launch {
            delay(1500)
            shouldExit = false
        }
    }
}