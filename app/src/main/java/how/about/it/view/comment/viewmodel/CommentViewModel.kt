package how.about.it.view.comment.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import how.about.it.view.comment.Emoji
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CommentViewModel : ViewModel() {
    private val _openReact = MutableStateFlow(0)
    val openReact: StateFlow<Int> = _openReact

    private val _emptyReact = MutableStateFlow(false)
    val emptyReact: StateFlow<Boolean> = _emptyReact

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
