package how.about.it.view.comment.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import how.about.it.view.comment.*
import how.about.it.view.comment.repository.CommentRepository
import how.about.it.view.commentupdate.RequestPutComment
import how.about.it.view.vote.RequestCommentId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CommentViewModel(private val commentRepository: CommentRepository) : ViewModel() {
    private val _openReact = MutableStateFlow(0)
    val openReact: StateFlow<Int> = _openReact

    private val _emptyReact = MutableStateFlow(0)
    val emptyReact: StateFlow<Int> = _emptyReact

    private val _reComment = MutableStateFlow<List<Comment>?>(null)
    val reComment = _reComment.asStateFlow()

    private val _isPosted = MutableStateFlow(false)
    val isPosted = _isPosted.asStateFlow()

    private val _isUpdated = MutableStateFlow(false)
    val isUpdated = _isUpdated.asStateFlow()

    private val _networkError = MutableStateFlow(false)
    val networkError: StateFlow<Boolean> = _networkError

    private val _commentEmoji =
        MutableStateFlow(
            listOf(
                Emoji(emojiId = 1, emojiCount = 0, checked = false),
                Emoji(emojiId = 2, emojiCount = 0, checked = false),
                Emoji(emojiId = 3, emojiCount = 0, checked = false),
                Emoji(emojiId = 4, emojiCount = 0, checked = false),
                Emoji(emojiId = 5, emojiCount = 0, checked = false),
            )
        )
    val commentEmoji: StateFlow<List<Emoji>> = _commentEmoji

    fun setOpenReact() {
        _openReact.value = when (_openReact.value) {
            0 -> 1
            1 -> 0
            else -> throw IllegalAccessException()
        }
    }

    fun requestCommentReply(id: Int) {
        viewModelScope.launch {
            val commentReply = runCatching { commentRepository.requestCommentReply(id) }.getOrNull()
            Log.d("commentReply", commentReply.toString())
            commentReply?.let {
                _reComment.emit(commentReply.commentList.filterIndexed { index, comment ->
                    (index == 0 || !comment.deleted)
                })
            } ?: _networkError.emit(true)
        }
    }

    fun requestPostReply(id: Int, content: String) {
        viewModelScope.launch {
            val postReply = runCatching {
                commentRepository.requestPostReComment(
                    id,
                    RequestPostReComment(content = content)
                )
            }.getOrNull()
            postReply?.let {
                _isPosted.emit(true)
            } ?: _networkError.emit(true)
        }
    }

    fun requestCommentUpdate(id: Int, content: String) {
        viewModelScope.launch {
            val requestUpdate = runCatching {
                commentRepository.requestCommentUpdate(
                    RequestPutComment(commentId = id, content = content)
                )
            }.getOrNull()
            requestUpdate?.let {
                _isUpdated.emit(true)
            } ?: _networkError.emit(true)
        }
    }

    fun requestDeleteComment(id: Int) {
        viewModelScope.launch {
            val requestDeleteComment = runCatching {
                commentRepository.requestCommentDelete(RequestCommentId(commentId = id))
            }.getOrNull()
            Log.d("delete", requestDeleteComment.toString())
            requestDeleteComment?.let {
                _reComment.emit((requireNotNull(_reComment.value).map { comment ->
                    if (comment.commentId == id) {
                        comment.copy(deleted = true)
                    } else comment
                }).filterIndexed { index, comment ->
                    (index == 0 || !comment.deleted)
                })
            } ?: _networkError.emit(true)
        }
    }

    fun initEmojiList(list: List<Emoji>) {
        viewModelScope.launch {
            list.forEach { responseEmoji ->
                _commentEmoji.emit(_commentEmoji.value.map { initEmoji ->
                    with(responseEmoji) {
                        if (initEmoji.emojiId == emojiId) {
                            initEmoji.copy(
                                emojiId = emojiId,
                                emojiCount = emojiCount,
                                checked = checked
                            )
                        } else initEmoji
                    }
                })
            }
        }
    }

    fun requestEmoji(index: Int, id: Int) {
        when (_commentEmoji.value[index].emojiCount) {
            0 -> requestPostEmoji(index + 1, id)
            else -> requestPutEmoji(index + 1)
        }
    }

    private fun requestPostEmoji(index: Int, id: Int) {
        viewModelScope.launch {
            val postEmoji = runCatching {
                commentRepository.requestPostEmoji(
                    RequestPostEmoji(
                        emojiId = index,
                        commentId = id
                    )
                )
            }.getOrNull()
            postEmoji?.let {
                setCommentEmojiCount(index)
            } ?: _networkError.emit(true)
        }
    }

    private fun requestPutEmoji(index: Int) {
        viewModelScope.launch {
            val putEmoji = runCatching {
                commentRepository.requestPutEmoji(
                    RequestPutEmoji(
                        commentEmojiId = index,
                        isChecked = _commentEmoji.value[index].checked
                    )
                )
            }.getOrNull()
            putEmoji?.let {
                setCommentEmojiCount(index)
            } ?: _networkError.emit(true)
        }
    }

    private fun setCommentEmojiCount(index: Int) {
        viewModelScope.launch {
            Log.d("emojiIndex", index.toString())
            _commentEmoji.emit(_commentEmoji.value.map { emoji ->
                with(emoji) {
                    if (emojiId == index) {
                        copy(
                            emojiId = emojiId,
                            emojiCount = setEmojiCount(this),
                            checked = !checked
                        )
                    } else this
                }
            })
        }
    }

    private fun setEmojiCount(emoji: Emoji) = when (emoji.checked) {
        true -> emoji.emojiCount - 1
        false -> emoji.emojiCount + 1
    }

    fun setFloatingCommentEmojiCount(selected: Int) {
        viewModelScope.launch {
            _commentEmoji.value = _commentEmoji.value.map { emoji ->
                with(emoji) {
                    if (emojiId == selected + 1 && !checked) {
                        copy(
                            emojiId = emojiId,
                            emojiCount = emojiCount + 1,
                            checked = true
                        )
                    } else this
                }
            }
        }
    }

    fun setEmptyCommentReactVisibility() {
        viewModelScope.launch {
            var sum = 0
            _commentEmoji.value.forEach { emoji ->
                sum += emoji.emojiCount
            }
            _emptyReact.emit(sum)
        }
    }
}
