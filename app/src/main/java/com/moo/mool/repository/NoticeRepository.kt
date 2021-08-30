package com.moo.mool.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.moo.mool.model.Notice
import com.moo.mool.model.ResponseNotice
import com.moo.mool.network.RequestToServer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NoticeRepository {

    fun requestNoticeList() : LiveData<List<Notice>> {
        val posts = MutableLiveData<List<Notice>>()

        RequestToServer.service.requestNoticeList()
            .enqueue(object : Callback<ResponseNotice> {
                override fun onResponse(
                    call: Call<ResponseNotice>,
                    response: Response<ResponseNotice>
                ) {
                    posts.value = response.body()!!.noticeList
                }
                override fun onFailure(call: Call<ResponseNotice>, t: Throwable) {
                    t.stackTrace
                }

            })
        return posts
    }

    fun requestNoticePost(noticeId: String) : LiveData<Notice> {
        val post = MutableLiveData<Notice>()

        RequestToServer.service.requestNoticePost(noticeId)
            .enqueue(object : Callback<Notice> {
                override fun onResponse(call: Call<Notice>, response: Response<Notice>) {
                    post.value = response.body()!!
                }

                override fun onFailure(call: Call<Notice>, t: Throwable) {
                    t.stackTrace
                }
            })
        return post
    }
}