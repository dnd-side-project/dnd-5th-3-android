package com.moo.mool.view.vote.repository

import com.moo.mool.network.vote.VoteServiceImpl
import com.moo.mool.view.vote.model.RequestPostComment
import com.moo.mool.view.vote.model.RequestVote
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class VoteRepository @Inject constructor(
    private val voteServiceImpl: VoteServiceImpl
) {
    suspend fun requestVoteFeedDetail(id: Int) = flow {
        runCatching {
            voteServiceImpl.requestVoteFeedDetail(id)
        }.getOrNull()?.let { feedDetail ->
            emit(feedDetail)
        } ?: emit(null)
    }.flowOn(Dispatchers.IO)

    suspend fun requestVoteFeedComment(id: Int) = flow {
        runCatching {
            voteServiceImpl.requestVoteFeedComment(id)
        }.getOrNull()?.let { feedDetailComments ->
            emit(feedDetailComments.commentList.filterNot { comment ->
                (comment.deleted && comment.replyCount == 0)
            })
        } ?: emit(null)
    }

    suspend fun requestVoteFeedDelete(id: Int) = flow {
        runCatching {
            voteServiceImpl.requestVoteFeedDelete(id)
        }.getOrNull()?.let {
            emit(true)
        } ?: emit(false)
    }

    suspend fun requestVote(id: Int, vote: String) = flow {
        runCatching {
            voteServiceImpl.requestVote(id, RequestVote(vote))
        }.getOrNull()?.let {
            emit(true)
        } ?: emit(false)
    }

    suspend fun requestVotePostComment(id: Int, content: String) = flow {
        runCatching {
            voteServiceImpl.requestVotePostComment(
                RequestPostComment(
                    postId = id,
                    content = content
                )
            )
        }.getOrNull()?.let {
            emit(true)
        } ?: emit(false)
    }
}
