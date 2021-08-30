package com.moo.mool.viewmodel

import androidx.lifecycle.*
import com.moo.mool.model.Notice
import com.moo.mool.repository.NoticeRepository

class NoticeViewModel : ViewModel() {

    val getAllNoticeList : LiveData<List<Notice>>
    private var noticeRepository : NoticeRepository

    init {
        noticeRepository = NoticeRepository()
        getAllNoticeList = noticeRepository.requestNoticeList()
    }

    fun requestNoticePost(noticeId: String) : LiveData<Notice> {
        return noticeRepository.requestNoticePost(noticeId)
    }
}