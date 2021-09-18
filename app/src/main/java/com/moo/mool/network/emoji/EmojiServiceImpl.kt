package com.moo.mool.network.emoji

import com.moo.mool.view.comment.model.RequestPostEmoji
import com.moo.mool.view.comment.model.RequestPutEmoji
import javax.inject.Inject

class EmojiServiceImpl @Inject constructor(
    private val emojiInterface: EmojiInterface
) : EmojiService {
    override suspend fun requestPostEmoji(body: RequestPostEmoji) =
        emojiInterface.requestPostEmoji(body)

    override suspend fun requestPutEmoji(body: RequestPutEmoji) =
        emojiInterface.requestPutEmoji(body)
}
