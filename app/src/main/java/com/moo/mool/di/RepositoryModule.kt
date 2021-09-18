package com.moo.mool.di

import android.content.Context
import com.moo.mool.network.comment.CommentServiceImpl
import com.moo.mool.network.emoji.EmojiServiceImpl
import com.moo.mool.network.feed.FeedServiceImpl
import com.moo.mool.network.login.LoginServiceImpl
import com.moo.mool.network.mypage.MyPageServiceImpl
import com.moo.mool.network.vote.VoteServiceImpl
import com.moo.mool.repository.LoginRepository
import com.moo.mool.repository.SettingRepository
import com.moo.mool.view.comment.repository.CommentRepository
import com.moo.mool.view.comment.repository.EmojiRepository
import com.moo.mool.view.feed.repository.FeedRepository
import com.moo.mool.view.mypage.repository.MyPageRepository
import com.moo.mool.view.vote.repository.VoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun provideEmojiRepository(emojiServiceImpl: EmojiServiceImpl): EmojiRepository =
        EmojiRepository(emojiServiceImpl)

    @Provides
    @Singleton
    fun provideMyPageRepository(myPageServiceImpl: MyPageServiceImpl): MyPageRepository =
        MyPageRepository(myPageServiceImpl)

    @Provides
    @Singleton
    fun provideLoginRepository(
        loginServiceImpl: LoginServiceImpl,
        @ApplicationContext context: Context
    ): LoginRepository =
        LoginRepository(loginServiceImpl, context)

    @Provides
    @Singleton
    fun provideSettingRepository(@ApplicationContext context: Context): SettingRepository =
        SettingRepository(context)
}
