package raf.console.chitalka.presentation.screens.browse.components.adding_dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import raf.console.chitalka.R
import raf.console.chitalka.domain.model.NullableBook
import raf.console.chitalka.domain.util.Selected
import raf.console.chitalka.presentation.core.components.common.CircularCheckbox

/**
 * Adding dialog item.
 *
 * @param result Result of the file parsing, can be Null or Not Null.
 * @param onClick OnClick event(both error/not error).
 */
@Composable
fun BrowseAddingDialogItem(result: Pair<NullableBook, Selected>, onClick: (Boolean) -> Unit) {
    if (result.first is NullableBook.NotNull) {
        Row(
            Modifier
                .fillMaxWidth()
                .clickable {
                    onClick(true)
                }
                .padding(vertical = 12.dp, horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                Modifier.weight(1f)
            ) {
                Text(
                    text = result.first.bookWithTextAndCover!!.book.title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = result.first.bookWithTextAndCover!!.book.author.asString(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Row {
                Spacer(modifier = Modifier.width(24.dp))
                CircularCheckbox(
                    selected = result.second,
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                )
            }
        }
    } else {
        Row(
            Modifier
                .fillMaxWidth()
                .clickable {
                    onClick(false)
                }
                .padding(vertical = 12.dp, horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = stringResource(id = R.string.error_content_desc),
                modifier = Modifier.size(26.dp),
                tint = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = result.first.fileName!!,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}