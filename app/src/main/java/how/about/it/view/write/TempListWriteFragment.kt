package how.about.it.view.write

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import how.about.it.R
import how.about.it.databinding.FragmentTempListWriteBinding
import how.about.it.view.ToastDefaultBlack
import how.about.it.view.main.MainActivity

class TempListWriteFragment : Fragment() {
    private var _binding: FragmentTempListWriteBinding? = null
    private val binding get() = requireNotNull(_binding)
    // 임시 저장 글 삭제 중 확인
    var isEditing = false

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

        binding.toolbarWriteTempListBoard.tvToolbarTitle.setText(R.string.write_temp_save)
        (activity as MainActivity).setSupportActionBar(binding.toolbarWriteTempListBoard.toolbarBoard)
        (activity as MainActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        (activity as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        showBackButton()

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_write_temp_toolbar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        
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

                // 임시 저장글 false 임시처리
                if(false/** 임시 저장글을 선택한 경우 **/ ){
                    mDialogView.findViewById<TextView>(R.id.tv_message_dialog_title).setText(R.string.delete)
                    mDialogView.findViewById<TextView>(R.id.tv_message_dialog_description).setText(R.string.write_temp_list_delete_dialog_description)

                    confirmButton.setOnClickListener {
                        // TODO : 임시 게시글 삭제 코드 작성
                        mAlertDialog.dismiss()
                        ToastDefaultBlack.createToast(requireContext(), getString(R.string.write_temp_list_delete_toast_description))
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
            }
            else if(!isEditing){
                deleteItem.setIcon(null)
                // TODO : 삭제 모드로 진입 (RecyclerView List 선택으로 변경)
            }
            isEditing = !isEditing
        }
        return true
    }
    private fun showBackButton() {
        (activity as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        this.setHasOptionsMenu(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}