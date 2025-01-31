package raf.console.chitalka.presentation.core.components.navigation_rail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import raf.console.chitalka.domain.util.Route
import raf.console.chitalka.presentation.core.constants.Constants
import raf.console.chitalka.presentation.core.constants.provideNavigationItems
import raf.console.chitalka.presentation.core.navigation.LocalNavigatorInstance

/**
 * Navigation Rail. It is used to be shown on Tablets.
 */
@Composable
fun NavigationRail() {
    var currentScreen: Route? by remember { mutableStateOf(null) }
    val navigator = LocalNavigatorInstance.current
    val layoutDirection = LocalLayoutDirection.current

    LaunchedEffect(Unit) {
        navigator.currentScreen.collect { route ->
            if (
                Constants.provideNavigationItems().any {
                    navigator.run { it.screen.getRoute() } == route
                }
            ) {
                currentScreen = route
            }
        }
    }

    NavigationRail(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(
                start = WindowInsets.navigationBars
                    .asPaddingValues()
                    .calculateStartPadding(layoutDirection) +
                        WindowInsets.displayCutout
                            .asPaddingValues()
                            .calculateStartPadding(layoutDirection)
            )
            .width(80.dp)
            .padding(horizontal = 12.dp),
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        windowInsets = WindowInsets(0, 0, 0, 0)
    ) {
        Column(
            Modifier
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Constants.provideNavigationItems().forEach {
                NavigationRailItem(
                    item = it,
                    isSelected = currentScreen == navigator.run { it.screen.getRoute() }
                ) {
                    navigator.navigate(it.screen, false)
                }
            }
        }
    }
}