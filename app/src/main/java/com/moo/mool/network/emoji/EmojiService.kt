package com.moo.mool.network.emoji

import com.moo.mool.view.comment.model.RequestPostEmoji
import com.moo.mool.view.comment.model.RequestPutEmoji
import com.moo.mool.view.comment.model.ResponseEmoji

interface EmojiService {
    suspend fun requestPostEmoji(body: RequestPostEmoji): ResponseEmoji

    suspend fun requestPutEmoji(body: RequestPutEmoji): ResponseEmoji
}
