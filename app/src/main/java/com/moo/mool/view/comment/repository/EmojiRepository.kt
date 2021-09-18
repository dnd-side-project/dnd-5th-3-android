package com.moo.mool.view.comment.repository

import com.moo.mool.network.emoji.EmojiServiceImpl
import com.moo.mool.view.comment.model.RequestPostEmoji
import com.moo.mool.view.comment.model.RequestPutEmoji
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class EmojiRepository @Inject constructor(
    private val emojiServiceImpl: EmojiServiceImpl
) {
    suspend fun requestPostEmoji(emojiId: Int, commentId: Int) = flow {
        runCatching {
            emojiServiceImpl.requestPostEmoji(
                RequestPostEmoji(
                    emojiId = emojiId,
                    commentId = commentId
                )
            )
        }.getOrNull()?.let {
            emit(true)
        } ?: emit(false)
    }

    suspend fun requestPutEmoji(emojiId: Int, isChecked: Boolean) = flow {
        runCatching {
            emojiServiceImpl.requestPutEmoji(
                RequestPutEmoji(
                    commentEmojiId = emojiId,
                    isChecked = isChecked
                )
            )
        }.getOrNull()?.let {
            emit(true)
        } ?: emit(false)
    }
}
