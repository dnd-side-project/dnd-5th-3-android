package com.moo.mool.view.commentupdate.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moo.mool.view.comment.repository.CommentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentUpdateViewModel @Inject constructor(
    private val commentRepository: CommentRepository
) : ViewModel() {
    private val _isUpdated = MutableStateFlow(false)
    val isUpdated = _isUpdated.asStateFlow()

    fun requestCommentUpdate(id: Int, content: String) {
        viewModelScope.launch {
            commentRepository.requestUpdateComment(id, content).collect { result ->
                _isUpdated.emit(result)
            }
        }
    }
}
