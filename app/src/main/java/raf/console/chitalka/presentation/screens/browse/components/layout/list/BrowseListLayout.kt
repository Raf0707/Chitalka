package raf.console.chitalka.presentation.screens.browse.components.layout.list

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import raf.console.chitalka.domain.model.SelectableFile
import raf.console.chitalka.presentation.screens.browse.components.layout.BrowseItem
import raf.console.chitalka.presentation.screens.browse.data.BrowseViewModel
import raf.console.chitalka.presentation.screens.settings.nested.browse.data.BrowseLayout

@Composable
fun BrowseListLayout(
    filteredFiles: List<SelectableFile>,
    onLongItemClick: (SelectableFile) -> Unit,
    onFavoriteItemClick: (SelectableFile) -> Unit,
    onItemClick: (SelectableFile) -> Unit,
) {
    val state = BrowseViewModel.getState()

    LazyColumn(
        state = state.value.listState,
        modifier = Modifier
            .fillMaxSize()
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(
            filteredFiles,
            key = { it.fileOrDirectory.path }
        ) { selectableFile ->
            BrowseItem(
                file = selectableFile,
                layout = BrowseLayout.LIST,
                modifier = Modifier.animateItem(),
                hasSelectedFiles = state.value.selectableFiles.any { it.isSelected },
                onLongClick = {
                    onLongItemClick(selectableFile)
                },
                onFavoriteClick = {
                    onFavoriteItemClick(selectableFile)
                },
                onClick = {
                    onItemClick(selectableFile)
                }
            )
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}