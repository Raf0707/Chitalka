package raf.console.chitalka.presentation.screens.about.nested.credits

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import raf.console.chitalka.R
import raf.console.chitalka.presentation.core.components.common.GoBackButton
import raf.console.chitalka.presentation.core.components.common.LazyColumnWithScrollbar
import raf.console.chitalka.presentation.core.components.top_bar.collapsibleTopAppBarScrollBehavior
import raf.console.chitalka.presentation.core.constants.Constants
import raf.console.chitalka.presentation.core.constants.provideCredits
import raf.console.chitalka.presentation.core.navigation.LocalNavigator
import raf.console.chitalka.presentation.core.util.showToast
import raf.console.chitalka.presentation.screens.about.data.AboutEvent
import raf.console.chitalka.presentation.screens.about.data.AboutViewModel
import raf.console.chitalka.presentation.screens.about.nested.credits.components.CreditItem

@Composable
fun CreditsScreenRoot() {
    CreditsScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreditsScreen() {
    val onEvent = AboutViewModel.getEvent()
    val onNavigate = LocalNavigator.current
    val context = LocalContext.current

    val (scrollBehavior, lazyListState) = TopAppBarDefaults.collapsibleTopAppBarScrollBehavior()

    Scaffold(
        Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .windowInsetsPadding(WindowInsets.navigationBars),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(stringResource(id = R.string.credits_option))
                },
                navigationIcon = {
                    GoBackButton(onNavigate = onNavigate)
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            )
        }
    ) { paddingValues ->
        LazyColumnWithScrollbar(
            Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding()),
            state = lazyListState
        ) {
            items(Constants.provideCredits(), key = { it.name }) {
                CreditItem(credit = it) {
                    it.website?.let { website ->
                        onEvent(
                            AboutEvent.OnNavigateToBrowserPage(
                                page = website,
                                context = context,
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







