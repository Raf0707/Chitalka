package raf.console.chitalka.presentation.screens.book_info.components.change_cover_bottom_sheet

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HideImage
import androidx.compose.material.icons.filled.ImageSearch
import androidx.compose.material.icons.filled.Restore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import raf.console.chitalka.R
import raf.console.chitalka.presentation.core.components.modal_bottom_sheet.ModalBottomSheet
import raf.console.chitalka.presentation.core.util.showToast
import raf.console.chitalka.presentation.screens.book_info.data.BookInfoEvent
import raf.console.chitalka.presentation.screens.book_info.data.BookInfoViewModel
import raf.console.chitalka.presentation.screens.history.data.HistoryEvent
import raf.console.chitalka.presentation.screens.history.data.HistoryViewModel
import raf.console.chitalka.presentation.screens.library.data.LibraryEvent
import raf.console.chitalka.presentation.screens.library.data.LibraryViewModel

/**
 * Change cover bottom sheet. Lets user select photo from the gallery and replaces old one.
 */
@Composable
fun BookInfoChangeCoverBottomSheet() {
    val state = BookInfoViewModel.getState()
    val onEvent = BookInfoViewModel.getEvent()
    val onLibraryEvent = LibraryViewModel.getEvent()
    val onHistoryEvent = HistoryViewModel.getEvent()
    val context = LocalContext.current

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                onEvent(
                    BookInfoEvent.OnChangeCover(
                        uri,
                        context,
                        refreshList = {
                            onLibraryEvent(LibraryEvent.OnUpdateBook(it))
                            onHistoryEvent(HistoryEvent.OnUpdateBook(it))
                        }
                    )
                )
                context.getString(R.string.cover_image_changed)
                    .showToast(context = context)
            }
        }
    )

    LaunchedEffect(Unit) {
        onEvent(BookInfoEvent.OnCheckCoverReset)
    }

    ModalBottomSheet(
        modifier = Modifier.fillMaxWidth(),
        onDismissRequest = {
            onEvent(BookInfoEvent.OnShowHideChangeCoverBottomSheet)
        },
        sheetGesturesEnabled = true
    ) {
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            if (state.value.canResetCover) {
                item {
                    BookInfoChangeCoverBottomSheetItem(
                        icon = Icons.Default.Restore,
                        text = stringResource(id = R.string.reset_cover),
                        description = stringResource(id = R.string.reset_cover_desc)
                    ) {
                        onEvent(
                            BookInfoEvent.OnResetCoverImage(
                                refreshList = {
                                    onLibraryEvent(LibraryEvent.OnUpdateBook(it))
                                    onHistoryEvent(HistoryEvent.OnUpdateBook(it))
                                },
                                showResult = {
                                    it.asString(context).showToast(context = context)
                                }
                            )
                        )
                    }
                }
            }

            item {
                BookInfoChangeCoverBottomSheetItem(
                    icon = Icons.Default.ImageSearch,
                    text = stringResource(id = R.string.change_cover),
                    description = stringResource(id = R.string.change_cover_desc)
                ) {
                    photoPicker.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
            }

            if (state.value.book.coverImage != null) {
                item {
                    BookInfoChangeCoverBottomSheetItem(
                        icon = Icons.Default.HideImage,
                        text = stringResource(id = R.string.delete_cover),
                        description = stringResource(id = R.string.delete_cover_desc)
                    ) {
                        onEvent(
                            BookInfoEvent.OnDeleteCover(
                                refreshList = {
                                    onLibraryEvent(LibraryEvent.OnUpdateBook(it))
                                    onHistoryEvent(HistoryEvent.OnUpdateBook(it))
                                }
                            )
                        )
                        context.getString(R.string.cover_image_deleted)
                            .showToast(context = context)
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}