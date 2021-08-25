package how.about.it.view.feed.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import how.about.it.view.feed.Feed
import how.about.it.view.feed.repository.FeedRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val feedRepository: FeedRepository
) : ViewModel() {
    private val _toggleCategory = MutableStateFlow(0)
    val toggleCategory = _toggleCategory.asStateFlow()

    private val _feedTopList = MutableStateFlow<List<Feed>?>(null)
    val feedTopList = _feedTopList.asStateFlow()

    private val _feedBottomList = MutableStateFlow<List<Feed>?>(null)
    val feedBottomList = _feedBottomList.asStateFlow()

    private val _networkError = MutableStateFlow(false)
    val networkError = _networkError.asStateFlow()

    fun setToggleCategory(category: Int) {
        _toggleCategory.value = category
    }

    fun requestTopFeedList() {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                feedRepository.requestTopFeedList()
            }.getOrNull()?.let { feedList ->
                _feedTopList.emit(feedList.posts.filter { feed ->
                    (feed.id != -1)
                })
            } ?: _networkError.emit(true)
        }
    }

    fun requestBottomFeedList(sorted: String) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                feedRepository.requestBottomFeedList(sorted)
            }.getOrNull()?.let { feedList ->
                _feedBottomList.emit(feedList.posts)
            } ?: _networkError.emit(true)
        }
    }
}
