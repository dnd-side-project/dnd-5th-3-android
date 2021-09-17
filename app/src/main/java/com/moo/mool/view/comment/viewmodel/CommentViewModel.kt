package com.moo.mool.view.comment.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moo.mool.view.comment.model.Comment
import com.moo.mool.view.comment.model.Emoji
import com.moo.mool.view.comment.model.RequestPostEmoji
import com.moo.mool.view.comment.model.RequestPutEmoji
import com.moo.mool.view.comment.repository.CommentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentViewModel @Inject constructor(
    private val commentRepository: CommentRepository
) : ViewModel() {
    private val _openReact = MutableStateFlow(0)
    val openReact = _openReact.asStateFlow()

    private val _emptyEmoji = MutableStateFlow(0)
    val emptyEmoji = _emptyEmoji.asStateFlow()

    private val _replyList = MutableStateFlow<List<Comment>?>(null)
    val replyList = _replyList.asStateFlow()

    private val _isPosted = MutableStateFlow(false)
    val isPosted = _isPosted.asStateFlow()

    private val _isUpdated = MutableStateFlow(false)
    val isUpdated = _isUpdated.asStateFlow()

    private val _networkError = MutableStateFlow(false)
    val networkError = _networkError.asStateFlow()

    private val _emojiList = MutableStateFlow(
        listOf(
            Emoji(emojiId = 1, emojiCount = 0, checked = false, commentEmojiId = -1),
            Emoji(emojiId = 2, emojiCount = 0, checked = false, commentEmojiId = -1),
            Emoji(emojiId = 3, emojiCount = 0, checked = false, commentEmojiId = -1),
            Emoji(emojiId = 4, emojiCount = 0, checked = false, commentEmojiId = -1),
            Emoji(emojiId = 5, emojiCount = 0, checked = false, commentEmojiId = -1),
        )
    )
    val emojiList = _emojiList.asStateFlow()

    fun initOpenEmoji(value: Int) {
        _openReact.value = value
    }

    fun setOpenOrNotOpenReact() {
        _openReact.value = when (_openReact.value) {
            0 -> 1
            1 -> 0
            else -> throw IllegalAccessException()
        }
    }

    fun resetIsPosted() {
        _isPosted.value = false
    }

    fun requestGetReply(id: Int) {
        viewModelScope.launch {
            commentRepository.requestGetReply(id).collect { replyList ->
                replyList?.let {
                    _replyList.emit(replyList)
                    initEmojiList(replyList[0].emojiList)
                }
            }
        }
    }

    fun requestPostReply(id: Int, content: String) {
        viewModelScope.launch {
            commentRepository.requestPostReply(id, content).collect { result ->
                _isPosted.emit(result)
            }
        }
    }

    fun requestDeleteComment(id: Int) {
        viewModelScope.launch {
            commentRepository.requestDeleteComment(id).collect { result ->
                when (result) {
                    true -> {
                        _replyList.emit((requireNotNull(_replyList.value).map { comment ->
                            if (comment.commentId == id) {
                                comment.copy(deleted = true)
                            } else comment
                        }).filterIndexed { index, comment ->
                            (index == 0 || !comment.deleted)
                        })
                    }
                }
            }
        }
    }

    private fun initEmojiList(emojiList: List<Emoji>) {
        viewModelScope.launch {
            emojiList.forEach { responseEmoji ->
                _emojiList.emit(_emojiList.value.map { initEmoji ->
                    with(responseEmoji) {
                        if (initEmoji.emojiId == emojiId) {
                            initEmoji.copy(
                                emojiId = emojiId,
                                emojiCount = emojiCount,
                                checked = checked,
                                commentEmojiId = commentEmojiId
                            )
                        } else initEmoji
                    }
                })
            }
        }
    }

    fun requestEmoji(index: Int, id: Int) {
        when (_emojiList.value[index].commentEmojiId) {
            -1 -> requestPostEmoji(index, id)
            else -> requestPutEmoji(index)
        }
    }

    private fun requestPostEmoji(index: Int, id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                commentRepository.requestPostEmoji(
                    RequestPostEmoji(
                        emojiId = _emojiList.value[index].emojiId,
                        commentId = id
                    )
                )
            }.getOrNull()?.let {
                setCommentEmojiCount(index)
            } ?: _networkError.emit(true)
        }
    }

    private fun requestPutEmoji(index: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                commentRepository.requestPutEmoji(
                    RequestPutEmoji(
                        commentEmojiId = _emojiList.value[index].commentEmojiId,
                        isChecked = !_emojiList.value[index].checked
                    )
                )
            }.getOrNull()?.let {
                setCommentEmojiCount(index)
            } ?: _networkError.emit(true)
        }
    }

    private fun setCommentEmojiCount(selected: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _emojiList.emit(_emojiList.value.mapIndexed { index, emoji ->
                if (selected == index) {
                    emoji.copy(
                        emojiId = emoji.emojiId,
                        emojiCount = setEmojiCount(emoji),
                        checked = !emoji.checked,
                        commentEmojiId = emoji.commentEmojiId
                    )
                } else emoji
            })
        }
    }

    private fun setEmojiCount(emoji: Emoji) = when (emoji.checked) {
        true -> emoji.emojiCount - 1
        false -> emoji.emojiCount + 1
    }

    fun setEmptyCommentReactVisibility() {
        viewModelScope.launch(Dispatchers.IO) {
            var sum = 0
            _emojiList.value.forEach { emoji ->
                sum += emoji.emojiCount
            }
            _emptyEmoji.emit(sum)
        }
    }
}
