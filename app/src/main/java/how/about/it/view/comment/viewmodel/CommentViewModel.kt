package how.about.it.view.comment.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import how.about.it.view.comment.Comment
import how.about.it.view.comment.Emoji
import how.about.it.view.comment.RequestPostReComment
import how.about.it.view.comment.repository.CommentRepository
import how.about.it.view.commentupdate.RequestPutComment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CommentViewModel(private val commentRepository: CommentRepository) : ViewModel() {
    private val _openReact = MutableStateFlow(0)
    val openReact: StateFlow<Int> = _openReact

    private val _emptyReact = MutableStateFlow(false)
    val emptyReact: StateFlow<Boolean> = _emptyReact

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
                _reComment.emit(commentReply.commentList)
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

    fun initEmojiList() {
        //TODO EmojiList
        val list = listOf(
            Emoji(emojiId = 2, emojiCount = 0, checked = false),
            Emoji(emojiId = 4, emojiCount = 0, checked = false)
        )
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

    fun setCommentEmojiCount(index: Int) {
        viewModelScope.launch {
            _commentEmoji.emit(_commentEmoji.value.map { emoji ->
                with(emoji) {
                    if (emojiId == index + 1) {
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
            _commentEmoji.emit(_commentEmoji.value.map { emoji ->
                with(emoji) {
                    if (emojiId == selected + 1 && !checked) {
                        copy(
                            emojiId = emojiId,
                            emojiCount = emojiCount + 1,
                            checked = true
                        )
                    } else this
                }
            })
        }
    }

    fun setEmptyCommentReactVisibility() {
        viewModelScope.launch {
            _commentEmoji.value.forEach { emoji ->
                if (emoji.emojiCount != 0) {
                    _emptyReact.emit(false)
                    return@launch
                }
            }
            _emptyReact.emit(true)
        }
    }
}
