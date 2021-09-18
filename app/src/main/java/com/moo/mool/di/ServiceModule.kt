package com.moo.mool.di

import com.moo.mool.network.RequestToServer
import com.moo.mool.network.comment.CommentInterface
import com.moo.mool.network.comment.CommentService
import com.moo.mool.network.comment.CommentServiceImpl
import com.moo.mool.network.emoji.EmojiInterface
import com.moo.mool.network.emoji.EmojiService
import com.moo.mool.network.emoji.EmojiServiceImpl
import com.moo.mool.network.feed.FeedInterface
import com.moo.mool.network.feed.FeedService
import com.moo.mool.network.feed.FeedServiceImpl
import com.moo.mool.network.login.LoginInterface
import com.moo.mool.network.login.LoginService
import com.moo.mool.network.login.LoginServiceImpl
import com.moo.mool.network.mypage.MyPageInterface
import com.moo.mool.network.vote.VoteInterface
import com.moo.mool.network.vote.VoteService
import com.moo.mool.network.vote.VoteServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
    fun provideEmojiInterface(): EmojiInterface = RequestToServer.emojiInterface

    @Provides
    @Singleton
    fun provideMyPageInterface(): MyPageInterface = RequestToServer.myPageInterface

    @Provides
    @Singleton
    fun provideLoginInterface(): LoginInterface = RequestToServer.loginInterface

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

    @Provides
    @Singleton
    fun provideEmojiServiceImpl(emojiInterface: EmojiInterface): EmojiService =
        EmojiServiceImpl(emojiInterface)

    @Provides
    @Singleton
    fun provideLoginServiceImpl(loginInterface: LoginInterface): LoginService =
        LoginServiceImpl(loginInterface)
}
