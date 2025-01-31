package raf.console.chitalka.presentation.screens.browse.components.filter_bottom_sheet

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import raf.console.chitalka.presentation.core.components.common.LazyColumnWithScrollbar
import raf.console.chitalka.presentation.core.components.modal_bottom_sheet.ModalBottomSheet
import raf.console.chitalka.presentation.screens.browse.data.BrowseEvent
import raf.console.chitalka.presentation.screens.browse.data.BrowseViewModel
import raf.console.chitalka.presentation.screens.settings.nested.browse.components.subcategories.BrowseFilterSubcategory
import raf.console.chitalka.presentation.screens.settings.nested.browse.components.subcategories.BrowseGeneralSubcategory
import raf.console.chitalka.presentation.screens.settings.nested.browse.components.subcategories.BrowseSortSubcategory

/**
 * Browse Filter Bottom Sheet.
 * Lets user sort, filter and change appearance of the Browse.
 */
@Composable
fun BrowseFilterBottomSheet() {
    val state = BrowseViewModel.getState()
    val onEvent = BrowseViewModel.getEvent()

    val pagerState = rememberPagerState(state.value.currentPage) { 3 }

    LaunchedEffect(pagerState.currentPage) {
        onEvent(
            BrowseEvent.OnScrollToFilterPage(
                page = pagerState.currentPage,
                pagerState = null
            )
        )
    }

    ModalBottomSheet(
        hasFixedHeight = true,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f),
        dragHandle = {},
        onDismissRequest = {
            onEvent(BrowseEvent.OnShowHideFilterBottomSheet)
        },
        sheetGesturesEnabled = false
    ) {
        BrowseFilterBottomSheetTabRow(
            onEvent = onEvent,
            pagerState = pagerState
        )

        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
            when (page) {
                0 -> {
                    LazyColumnWithScrollbar(modifier = Modifier.fillMaxSize()) {
                        BrowseGeneralSubcategory(
                            showTitle = false,
                            showDivider = false,
                            topPadding = 16.dp,
                            bottomPadding = 8.dp
                        )
                    }
                }

                1 -> {
                    LazyColumnWithScrollbar(modifier = Modifier.fillMaxSize()) {
                        BrowseFilterSubcategory(
                            showTitle = false,
                            showDivider = false,
                            topPadding = 16.dp,
                            bottomPadding = 8.dp
                        )
                    }
                }

                2 -> {
                    LazyColumnWithScrollbar(modifier = Modifier.fillMaxSize()) {
                        BrowseSortSubcategory(
                            showTitle = false,
                            showDivider = false,
                            topPadding = 16.dp,
                            bottomPadding = 8.dp
                        )
                    }
                }
            }
        }
    }
}