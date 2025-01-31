package raf.console.chitalka.presentation.screens.about.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Update
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import raf.console.chitalka.R
import raf.console.chitalka.presentation.core.components.dialog.DialogWithContent
import raf.console.chitalka.presentation.core.constants.Constants
import raf.console.chitalka.presentation.core.constants.provideReleasesPage
import raf.console.chitalka.presentation.core.util.showToast
import raf.console.chitalka.presentation.screens.about.data.AboutEvent
import raf.console.chitalka.presentation.screens.about.data.AboutViewModel

/**
 * Update dialog. If there is app update transfers to the download page.
 */
@Composable
fun AboutUpdateDialog() {
    val state = AboutViewModel.getState()
    val onEvent = AboutViewModel.getEvent()
    val context = LocalContext.current

    val update = remember(state.value.showUpdateDialog) {
        state.value.updateInfo!!
    }

    DialogWithContent(
        title = stringResource(id = R.string.update_query, update.tagName),
        imageVectorIcon = Icons.Default.Update,
        description = stringResource(
            id = R.string.update_app_description
        ),
        actionText = stringResource(id = R.string.proceed),
        isActionEnabled = true,
        disableOnClick = false,
        onDismiss = { onEvent(AboutEvent.OnDismissUpdateDialog) },
        onAction = {
            onEvent(
                AboutEvent.OnNavigateToBrowserPage(
                    page = Constants.provideReleasesPage(),
                    context = context,
                    noAppsFound = {
                        context.getString(R.string.error_no_browser).showToast(
                            context = context,
                            longToast = false
                        )
                    }
                )
            )
            onEvent(AboutEvent.OnDismissUpdateDialog)
        },
        withDivider = false,
    )
}