package com.moo.mool.view.write

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.moo.mool.R
import com.moo.mool.database.TempPost
import com.moo.mool.databinding.FragmentTempSavedWriteBinding
import com.moo.mool.util.DefaultDialogUtil
import com.moo.mool.util.ToolbarDecorationUtil
import com.moo.mool.util.popBackStack
import com.moo.mool.view.main.MainActivity
import com.moo.mool.viewmodel.WriteViewModel
import java.util.*

class TempSavedWriteFragment : Fragment() {
    private var _binding: FragmentTempSavedWriteBinding? = null
    private val binding get() = requireNotNull(_binding)
    private lateinit var writeViewModel : WriteViewModel
    private lateinit var currentTempPost : TempPost

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentTempPost = arguments?.getParcelable<TempPost>("tempPost")!!

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    popBackStack()
                }
            }

        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTempSavedWriteBinding.inflate(layoutInflater)
        writeViewModel = ViewModelProvider(this, WriteViewModel.Factory(requireActivity().application)).get(WriteViewModel::class.java)
        binding.writeViewModel = writeViewModel

        ToolbarDecorationUtil.setToolbarDetail(binding.toolbarWriteTempSavedBoard, R.string.write_temp_post, requireActivity(), this)

        binding.tvTempSavedWriteTitle.text = currentTempPost.title
        binding.tvTempSavedWriteDetail.text = currentTempPost.content
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.imgTempSavedWrite.setImageBitmap(currentTempPost.productImage?.toBitmap())
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_write_temp_recall_toolbar, menu)
        menu?.apply {
            for(index in 0 until this.size()){
                val item = this.getItem(index)
                val s = SpannableString(item.title)
                s.setSpan(ForegroundColorSpan(resources.getColor(R.color.moomool_pink_ff227c, requireContext()?.theme)),0,s.length,0)
                item.title = s
            }
        }
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if (item.itemId == android.R.id.home) {
            (activity as MainActivity).onBackPressed()
        } else if(item.itemId == R.id.action_recall_temp_post) {
            val mAlertDialog = DefaultDialogUtil.createDialog(requireContext(),
                R.string.recall, R.string.write_temp_post_recall_dialog_description,
                true, true, null, null,
                {
                    val bundle = Bundle()
                    bundle.putParcelable("currentTempPost", currentTempPost)
                    // 불러온 글을 WriteFragment로 데이터를 넘기면서 해당 임시 저장글 삭제하여 불러온 글이 임시저장 리스트에 남아있지 않도록 함
                    writeViewModel.deleteTempPost(currentTempPost)
                    requireView().findNavController().navigate(R.id.action_tempSavedWriteFragment_to_writeFragment, bundle)
                }, null
            )
            mAlertDialog.show()
        }
        return true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun String.toBitmap(): Bitmap?{
        Base64.getDecoder().decode(this).apply {
            return BitmapFactory.decodeByteArray(this,0,size)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}