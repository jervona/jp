package ja.interview.jpmccodingchallenge.extensions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

fun ViewModel.launch(action: suspend () -> Unit) = this.viewModelScope.launch { action() }
