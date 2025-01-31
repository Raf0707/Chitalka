package raf.console.chitalka.presentation.screens.book_info.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import raf.console.chitalka.R
import raf.console.chitalka.presentation.core.util.calculateProgress
import raf.console.chitalka.presentation.screens.book_info.data.BookInfoViewModel

/**
 * Statistic section.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun BookInfoStatisticSection() {
    val state = BookInfoViewModel.getState()

    val progress by remember {
        derivedStateOf {
            "${state.value.book.progress.calculateProgress(1)}%"
        }
    }
    val description = stringResource(
        if (state.value.book.progress == 1f) R.string.read_done
        else if (state.value.book.progress > 0.2f) R.string.read_keep
        else R.string.read_more
    )

    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Text(
            text = progress,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(6.dp))

        LinearWavyProgressIndicator(
            progress = { state.value.book.progress.coerceIn(0f, 1f) },
            trackColor = MaterialTheme.colorScheme.secondaryContainer.copy(0.7f),
            modifier = Modifier.fillMaxWidth(),
            amplitude = { 0.5f },
            wavelength = 80.dp,
            waveSpeed = 15.dp
        )

        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = description,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}