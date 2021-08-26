package how.about.it.view.mypage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import how.about.it.view.feed.Feed
import how.about.it.view.mypage.repository.MyPageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val myPageRepository: MyPageRepository
) : ViewModel() {
    private val _category = MutableStateFlow(0)
    val category = _category.asStateFlow()

    private val _myPageFeedList = MutableStateFlow<List<Feed>?>(null)
    val myPageFeedList = _myPageFeedList.asStateFlow()

    private val _networkError = MutableStateFlow(false)
    val networkError = _networkError.asStateFlow()

    fun setCategory(category: Int) {
        _category.value = category
    }

    fun resetNetworkError() {
        _networkError.value = false
    }

    fun requestMyPageFeedList(sorted: String) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                myPageRepository.requestMyPageFeedList(sorted)
            }.getOrNull()?.let { feedList ->
                _myPageFeedList.emit(feedList.posts)
            } ?: _networkError.emit(true)
        }
    }
}
