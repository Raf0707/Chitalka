package raf.console.chitalka.presentation.screens.start.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import raf.console.chitalka.R
import raf.console.chitalka.presentation.core.navigation.LocalNavigator
import raf.console.chitalka.presentation.data.MainEvent
import raf.console.chitalka.presentation.data.MainViewModel
import raf.console.chitalka.presentation.screens.browse.data.BrowseEvent
import raf.console.chitalka.presentation.screens.browse.data.BrowseViewModel
import raf.console.chitalka.presentation.screens.start.data.StartEvent
import raf.console.chitalka.presentation.screens.start.data.StartViewModel

/**
 * Start Done screen.
 */
@Composable
fun StartDone() {
    val state = StartViewModel.getState()
    val onEvent = StartViewModel.getEvent()
    val onMainEvent = MainViewModel.getEvent()
    val onBrowseEvent = BrowseViewModel.getEvent()
    val onNavigate = LocalNavigator.current

    StartNavigationTransition(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(48.dp),
        visible = state.value.isDone,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        bottomBar = {
            Column {
                Spacer(modifier = Modifier.height(18.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .navigationBarsPadding()
                        .padding(bottom = 8.dp)
                        .padding(horizontal = 18.dp)
                        .fillMaxWidth()
                ) {
                    TextButton(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(100),
                        contentPadding = ButtonDefaults.ContentPadding,
                        onClick = {
                            onEvent(
                                StartEvent.OnGoToBrowse(
                                    onNavigate = onNavigate,
                                    onCompletedStartGuide = {
                                        onBrowseEvent(BrowseEvent.OnLoadList)
                                        onMainEvent(
                                            MainEvent.OnChangeShowStartScreen(false)
                                        )
                                    }
                                )
                            )
                        }
                    ) {
                        Text(text = stringResource(id = R.string.no))
                    }
                    Spacer(modifier = Modifier.width(18.dp))
                    Button(
                        modifier = Modifier.weight(3f),
                        shape = RoundedCornerShape(100),
                        onClick = {
                            onEvent(
                                StartEvent.OnGoToHelp(onNavigate = onNavigate)
                            )
                        }
                    ) {
                        Text(text = stringResource(id = R.string.yes_go_to_help))
                    }
                }


            }
        }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.start_done),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(190.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(id = R.string.start_done),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(id = R.string.start_done_desc),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}