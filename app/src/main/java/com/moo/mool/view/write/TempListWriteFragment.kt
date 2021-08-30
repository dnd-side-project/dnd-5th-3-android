package com.moo.mool.view.write

import android.app.AlertDialog
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.moo.mool.R
import com.moo.mool.database.TempPost
import com.moo.mool.databinding.FragmentTempListWriteBinding
import com.moo.mool.databinding.ItemWriteSaveTempBinding
import com.moo.mool.view.ToastDefaultBlack
import com.moo.mool.view.main.MainActivity
import com.moo.mool.viewmodel.WriteViewModel

class TempListWriteFragment : Fragment() {
    private var _binding: FragmentTempListWriteBinding? = null
    private val binding get() = requireNotNull(_binding)
    // 임시 저장 글 삭제 중 확인
    var isEditing = false
    private lateinit var writeViewModel : WriteViewModel
    lateinit var tempPostList : List<TempPost>
    var deletePost = mutableListOf<TempPost>()
    val tempListAdapter = TempListWriteAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    requireView().findNavController().popBackStack()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTempListWriteBinding.inflate(layoutInflater)
        writeViewModel = ViewModelProvider(this, WriteViewModel.Factory(requireActivity().application)).get(WriteViewModel::class.java)
        binding.writeViewModel = writeViewModel
        binding.rvWriteTempPostAll.adapter = tempListAdapter

        writeViewModel.getAllTempList.observe(viewLifecycleOwner, Observer {
            tempListAdapter.setData(it)
            tempPostList = it
        })

        tempListAdapter.setItemClickListener(object : TempListWriteAdapter.ItemClickListener{
            // 글 불러오기 화면으로 이동
            override fun onClick(view: View, position: Int) {
                val bundle = Bundle()
                bundle.putParcelable("tempPost", tempPostList[position])
                requireView().findNavController().navigate(R.id.action_tempListWriteFragment_to_tempSavedWriteFragment, bundle)
            }
        })

        binding.toolbarWriteTempListBoard.tvToolbarTitle.setText(R.string.write_temp_save)
        (activity as MainActivity).setSupportActionBar(binding.toolbarWriteTempListBoard.toolbarBoard)
        (activity as MainActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        showBackButton()

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_write_temp_toolbar, menu)
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
        binding.rvWriteTempPostAll.adapter = tempListAdapter

        if (item.itemId == android.R.id.home) {
            (activity as MainActivity).onBackPressed()
        } else if(item.itemId == R.id.action_delete_temp_post) {
            val deleteItem = binding.toolbarWriteTempListBoard.toolbarBoard.menu.findItem(R.id.action_delete_temp_post)
            // 삭제 중인 경우
            if(isEditing){
                // TODO : Dialog 띄우기 코드 개선 필요
                val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_default_confirm, null)
                val mBuilder = AlertDialog.Builder(requireContext())
                    .setView(mDialogView)
                val mAlertDialog = mBuilder.show()
                // Dialog 확인, 취소버튼 설정
                val confirmButton = mDialogView.findViewById<Button>(R.id.btn_dialog_confirm)
                val cancelButton = mDialogView.findViewById<Button>(R.id.btn_dialog_cancel)

                if(deletePost.isNotEmpty()){
                    mDialogView.findViewById<TextView>(R.id.tv_message_dialog_title).setText(R.string.delete)
                    mDialogView.findViewById<TextView>(R.id.tv_message_dialog_description).setText(R.string.write_temp_list_delete_dialog_description)

                    confirmButton.setOnClickListener {
                        // 임시 게시글 삭제
                        for(post in deletePost){
                            writeViewModel.deleteTempPost(post)
                        }
                        mAlertDialog.dismiss()
                        ToastDefaultBlack.createToast(requireContext(), getString(R.string.write_temp_list_delete_toast_description))?.show()
                    }
                    cancelButton.setOnClickListener {
                        mAlertDialog.dismiss()
                    }
                } else { /** 선택한 임시 저장글이 없는 상태로 누른 경우 **/
                    mDialogView.findViewById<TextView>(R.id.tv_message_dialog_title).setText(R.string.write_temp_list_delete_empty_dialog_title)
                    mDialogView.findViewById<TextView>(R.id.tv_message_dialog_description).setText(R.string.write_temp_list_delete_empty_dialog_description)

                    mDialogView.findViewById<ConstraintLayout>(R.id.layout_dialog_cancel).visibility = View.GONE
                    confirmButton.setOnClickListener {
                        mAlertDialog.dismiss()
                    }
                }
                deleteItem.setIcon(R.drawable.ic_trashbin)

                // 삭제로 인하여 onClick이 Override 되었기 때문에 다시 목록 선택click 이 될 수 있도록 override
                // TODO : OnCreatedView에서 코드와 Duplicate Code
                tempListAdapter.setItemClickListener(object : TempListWriteAdapter.ItemClickListener{
                    // 글 불러오기 화면으로 이동
                    override fun onClick(view: View, position: Int) {
                        val bundle = Bundle()
                        bundle.putParcelable("tempPost", tempPostList[position])
                        requireView().findNavController().navigate(R.id.action_tempListWriteFragment_to_tempSavedWriteFragment, bundle)
                    }
                })
            }
            else if(!isEditing){
                deleteItem.setIcon(null)
                // 삭제 모드로 진입. 삭제할 게시글들 선택하여 deletePost에 추가
                tempListAdapter.setItemClickListener(object : TempListWriteAdapter.ItemClickListener{
                    override fun onClick(view: View, position: Int) {
                        TempListWriteAdapter.TempListViewHolder(ItemWriteSaveTempBinding.bind(view)).binding.layoutWriteTempListItem.setBackgroundColor(requireContext().resources.getColor(R.color.bluegray800_303540, context?.theme))
                        deletePost += tempPostList[position]
                    }
                })
                deletePost.clear()
            }
            isEditing = !isEditing
        }
        return true
    }
    private fun showBackButton() {
        (activity as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back)
        this.setHasOptionsMenu(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}