package how.about.it.viewmodel

import androidx.lifecycle.*
import how.about.it.model.Notice
import how.about.it.repository.NoticeRepository

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