package raf.console.chitalka.presentation.screens.start.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import raf.console.chitalka.R
import raf.console.chitalka.domain.model.ButtonItem
import raf.console.chitalka.presentation.core.components.dialog.SelectableDialogItem
import raf.console.chitalka.presentation.data.MainEvent
import raf.console.chitalka.presentation.screens.settings.components.SettingsCategoryTitle

/**
 * Language settings.
 */
fun LazyListScope.startLanguageScreen(
    onMainEvent: (MainEvent) -> Unit,
    languages: List<ButtonItem>
) {
    item {
        Spacer(modifier = Modifier.height(16.dp))
        SettingsCategoryTitle(
            title = stringResource(id = R.string.start_language_preferences),
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(12.dp))
    }

    items(languages, key = { it.id }) {
        SelectableDialogItem(
            selected = it.selected,
            title = it.title,
            horizontalPadding = 18.dp
        ) {
            onMainEvent(MainEvent.OnChangeLanguage(it.id))
        }
    }

    item {
        Spacer(modifier = Modifier.height(8.dp))
    }
}