package raf.console.chitalka.presentation.screens.about.nested.license_info

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import raf.console.chitalka.R
import raf.console.chitalka.presentation.core.components.common.GoBackButton
import raf.console.chitalka.presentation.core.components.common.IconButton
import raf.console.chitalka.presentation.core.components.common.LazyColumnWithScrollbar
import raf.console.chitalka.presentation.core.components.top_bar.collapsibleTopAppBarScrollBehavior
import raf.console.chitalka.presentation.core.navigation.LocalNavigator
import raf.console.chitalka.presentation.core.navigation.Screen
import raf.console.chitalka.presentation.core.util.showToast
import raf.console.chitalka.presentation.screens.about.nested.license_info.data.LicenseInfoEvent
import raf.console.chitalka.presentation.screens.about.nested.license_info.data.LicenseInfoViewModel
import raf.console.chitalka.presentation.ui.DefaultTransition
import raf.console.chitalka.presentation.ui.SlidingTransition

@Composable
fun LicenseInfoScreenRoot(screen: Screen.About.LicenseInfo) {
    val onEvent = LicenseInfoViewModel.getEvent()
    val onNavigate = LocalNavigator.current
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        onEvent(
            LicenseInfoEvent.OnInit(
                screen = screen,
                navigateBack = {
                    onNavigate {
                        navigateBack()
                    }
                },
                context = context
            )
        )
    }

    LicenseInfoScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LicenseInfoScreen() {
    val state = LicenseInfoViewModel.getState()
    val onEvent = LicenseInfoViewModel.getEvent()
    val context = LocalContext.current
    val onNavigate = LocalNavigator.current

    val (scrollBehavior, lazyListState) = TopAppBarDefaults.collapsibleTopAppBarScrollBehavior()
    val licenses = remember(state.value.license?.licenses) {
        state.value.license?.licenses?.toList()
    }

    Scaffold(
        Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .windowInsetsPadding(WindowInsets.navigationBars),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            LargeTopAppBar(
                title = {
                    DefaultTransition(
                        visible = state.value.license?.name != null
                    ) {
                        Text(
                            state.value.license?.name ?: "",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                },
                navigationIcon = {
                    GoBackButton(onNavigate = onNavigate)
                },
                actions = {
                    if (state.value.license?.website?.isNotBlank() == true) {
                        IconButton(
                            icon = Icons.Outlined.Language,
                            contentDescription = R.string.open_in_web_content_desc,
                            disableOnClick = false
                        ) {
                            var url = state.value.license?.website!!
                            if (url.startsWith("http://") || !url.startsWith("https://")) {
                                url = "https://$url"
                            }

                            onEvent(
                                LicenseInfoEvent.OnOpenLicensePage(
                                    page = url,
                                    context = context,
                                    noAppsFound = {
                                        context.getString(R.string.error_no_browser).showToast(
                                            context = context,
                                            longToast = false
                                        )
                                    }
                                )
                            )
                        }
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            )
        }
    ) { paddingValues ->
        DefaultTransition(
            visible = state.value.license != null
        ) {
            LazyColumnWithScrollbar(
                Modifier
                    .fillMaxSize()
                    .padding(top = paddingValues.calculateTopPadding()),
                state = lazyListState,
                contentPadding = PaddingValues(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                }

                items(
                    licenses ?: emptyList(),
                    key = { it.name }
                ) { license ->
                    val showed = remember {
                        mutableStateOf(licenses?.size == 1)
                    }

                    Column(
                        modifier = Modifier.animateItem(
                            fadeInSpec = null,
                            fadeOutSpec = null
                        )
                    ) {
                        Text(
                            text = license.name,
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.titleSmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .clip(RoundedCornerShape(100))
                                .clickable(enabled = license.licenseContent?.isNotBlank() == true) {
                                    showed.value = !showed.value
                                }
                                .background(
                                    MaterialTheme.colorScheme.primary,
                                    RoundedCornerShape(100)
                                )
                                .padding(vertical = 6.dp, horizontal = 12.dp)
                        )

                        SlidingTransition(
                            visible = showed.value && license.licenseContent?.isNotBlank() == true,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            Column {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = license.licenseContent!!
                                        .lines()
                                        .joinToString(separator = "\n") {
                                            it.trim()
                                        },
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}