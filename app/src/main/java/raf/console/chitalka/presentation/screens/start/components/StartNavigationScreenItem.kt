package raf.console.chitalka.presentation.screens.start.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import raf.console.chitalka.presentation.core.components.common.AnimatedVisibility
import raf.console.chitalka.presentation.core.components.common.LazyColumnWithScrollbar
import raf.console.chitalka.presentation.screens.start.data.StartNavigationScreen
import raf.console.chitalka.presentation.screens.start.data.StartViewModel
import raf.console.chitalka.presentation.ui.Transitions

/**
 * Navigation item screen. Uses sliding anim.
 */
@Composable
fun StartNavigationScreenItem(
    screen: StartNavigationScreen,
    content: LazyListScope.() -> Unit
) {
    val state = StartViewModel.getState()
    val listState = rememberLazyListState()

    AnimatedVisibility(
        visible = state.value.currentScreen == screen,
        enter = if (state.value.useBackAnimation) Transitions.BackSlidingTransitionIn
        else Transitions.SlidingTransitionIn,
        exit = if (state.value.useBackAnimation) Transitions.BackSlidingTransitionOut
        else Transitions.SlidingTransitionOut
    ) {
        LazyColumnWithScrollbar(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceContainerLow),
            state = listState
        ) {
            content()
        }
    }
}
