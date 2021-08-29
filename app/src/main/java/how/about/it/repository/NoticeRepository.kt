package how.about.it.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import how.about.it.model.Notice
import how.about.it.model.ResponseNotice
import how.about.it.network.RequestToServer
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