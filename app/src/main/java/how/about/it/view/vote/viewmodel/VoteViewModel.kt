package how.about.it.view.vote.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import how.about.it.view.comment.Comment
import how.about.it.view.vote.RequestVote
import how.about.it.view.vote.ResponseFeedDetail
import how.about.it.view.vote.repository.VoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    private val _networkError = MutableStateFlow(false)
    val networkError: StateFlow<Boolean> = _networkError

    fun setOpenVote() {
        _openVote.value = when (_openVote.value) {
            0 -> 1
            1 -> 0
            else -> throw IllegalAccessException()
        }
    }

    fun requestVoteFeedDetail(id: Int) {
        viewModelScope.launch {
            val feedDetail = runCatching { voteRepository.requestVoteFeedDetail(id) }.getOrNull()
            Log.d("feedDetail", feedDetail.toString())
            feedDetail?.let {
                _feedDetail.emit(feedDetail)
            } ?: _feedDetail.emit(
                ResponseFeedDetail(
                    id = 0,
                    name = "kimym",
                    title = "testttt",
                    content = "content",
                    productImageUrl = "https://user-images.githubusercontent.com/63637706/129483564-5a341dff-6eb7-412a-b375-c59e8c491876.png",
                    isVoted = false,
                    permitRatio = 80,
                    rejectRatio = 20,
                    createdDate = "2021-08-16T13:34:00",
                    voteDeadline = "2021-08-17T13:34:00",
                    currentMemberVoteResult = "NO_RESULT"
                )
            )
        }
    }


    fun requestVoteFeedComment(id: Int) {
        viewModelScope.launch {
            val feedDetailComment =
                runCatching { voteRepository.requestVoteFeedComment(id) }.getOrNull()
            Log.d("feedDetailComment", feedDetailComment.toString())
            feedDetailComment?.let {
                _feedDetailComment.emit(feedDetailComment.commentList)
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
            } ?: _requestVote.emit(index)
        }
    }
}
