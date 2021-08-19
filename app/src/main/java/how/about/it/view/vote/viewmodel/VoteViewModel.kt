package how.about.it.view.vote.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import how.about.it.view.comment.Comment
import how.about.it.view.vote.RequestCommentId
import how.about.it.view.vote.RequestPostComment
import how.about.it.view.vote.RequestVote
import how.about.it.view.vote.ResponseFeedDetail
import how.about.it.view.vote.repository.VoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VoteViewModel(private val voteRepository: VoteRepository) : ViewModel() {
    private val _openVote = MutableStateFlow(0)
    val openVote = _openVote.asStateFlow()

    private val _feedDetail = MutableStateFlow<ResponseFeedDetail?>(null)
    val feedDetail = _feedDetail.asStateFlow()

    private val _feedDetailComment = MutableStateFlow<List<Comment>?>(null)
    val feedDetailComment = _feedDetailComment.asStateFlow()

    private val _requestVote = MutableStateFlow<Int?>(null)
    val requestVote = _requestVote.asStateFlow()

    private val _requestDelete = MutableStateFlow(false)
    val requestDelete = _requestDelete.asStateFlow()

    private val _requestPostComment = MutableStateFlow(false)
    val requestPostComment = _requestPostComment.asStateFlow()

    private val _networkError = MutableStateFlow(false)
    val networkError = _networkError.asStateFlow()

    fun setOpenVote() {
        _openVote.value = when (_openVote.value) {
            0 -> 1
            1 -> 0
            else -> throw IllegalAccessException()
        }
    }

    fun openVote() {
        _feedDetail.value?.let {
            if (requireNotNull(_feedDetail.value).currentMemberVoteResult == "NO_RESULT") {
                _openVote.value = 1
            }
        }
    }

    fun requestVoteFeedDetail(id: Int) {
        viewModelScope.launch {
            val feedDetail = runCatching { voteRepository.requestVoteFeedDetail(id) }.getOrNull()
            Log.d("feedDetail", feedDetail.toString())
            feedDetail?.let {
                _feedDetail.emit(feedDetail)
            } ?: _networkError.emit(true)
        }
    }

    fun requestVoteFeedComment(id: Int) {
        viewModelScope.launch {
            val feedDetailComment =
                runCatching { voteRepository.requestVoteFeedComment(id) }.getOrNull()
            Log.d("feedDetailComment", feedDetailComment.toString())
            feedDetailComment?.let {
                _feedDetailComment.emit(feedDetailComment.commentList.filterNot { comment ->
                    (comment.deleted && comment.replyCount == 0)
                })
            } ?: _networkError.emit(true)
        }
    }

    fun requestVote(index: Int, id: Int, vote: String) {
        viewModelScope.launch {
            val requestVote =
                runCatching { voteRepository.requestVote(id, RequestVote(vote)) }.getOrNull()
            Log.d("requestVote", requestVote.toString())
            requestVote?.let {
                _requestVote.emit(index)
                _feedDetail.emit(
                    with(requireNotNull(_feedDetail.value)) {
                        copy(
                            permitCount = responsePermitVote(vote, this.permitCount),
                            rejectCount = responseRejectVote(vote, this.rejectCount),
                        )
                    })
            } ?: _requestVote.emit(index)
        }
    }

    fun requestVoteDelete(id: Int) {
        viewModelScope.launch {
            val requestVoteDelete =
                runCatching { voteRepository.requestVoteDelete(id) }.getOrNull()
            Log.d("requestVoteDelete", requestVoteDelete.toString())
            requestVoteDelete?.let {
                _requestDelete.emit(true)
            } ?: _networkError.emit(true)
        }
    }

    fun requestPostComment(id: Int, content: String) {
        viewModelScope.launch {
            val requestPostComment = runCatching {
                voteRepository.requestVotePostComment(
                    RequestPostComment(postId = id, content = content)
                )
            }.getOrNull()
            requestPostComment?.let {
                _requestPostComment.emit(true)
            } ?: _networkError.emit(true)
        }
    }

    fun requestDeleteComment(id: Int) {
        viewModelScope.launch {
            val requestDeleteComment = runCatching {
                voteRepository.requestCommentDelete(RequestCommentId(commentId = id))
            }.getOrNull()
            Log.d("delete", requestDeleteComment.toString())
            requestDeleteComment?.let {
                _feedDetailComment.emit((requireNotNull(_feedDetailComment.value).map { comment ->
                    if (comment.commentId == id) {
                        comment.copy(deleted = true)
                    } else comment
                }).filterNot { comment ->
                    (comment.deleted && comment.replyCount == 0)
                })
            } ?: _networkError.emit(true)
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
