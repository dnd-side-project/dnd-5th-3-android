package how.about.it.view.feed.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import how.about.it.view.feed.repository.FeedRepository

class FeedViewModelFactory(
    private val feedRepository: FeedRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FeedViewModel::class.java)) {
            return FeedViewModel(feedRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
