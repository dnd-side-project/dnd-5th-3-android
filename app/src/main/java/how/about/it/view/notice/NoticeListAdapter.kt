package how.about.it.view.notice

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import how.about.it.databinding.ItemNoticeBinding
import how.about.it.model.Notice

class NoticeListAdapter: RecyclerView.Adapter<NoticeListAdapter.NoticeListItemViewHolder>() {
    private var noticeList = emptyList<Notice>()

    class NoticeListItemViewHolder(val binding: ItemNoticeBinding) :RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeListItemViewHolder {
        val binding = ItemNoticeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoticeListItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoticeListItemViewHolder, position: Int) {
        // 원하는 형식으로 보여주기 위하여 문자열 자른후, -문자 .로 변환
        this.noticeList[position].createdDate = noticeList[position].createdDate.toString().substring(0, 10)
            .replace("-", ".")

        holder.binding.notice = noticeList[position]
        holder.itemView.setOnClickListener{
            itemClickListener.onClick(it, position)
        }
    }

    override fun getItemCount(): Int {
        return noticeList.size
    }

    fun setData(notice: List<Notice>){
        noticeList = notice
        notifyDataSetChanged()
    }

    interface ItemClickListener {
        fun onClick(view: View, position: Int)
    }
    private lateinit var itemClickListener: ItemClickListener
    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }
}