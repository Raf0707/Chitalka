package raf.console.chitalka.presentation.screens.browse.components.layout

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import raf.console.chitalka.domain.model.SelectableFile
import raf.console.chitalka.presentation.screens.browse.components.layout.grid.BrowseGridItem
import raf.console.chitalka.presentation.screens.browse.components.layout.list.BrowseListItem
import raf.console.chitalka.presentation.screens.settings.nested.browse.data.BrowseLayout
import java.io.File

/**
 * Browse list element item. Can be selected.
 *
 * @param file [File].
 * @param hasSelectedFiles Whether parent list has selected items.
 * @param modifier Modifier.
 * @param onClick OnClick callback.
 * @param onLongClick OnLongClick callback.
 */
@Composable
fun BrowseItem(
    layout: BrowseLayout,
    file: SelectableFile,
    hasSelectedFiles: Boolean,
    modifier: Modifier,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onLongClick: () -> Unit
) {
    when (layout) {
        BrowseLayout.LIST -> {
            BrowseListItem(
                modifier = modifier,
                file = file,
                hasSelectedFiles = hasSelectedFiles,
                onClick = onClick,
                onFavoriteClick = onFavoriteClick,
                onLongClick = onLongClick
            )
        }

        BrowseLayout.GRID -> {
            BrowseGridItem(
                modifier = modifier,
                file = file,
                hasSelectedFiles = hasSelectedFiles,
                onClick = onClick,
                onFavoriteClick = onFavoriteClick,
                onLongClick = onLongClick
            )
        }
    }
}