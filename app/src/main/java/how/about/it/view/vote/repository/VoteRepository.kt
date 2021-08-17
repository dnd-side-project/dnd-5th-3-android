package how.about.it.view.vote.repository

import how.about.it.network.vote.VoteServiceImpl
import how.about.it.view.vote.RequestVote

class VoteRepository(private val voteServiceImpl: VoteServiceImpl) {
    suspend fun requestVoteFeedDetail(id: Int) = voteServiceImpl.requestVoteFeedDetail(id)

    suspend fun requestVoteFeedComment(id: Int) = voteServiceImpl.requestVoteFeedComment(id)

    suspend fun requestVote(id: Int, body: RequestVote) = voteServiceImpl.requestVote(id, body)

    suspend fun requestVoteDelete(id: Int) = voteServiceImpl.requestVoteDelete(id)
}