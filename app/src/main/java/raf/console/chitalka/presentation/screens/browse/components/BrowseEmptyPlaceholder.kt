package raf.console.chitalka.presentation.screens.browse.components

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import raf.console.chitalka.R
import raf.console.chitalka.presentation.core.components.common.AnimatedVisibility
import raf.console.chitalka.presentation.core.components.placeholder.EmptyPlaceholder
import raf.console.chitalka.presentation.core.components.placeholder.ErrorPlaceholder
import raf.console.chitalka.presentation.core.navigation.LocalNavigator
import raf.console.chitalka.presentation.core.navigation.Screen
import raf.console.chitalka.presentation.screens.browse.data.BrowseEvent
import raf.console.chitalka.presentation.screens.browse.data.BrowseViewModel
import raf.console.chitalka.presentation.ui.Transitions

/**
 * Browse Empty Placeholder.
 * Shows error or empty message.
 *
 * @param isFilesEmpty Whether the list is empty.
 * @param storagePermissionState Storage [PermissionState].
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BoxScope.BrowseEmptyPlaceholder(
    isFilesEmpty: Boolean,
    storagePermissionState: PermissionState
) {
    val state = BrowseViewModel.getState()
    val onEvent = BrowseViewModel.getEvent()
    val onNavigate = LocalNavigator.current

    AnimatedVisibility(
        visible = state.value.isError,
        modifier = Modifier.align(Alignment.Center),
        enter = Transitions.DefaultTransitionIn,
        exit = Transitions.NoExitAnimation
    ) {
        ErrorPlaceholder(
            modifier = Modifier.align(Alignment.Center),
            errorMessage = stringResource(id = R.string.error_permission),
            icon = painterResource(id = R.drawable.error),
            actionTitle = stringResource(id = R.string.grant_permission)
        ) {
            onEvent(
                BrowseEvent.OnPermissionCheck(storagePermissionState)
            )
        }
    }

    AnimatedVisibility(
        visible = !state.value.isLoading
                && isFilesEmpty
                && !state.value.isError
                && !state.value.requestPermissionDialog
                && !state.value.isRefreshing,
        modifier = Modifier.align(Alignment.Center),
        enter = Transitions.DefaultTransitionIn,
        exit = Transitions.NoExitAnimation
    ) {
        EmptyPlaceholder(
            message = stringResource(id = R.string.browse_empty),
            icon = painterResource(id = R.drawable.empty_browse),
            actionTitle = stringResource(id = R.string.get_help),
            action = {
                onNavigate {
                    navigate(Screen.Help(false))
                }
            }
        )
    }
}