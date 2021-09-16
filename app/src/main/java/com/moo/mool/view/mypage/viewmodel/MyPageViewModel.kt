package com.moo.mool.view.mypage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moo.mool.view.feed.model.Feed
import com.moo.mool.view.mypage.repository.MyPageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
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
        viewModelScope.launch {
            myPageRepository.requestMyPageFeedList(sorted).collect { feedList ->
                feedList?.let {
                    _myPageFeedList.emit(feedList)
                }
            }
        }
    }
}
