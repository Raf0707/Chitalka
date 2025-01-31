package raf.console.chitalka.presentation.screens.settings.nested.appearance.components.settings

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import raf.console.chitalka.R
import raf.console.chitalka.presentation.core.components.common.AnimatedVisibility
import raf.console.chitalka.presentation.data.MainEvent
import raf.console.chitalka.presentation.data.MainViewModel
import raf.console.chitalka.presentation.screens.settings.components.SettingsCategoryTitle
import raf.console.chitalka.presentation.ui.Theme
import raf.console.chitalka.presentation.ui.ThemeContrast
import raf.console.chitalka.presentation.ui.animatedColorScheme
import raf.console.chitalka.presentation.ui.isDark
import raf.console.chitalka.presentation.ui.isPureDark

/**
 * Theme setting.
 * Lets user change app's theme from available in [Theme.entries].
 */
@Composable
fun ThemeSetting(
    verticalPadding: Dp = 8.dp,
    horizontalPadding: Dp = 18.dp
) {
    val state = MainViewModel.getState()
    val onMainEvent = MainViewModel.getEvent()

    Column(
        Modifier
            .fillMaxWidth()
            .padding(vertical = verticalPadding)
    ) {
        SettingsCategoryTitle(
            title = stringResource(id = R.string.app_theme_option),
            padding = horizontalPadding
        )
        Spacer(modifier = Modifier.height(10.dp))
        LazyRow(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = horizontalPadding)
        ) {
            items(
                Theme.entries(),
                key = { theme -> theme.name }
            ) { theme ->
                ThemeSettingItem(
                    theme = theme,
                    darkTheme = state.value.darkTheme.isDark(),
                    themeContrast = state.value.themeContrast,
                    isPureDark = state.value.pureDark.isPureDark(context = LocalContext.current),
                    selected = state.value.theme == theme
                ) {
                    onMainEvent(MainEvent.OnChangeTheme(theme.name))
                }
            }
        }
    }
}

/**
 * Theme Setting item.
 * Used to switch between different themes.
 *
 * @param theme Target [Theme].
 * @param darkTheme Whether should be shown in Dark Theme.
 * @param isPureDark Whether should be shown in Pure Dark.
 * @param themeContrast [ThemeContrast].
 * @param selected True if this theme is currently selected.
 * @param onClick OnClick callback.
 */
@Composable
private fun ThemeSettingItem(
    theme: Theme,
    darkTheme: Boolean,
    isPureDark: Boolean,
    themeContrast: ThemeContrast,
    selected: Boolean,
    onClick: () -> Unit
) {
    val colorScheme = animatedColorScheme(
        theme = theme,
        isDark = darkTheme,
        isPureDark = isPureDark,
        themeContrast = themeContrast
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.large)
                .clickable(enabled = !selected) {
                    onClick()
                }
                .background(
                    colorScheme.surface,
                    MaterialTheme.shapes.large
                )
                .border(
                    4.dp,
                    if (selected) colorScheme.primary
                    else MaterialTheme.colorScheme.outlineVariant,
                    MaterialTheme.shapes.large
                )
                .padding(4.dp)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier
                        .height(20.dp)
                        .width(80.dp)
                        .background(colorScheme.onSurface, RoundedCornerShape(10.dp))
                )
                Spacer(modifier = Modifier.width(8.dp))

                AnimatedVisibility(
                    visible = selected,
                    enter = scaleIn(tween(300), initialScale = 0.5f) +
                            fadeIn(tween(300)),
                    exit = fadeOut(tween(100))
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = stringResource(id = R.string.selected_content_desc),
                        modifier = Modifier
                            .size(26.dp),
                        tint = colorScheme.primary
                    )
                }

                Box(modifier = Modifier.height(26.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                Modifier
                    .padding(start = 8.dp)
                    .height(80.dp)
                    .width(70.dp)
                    .background(
                        colorScheme.surfaceContainer,
                        RoundedCornerShape(14.dp)
                    )
            )
            Spacer(modifier = Modifier.height(40.dp))
            Row(
                Modifier
                    .width(130.dp)
                    .height(40.dp)
                    .background(
                        colorScheme.surfaceContainer
                    ),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier
                        .size(20.dp)
                        .background(colorScheme.primary, CircleShape)
                )
                Box(
                    Modifier
                        .height(20.dp)
                        .width(60.dp)
                        .background(colorScheme.onSurfaceVariant, RoundedCornerShape(10.dp))
                )

            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = stringResource(id = theme.title),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelLarge,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2
        )
    }
}