package com.moo.mool.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.moo.mool.network.RequestToServer
import com.moo.mool.network.comment.CommentInterface
import com.moo.mool.network.comment.CommentService
import com.moo.mool.network.comment.CommentServiceImpl
import com.moo.mool.network.feed.FeedInterface
import com.moo.mool.network.feed.FeedService
import com.moo.mool.network.feed.FeedServiceImpl
import com.moo.mool.network.mypage.MyPageInterface
import com.moo.mool.network.vote.VoteInterface
import com.moo.mool.network.vote.VoteService
import com.moo.mool.network.vote.VoteServiceImpl
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
