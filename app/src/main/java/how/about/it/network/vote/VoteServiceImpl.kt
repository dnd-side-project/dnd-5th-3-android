package how.about.it.network.vote

import how.about.it.view.vote.RequestVote

class VoteServiceImpl(private val voteInterface: VoteInterface) : VoteService {
    override suspend fun requestVoteFeedDetail(id: Int) = voteInterface.requestVoteFeedDetail(id)

    override suspend fun requestVoteFeedComment(id: Int) = voteInterface.requestVoteFeedComment(id)

    override suspend fun requestVote(id: Int, body: RequestVote) =
        voteInterface.requestVote(id, body)

    override suspend fun requestVoteDelete(id: Int) = voteInterface.requestVoteDelete(id)
}
