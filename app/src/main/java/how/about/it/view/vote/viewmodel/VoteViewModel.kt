package how.about.it.view.vote.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class VoteViewModel : ViewModel() {
    private val _openVote = MutableStateFlow(0)
    val openVote: StateFlow<Int> = _openVote

    fun setOpenVote() {
        _openVote.value = when (_openVote.value) {
            0 -> 1
            1 -> 0
            else -> throw IllegalAccessException()
        }
    }
}
