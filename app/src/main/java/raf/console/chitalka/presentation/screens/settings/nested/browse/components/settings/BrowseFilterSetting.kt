@file:Suppress("FunctionName")

package raf.console.chitalka.presentation.screens.settings.nested.browse.components.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import raf.console.chitalka.presentation.core.constants.Constants
import raf.console.chitalka.presentation.core.constants.provideSupportedExtensions
import raf.console.chitalka.presentation.data.MainEvent
import raf.console.chitalka.presentation.data.MainViewModel

/**
 * Browse Filter setting.
 * Lets user choose which items to filter in Browse.
 */
fun LazyListScope.BrowseFilterSetting() {
    items(Constants.provideSupportedExtensions(), key = { it }) {
        val state = MainViewModel.getState()
        val onMainEvent = MainViewModel.getEvent()

        FilterItem(
            item = it,
            isSelected = state.value.browseIncludedFilterItems.any { item ->
                item == it
            }
        ) {
            onMainEvent(
                MainEvent.OnChangeBrowseIncludedFilterItem(it)
            )
        }
    }
}

/**
 * Filter Item.
 * Can be selected.
 *
 * @param item Item.
 * @param isSelected Whether item is selected.
 * @param onClick OnClick callback.
 */
@Composable
private fun FilterItem(
    item: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(horizontal = 14.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isSelected,
            onCheckedChange = { onClick() }
        )
        Spacer(modifier = Modifier.width(14.dp))

        Text(
            text = item,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}