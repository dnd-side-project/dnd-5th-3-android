package com.moo.mool.di

import com.moo.mool.network.comment.CommentServiceImpl
import com.moo.mool.network.feed.FeedServiceImpl
import com.moo.mool.network.mypage.MyPageServiceImpl
import com.moo.mool.network.vote.VoteServiceImpl
import com.moo.mool.view.comment.repository.CommentRepository
import com.moo.mool.view.feed.repository.FeedRepository
import com.moo.mool.view.mypage.repository.MyPageRepository
import com.moo.mool.view.vote.repository.VoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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

    @Provides
    @Singleton
    fun provideMyPageRepository(myPageServiceImpl: MyPageServiceImpl): MyPageRepository =
        MyPageRepository(myPageServiceImpl)
}
