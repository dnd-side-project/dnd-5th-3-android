package how.about.it.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import how.about.it.network.RequestToServer
import how.about.it.network.comment.CommentInterface
import how.about.it.network.comment.CommentService
import how.about.it.network.comment.CommentServiceImpl
import how.about.it.network.feed.FeedInterface
import how.about.it.network.feed.FeedService
import how.about.it.network.feed.FeedServiceImpl
import how.about.it.network.mypage.MyPageInterface
import how.about.it.network.vote.VoteInterface
import how.about.it.network.vote.VoteService
import how.about.it.network.vote.VoteServiceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    @Provides
    @Singleton
    fun provideFeedInterface(): FeedInterface = RequestToServer.feedInterface

    @Provides
    @Singleton
    fun provideVoteInterface(): VoteInterface = RequestToServer.voteInterface

    @Provides
    @Singleton
    fun provideCommentInterface(): CommentInterface = RequestToServer.commentInterface

    @Provides
    @Singleton
    fun provideMyPageInterface(): MyPageInterface = RequestToServer.myPageInterface

    @Provides
    @Singleton
    fun provideFeedServiceImpl(feedInterface: FeedInterface): FeedService =
        FeedServiceImpl(feedInterface)

    @Provides
    @Singleton
    fun provideVoteServiceImpl(voteInterface: VoteInterface): VoteService =
        VoteServiceImpl(voteInterface)

    @Provides
    @Singleton
    fun provideCommentServiceImpl(commentInterface: CommentInterface): CommentService =
        CommentServiceImpl(commentInterface)
}
