package raf.console.chitalka.presentation.screens.about.components.badges

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import raf.console.chitalka.R
import raf.console.chitalka.presentation.core.constants.Constants
import raf.console.chitalka.presentation.core.constants.provideAboutBadges
import raf.console.chitalka.presentation.core.util.showToast
import raf.console.chitalka.presentation.screens.about.data.AboutEvent
import raf.console.chitalka.presentation.screens.about.data.AboutViewModel

/**
 * About Badges.
 * Links all ways to contact me.
 *
 * @param verticalPadding Vertical item padding.
 */
@Composable
fun AboutBadges(verticalPadding: Dp = 18.dp) {
    val onEvent = AboutViewModel.getEvent()
    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        LazyRow(
            Modifier
                .padding(horizontal = 18.dp, vertical = verticalPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(13.dp)
        ) {
            items(Constants.provideAboutBadges(), key = { it.id }) { badge ->
                AboutBadgeItem(badge = badge) {
                    when (badge.id) {
                        "palestine" -> {
                            "Free Palestine!"
                                .showToast(context = context, longToast = false)
                            
                        }

                        else -> {
                            badge.url?.let {
                                onEvent(
                                    AboutEvent.OnNavigateToBrowserPage(
                                        it,
                                        context,
                                        noAppsFound = {
                                            context.getString(R.string.error_no_browser)
                                                .showToast(context = context, longToast = false)
                                        }
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}