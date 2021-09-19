package com.moo.mool.view.vote.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moo.mool.view.comment.model.Comment
import com.moo.mool.view.comment.repository.CommentRepository
import com.moo.mool.view.comment.repository.EmojiRepository
import com.moo.mool.view.vote.model.ResponseFeedDetail
import com.moo.mool.view.vote.repository.VoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VoteViewModel @Inject constructor(
    private val voteRepository: VoteRepository,
    private val commentRepository: CommentRepository,
    private val emojiRepository: EmojiRepository
) : ViewModel() {
    private val _openVote = MutableStateFlow("CLOSE")
    val openVote = _openVote.asStateFlow()

    private val _feedDetail = MutableStateFlow<ResponseFeedDetail?>(null)
    val feedDetail = _feedDetail.asStateFlow()

    private val _feedDetailComments = MutableStateFlow<List<Comment>?>(null)
    val feedDetailComments = _feedDetailComments.asStateFlow()

    private val _requestVote = MutableStateFlow("")
    val requestVote = _requestVote.asStateFlow()

    private val _requestDelete = MutableStateFlow(false)
    val requestDelete = _requestDelete.asStateFlow()

    private val _requestPostComment = MutableStateFlow(false)
    val requestPostComment = _requestPostComment.asStateFlow()

    private val _networkError = MutableStateFlow(false)
    val networkError = _networkError.asStateFlow()

    fun setOpenVote() {
        _openVote.value = when (_openVote.value) {
            "CLOSE" -> "OPEN"
            "OPEN" -> "CLOSE"
            else -> throw IllegalAccessException()
        }
    }

    fun openVote() {
        _feedDetail.value?.let {
            if (requireNotNull(_feedDetail.value).currentMemberVoteResult == "NO_RESULT") {
                _openVote.value = "OPEN"
            }
        }
    }

    fun closeVote() {
        _openVote.value = "CLOSE"
    }

    fun resetIsPosted() {
        _requestPostComment.value = false
    }

    fun requestVoteFeedDetail(id: Int) {
        viewModelScope.launch {
            voteRepository.requestVoteFeedDetail(id).collect { feedDetail ->
                feedDetail?.let {
                    _feedDetail.emit(feedDetail)
                }
            }
        }
    }

    fun requestVoteFeedComment(id: Int) {
        viewModelScope.launch {
            voteRepository.requestVoteFeedComment(id).collect { feedDetailComments ->
                feedDetailComments?.let {
                    _feedDetailComments.emit(feedDetailComments)
                }
            }
        }
    }

    fun requestVoteFeedDelete(id: Int) {
        viewModelScope.launch {
            voteRepository.requestVoteFeedDelete(id).collect { resultDelete ->
                _requestDelete.emit(resultDelete)
            }
        }
    }

    fun requestVote(id: Int, vote: String) {
        viewModelScope.launch {
            voteRepository.requestVote(id, vote).collect { result ->
                when (result) {
                    true -> {
                        _requestVote.emit(vote)
                        _feedDetail.emit(
                            with(requireNotNull(_feedDetail.value)) {
                                copy(
                                    permitCount = responsePermitVote(vote, this.permitCount),
                                    rejectCount = responseRejectVote(vote, this.rejectCount),
                                )
                            }
                        )
                    }
                }
            }
        }
    }

    fun requestPostComment(id: Int, content: String) {
        viewModelScope.launch {
            voteRepository.requestVotePostComment(id, content).collect { result ->
                _requestPostComment.emit(result)
            }
        }
    }

    fun requestDeleteComment(id: Int) {
        viewModelScope.launch {
            commentRepository.requestDeleteComment(id).collect { result ->
                when (result) {
                    true -> {
                        _feedDetailComments.emit((requireNotNull(_feedDetailComments.value).map { comment ->
                            if (comment.commentId == id) {
                                comment.copy(deleted = true)
                            } else comment
                        }).filterNot { comment ->
                            (comment.deleted && comment.replyCount == 0)
                        })
                    }
                }
            }
        }
    }

    fun requestPutEmoji(emojiId: Int, checked: Boolean) {
        viewModelScope.launch {
            emojiRepository.requestPutEmoji(emojiId, checked).collect {
            }
        }
    }

    private fun responsePermitVote(vote: String, count: Int) = when (vote) {
        "PERMIT" -> count + 1
        else -> count
    }

    private fun responseRejectVote(vote: String, count: Int) = when (vote) {
        "REJECT" -> count + 1
        else -> count
    }
}
