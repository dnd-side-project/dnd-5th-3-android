package how.about.it.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import how.about.it.network.comment.CommentServiceImpl
import how.about.it.network.feed.FeedServiceImpl
import how.about.it.network.vote.VoteServiceImpl
import how.about.it.view.comment.repository.CommentRepository
import how.about.it.view.feed.repository.FeedRepository
import how.about.it.view.vote.repository.VoteRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideFeedRepository(feedServiceImpl: FeedServiceImpl): FeedRepository =
        FeedRepository(feedServiceImpl)

    @Provides
    @Singleton
    fun provideVoteRepository(voteServiceImpl: VoteServiceImpl): VoteRepository =
        VoteRepository(voteServiceImpl)

    @Provides
    @Singleton
    fun provideCommentRepository(commentServiceImpl: CommentServiceImpl): CommentRepository =
        CommentRepository(commentServiceImpl)
}