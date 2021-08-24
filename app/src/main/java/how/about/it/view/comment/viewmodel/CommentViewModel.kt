package how.about.it.view.comment.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import how.about.it.view.comment.*
import how.about.it.view.comment.repository.CommentRepository
import how.about.it.view.commentupdate.RequestPutComment
import how.about.it.view.vote.RequestCommentId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CommentViewModel(private val commentRepository: CommentRepository) : ViewModel() {
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

    private val _emojiList =
        MutableStateFlow(
            listOf(
                Emoji(emojiId = 1, emojiCount = 0, checked = false, commentEmojiId = -1),
                Emoji(emojiId = 2, emojiCount = 0, checked = false, commentEmojiId = -1),
                Emoji(emojiId = 3, emojiCount = 0, checked = false, commentEmojiId = -1),
                Emoji(emojiId = 4, emojiCount = 0, checked = false, commentEmojiId = -1),
                Emoji(emojiId = 5, emojiCount = 0, checked = false, commentEmojiId = -1),
            )
        )
    val emojiList = _emojiList.asStateFlow()

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

    fun requestGetComments(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                commentRepository.requestCommentReply(id)
            }.getOrNull()?.let { commentReply ->
                _replyList.emit(commentReply.commentList.filterIndexed { index, comment ->
                    (index == 0 || !comment.deleted)
                })
            } ?: _networkError.emit(true)
        }
    }

    fun initEmojiList(emojiList: List<Emoji>) {
        viewModelScope.launch(Dispatchers.IO) {
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

    fun requestPostReply(id: Int, content: String) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                commentRepository.requestPostReComment(
                    id,
                    RequestPostReComment(content = content)
                )
            }.getOrNull()?.let {
                _isPosted.emit(true)
            } ?: _networkError.emit(true)
        }
    }

    fun requestCommentUpdate(id: Int, content: String) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                commentRepository.requestCommentUpdate(
                    RequestPutComment(commentId = id, content = content)
                )
            }.getOrNull()?.let {
                _isUpdated.emit(true)
            } ?: _networkError.emit(true)
        }
    }

    fun requestDeleteComment(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                commentRepository.requestCommentDelete(RequestCommentId(commentId = id))
            }.getOrNull()?.let {
                _replyList.emit((requireNotNull(_replyList.value).map { comment ->
                    if (comment.commentId == id) {
                        comment.copy(deleted = true)
                    } else comment
                }).filterIndexed { index, comment ->
                    (index == 0 || !comment.deleted)
                })
            } ?: _networkError.emit(true)
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
