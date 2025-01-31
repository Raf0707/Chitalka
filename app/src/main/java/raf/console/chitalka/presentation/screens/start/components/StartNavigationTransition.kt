package raf.console.chitalka.presentation.screens.start.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import raf.console.chitalka.presentation.core.components.common.AnimatedVisibility
import raf.console.chitalka.presentation.screens.start.data.StartViewModel
import raf.console.chitalka.presentation.ui.Transitions

/**
 * Transition between [StartSettings] and [StartDone].
 */
@Composable
fun StartNavigationTransition(
    modifier: Modifier = Modifier,
    visible: Boolean,
    bottomBar: @Composable () -> Unit,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable ColumnScope.() -> Unit
) {
    val state = StartViewModel.getState()

    AnimatedVisibility(
        visible = visible,
        enter = if (state.value.useBackAnimation) Transitions.BackSlidingTransitionIn
        else Transitions.SlidingTransitionIn,
        exit = if (state.value.useBackAnimation) Transitions.BackSlidingTransitionOut
        else Transitions.SlidingTransitionOut
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = bottomBar,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .then(modifier),
                verticalArrangement = verticalArrangement,
                horizontalAlignment = horizontalAlignment
            ) {
                content()
            }
        }
    }
}