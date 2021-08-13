package how.about.it.view.write

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import how.about.it.database.TempPost
import how.about.it.databinding.ItemWriteSaveTempBinding
import java.util.*
import how.about.it.util.TimeChangerUtil
import java.lang.Math.min

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.binding.imgWriteSaveTemp.setImageBitmap(currentTempPost.productImage?.toBitmap()?.toSquare())
        }
        holder.binding.tvWriteSaveTempSavetime.text = TimeChangerUtil.timeChange(holder.binding.tvWriteSaveTempSavetime.context, currentTempPost.createdDate.toString())

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

    @RequiresApi(Build.VERSION_CODES.O)
    fun String.toBitmap(): Bitmap?{
        Base64.getDecoder().decode(this).apply {
            return BitmapFactory.decodeByteArray(this,0,size)
        }
    }
    fun Bitmap.toSquare():Bitmap?{
        // get the small side of bitmap
        val side = min(width,height)

        // calculate the x and y offset
        val xOffset = (width - side) /2
        val yOffset = (height - side)/2

        // create a square bitmap
        // a square is closed, two dimensional shape with 4 equal sides
        return Bitmap.createBitmap(
            this, // source bitmap
            xOffset, // x coordinate of the first pixel in source
            yOffset, // y coordinate of the first pixel in source
            side, // width
            side // height
        )
    }
}