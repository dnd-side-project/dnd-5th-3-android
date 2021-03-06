package com.moo.mool.view.feed.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moo.mool.view.feed.model.Feed
import com.moo.mool.view.feed.repository.FeedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
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

    init {
        requestTopFeedList()
    }

    fun setToggleCategory(category: Int) {
        _toggleCategory.value = category
    }

    fun resetNetworkError() {
        _networkError.value = false
    }

    fun requestTopFeedList() {
        viewModelScope.launch {
            feedRepository.requestTopFeedList().collect { feedList ->
                feedList?.let {
                    _feedTopList.emit(feedList)
                }
            }
        }
    }

    fun requestBottomFeedList(sorted: String) {
        viewModelScope.launch {
            feedRepository.requestBottomFeedList(sorted).collect { feedList ->
                feedList?.let {
                    _feedBottomList.emit(feedList)
                }
            }
        }
    }
}
