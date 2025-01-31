package raf.console.chitalka.presentation.core.components.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import my.nanihadesuka.compose.InternalLazyColumnScrollbar
import my.nanihadesuka.compose.ScrollbarSettings
import raf.console.chitalka.presentation.core.constants.Constants
import raf.console.chitalka.presentation.core.constants.provideSecondaryScrollbar

/**
 * Lazy Column.
 * Has a scrollbar.
 *
 * @param modifier Modifier.
 * @param state [LazyListState].
 * @param scrollbarSettings [ScrollbarSettings]. [provideSecondaryScrollbar] by default.
 * @param enableScrollbar Whether scrollbar is enabled.
 * @param contentPadding [PaddingValues].
 * @param verticalArrangement Vertical item arrangement.
 * @param content Content of the column.
 */
@Composable
fun LazyColumnWithScrollbar(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    scrollbarSettings: ScrollbarSettings = Constants.provideSecondaryScrollbar(),
    enableScrollbar: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: LazyListScope.() -> Unit
) {
    val enabled = remember {
        derivedStateOf {
            enableScrollbar && (state.canScrollBackward || state.canScrollForward)
        }
    }

    Box(modifier = modifier) {
        LazyColumn(
            state = state,
            verticalArrangement = verticalArrangement,
            contentPadding = contentPadding
        ) {
            content()
        }
        if (enabled.value) {
            InternalLazyColumnScrollbar(
                state = state,
                settings = scrollbarSettings
            )
        }
    }
}