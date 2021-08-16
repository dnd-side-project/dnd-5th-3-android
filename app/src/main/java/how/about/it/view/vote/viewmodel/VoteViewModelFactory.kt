package how.about.it.view.vote.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import how.about.it.view.vote.repository.VoteRepository

class VoteViewModelFactory(
    private val voteRepository: VoteRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VoteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VoteViewModel(voteRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
