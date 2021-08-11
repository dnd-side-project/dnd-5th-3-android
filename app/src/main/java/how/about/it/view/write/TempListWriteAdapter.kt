package how.about.it.view.write

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import how.about.it.database.TempPost
import how.about.it.databinding.ItemWriteSaveTempBinding

class TempListWriteAdapter : RecyclerView.Adapter<TempListWriteAdapter.TempListViewHolder>() {
    private var tempPostList = emptyList<TempPost>()

    class TempListViewHolder(val binding: ItemWriteSaveTempBinding) :RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TempListViewHolder {
        val binding = ItemWriteSaveTempBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TempListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TempListViewHolder, position: Int) {
        val currentTempPost = tempPostList[position]
        holder.binding.tvWriteSaveTempTitle.text = currentTempPost.title.toString()
        holder.binding.tvWriteSaveTempDetail.text = currentTempPost.content.toString()
        // holder.binding.imgWriteSaveTemp.text = currentTempPost.product_image.toString()
        holder.binding.tvWriteSaveTempSavetime.text = currentTempPost.created_date.toString()

        holder.itemView.setOnClickListener{
            itemClickListener.onClick(it, position)
        }
    }

    override fun getItemCount(): Int {
        return tempPostList.size
    }

    fun setData(tempPost: List<TempPost>){
        tempPostList = tempPost
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