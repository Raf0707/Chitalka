package raf.console.chitalka.presentation.screens.book_info

import androidx.activity.compose.BackHandler
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import raf.console.chitalka.R
import raf.console.chitalka.presentation.core.components.common.AnimatedVisibility
import raf.console.chitalka.presentation.core.components.common.Snackbar
import raf.console.chitalka.presentation.core.navigation.LocalNavigator
import raf.console.chitalka.presentation.core.navigation.Screen
import raf.console.chitalka.presentation.screens.book_info.components.BookInfoBackground
import raf.console.chitalka.presentation.screens.book_info.components.BookInfoDescriptionSection
import raf.console.chitalka.presentation.screens.book_info.components.BookInfoInfoSection
import raf.console.chitalka.presentation.screens.book_info.components.BookInfoMoreBottomSheet
import raf.console.chitalka.presentation.screens.book_info.components.BookInfoStatisticSection
import raf.console.chitalka.presentation.screens.book_info.components.BookInfoTopBar
import raf.console.chitalka.presentation.screens.book_info.components.change_cover_bottom_sheet.BookInfoChangeCoverBottomSheet
import raf.console.chitalka.presentation.screens.book_info.components.details_bottom_sheet.BookInfoDetailsBottomSheet
import raf.console.chitalka.presentation.screens.book_info.components.dialog.BookInfoConfirmUpdateDialog
import raf.console.chitalka.presentation.screens.book_info.components.dialog.BookInfoDeleteDialog
import raf.console.chitalka.presentation.screens.book_info.components.dialog.BookInfoMoveDialog
import raf.console.chitalka.presentation.screens.book_info.data.BookInfoEvent
import raf.console.chitalka.presentation.screens.book_info.data.BookInfoViewModel

@Composable
fun BookInfoScreenRoot(screen: Screen.BookInfo) {
    val onEvent = BookInfoViewModel.getEvent()
    val onNavigate = LocalNavigator.current
    val context = LocalContext.current

    val snackbarState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        onEvent(
            BookInfoEvent.OnInit(
                screen = screen,
                snackbarState = snackbarState,
                onNavigate = onNavigate,
                context = context
            )
        )
    }

    BookInfoScreen(
        snackbarState = snackbarState
    )

    DisposableEffect(Unit) {
        onDispose {
            onEvent(BookInfoEvent.OnClearViewModel)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun BookInfoScreen(snackbarState: SnackbarHostState) {
    val context = LocalContext.current
    val state = BookInfoViewModel.getState()
    val onEvent = BookInfoViewModel.getEvent()
    val onNavigate = LocalNavigator.current

    val listState = rememberLazyListState()
    val refreshState = rememberPullRefreshState(
        refreshing = state.value.updating || state.value.checkingForUpdate,
        onRefresh = {
            onEvent(
                BookInfoEvent.OnCheckForTextUpdate(
                    snackbarState,
                    context
                )
            )
        }
    )

    if (state.value.showChangeCoverBottomSheet) {
        BookInfoChangeCoverBottomSheet()
    }
    if (state.value.showDetailsBottomSheet) {
        BookInfoDetailsBottomSheet()
    }
    if (state.value.showDeleteDialog) {
        BookInfoDeleteDialog()
    }
    if (state.value.showMoveDialog) {
        BookInfoMoveDialog()
    }
    if (state.value.showConfirmTextUpdateDialog) {
        BookInfoConfirmUpdateDialog(snackbarHostState = snackbarState)
    }
    if (state.value.showMoreBottomSheet) {
        BookInfoMoreBottomSheet(snackbarState = snackbarState)
    }

    Scaffold(
        Modifier
            .fillMaxSize()
            .imePadding()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .pullRefresh(refreshState),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            BookInfoTopBar(
                listState = listState,
                snackbarState = snackbarState
            )
        },
        bottomBar = {
            Snackbar(
                modifier = Modifier.padding(bottom = 70.dp),
                snackbarState = snackbarState
            )
        }
    ) { paddingValues ->
        Box(
            Modifier
                .fillMaxSize()
        ) {
            LazyColumn(
                Modifier
                    .fillMaxSize(),
                state = listState
            ) {
                item {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        if (state.value.book.coverImage != null) {
                            BookInfoBackground(
                                height = paddingValues.calculateTopPadding() + 12.dp + 195.dp,
                                image = state.value.book.coverImage!!
                            )
                        }

                        Column(Modifier.fillMaxWidth()) {
                            Spacer(modifier = Modifier.height(paddingValues.calculateTopPadding() + 12.dp))
                            // Info
                            BookInfoInfoSection()
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    // Statistic
                    BookInfoStatisticSection()
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    // Description
                    BookInfoDescriptionSection()
                }
            }

            FloatingActionButton(
                onClick = {
                    if (!state.value.updating) {
                        onEvent(BookInfoEvent.OnNavigateToReaderScreen(onNavigate = onNavigate))
                    }
                },
                shape = MaterialTheme.shapes.large,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                elevation = FloatingActionButtonDefaults.elevation(),
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                content = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Spacer(modifier = Modifier.width(13.dp))
                        Icon(
                            imageVector = Icons.Filled.PlayArrow,
                            contentDescription = stringResource(id = R.string.continue_reading_content_desc),
                            Modifier.size(24.dp)
                        )
                        AnimatedVisibility(
                            visible = !listState.canScrollBackward,
                            enter = fadeIn() + expandHorizontally(),
                            exit = fadeOut() + shrinkHorizontally()
                        ) {
                            Text(
                                text = stringResource(
                                    if (state.value.book.progress == 0f) R.string.start
                                    else R.string.continue_read
                                ),
                                style = MaterialTheme.typography.labelLarge,
                                maxLines = 1,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                }
            )

            PullRefreshIndicator(
                state.value.updating || state.value.checkingForUpdate,
                refreshState,
                Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = paddingValues.calculateTopPadding()),
                backgroundColor = MaterialTheme.colorScheme.inverseSurface,
                contentColor = MaterialTheme.colorScheme.inverseOnSurface
            )
        }
    }

    BackHandler {
        var exitedEditMode = false

        if (state.value.editTitle) {
            onEvent(BookInfoEvent.OnShowHideEditTitle)
            exitedEditMode = true
        }

        if (state.value.editAuthor) {
            onEvent(BookInfoEvent.OnShowHideEditAuthor)
            exitedEditMode = true
        }

        if (state.value.editDescription) {
            onEvent(BookInfoEvent.OnShowHideEditDescription)
            exitedEditMode = true
        }

        if (exitedEditMode) {
            return@BackHandler
        }

        if (!state.value.updating) {
            onEvent(BookInfoEvent.OnCancelTextUpdate)
            onNavigate {
                navigateBack()
            }
        }
    }
}