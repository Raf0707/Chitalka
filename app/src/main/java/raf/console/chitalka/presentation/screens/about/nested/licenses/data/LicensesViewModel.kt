package raf.console.chitalka.presentation.screens.about.nested.licenses.data

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewModelScope
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.util.withJson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import raf.console.chitalka.R
import raf.console.chitalka.domain.util.UIViewModel
import javax.inject.Inject

@HiltViewModel
class LicensesViewModel @Inject constructor(

) : UIViewModel<LicensesState, LicensesEvent>() {

    companion object {
        @Composable
        fun getState() = getState<LicensesViewModel, LicensesState, LicensesEvent>()

        @Composable
        fun getEvent() = getEvent<LicensesViewModel, LicensesState, LicensesEvent>()
    }

    private val _state = MutableStateFlow(LicensesState())
    override val state = _state.asStateFlow()

    override fun onEvent(event: LicensesEvent) {
        viewModelScope.launch(Dispatchers.Main) {
            when (event) {
                is LicensesEvent.OnInit -> init(event)
            }
        }
    }

    private fun init(event: LicensesEvent.OnInit) {
        viewModelScope.launch(Dispatchers.IO) {
            if (_state.value.licenses.isNotEmpty()) {
                return@launch
            }

            val licenses = Libs
                .Builder()
                .withJson(event.context, R.raw.aboutlibraries)
                .build()
                .libraries
                .toList()

            _state.update {
                it.copy(
                    licenses = licenses.sortedBy { library -> library.openSource }
                )
            }
        }
    }
}