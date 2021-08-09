package how.about.it.view.write

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import how.about.it.R
import how.about.it.databinding.FragmentTempSavedWriteBinding
import how.about.it.view.main.MainActivity

class TempSavedWriteFragment : Fragment() {
    private var _binding: FragmentTempSavedWriteBinding? = null
    private val binding get() = requireNotNull(_binding)

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
        _binding = FragmentTempSavedWriteBinding.inflate(layoutInflater)

        binding.toolbarWriteTempSavedBoard.tvToolbarTitle.text = "저장된 글"
        (activity as MainActivity).setSupportActionBar(binding.toolbarWriteTempSavedBoard.toolbarBoard)
        (activity as MainActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        (activity as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        showBackButton()

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_write_temp_recall_toolbar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if (item.itemId == android.R.id.home) {
            (activity as MainActivity).onBackPressed()
        } else if(item.itemId == R.id.action_recall_temp_post) {
            // TODO : Dialog 띄우기 코드 개선 필요
            val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_default_confirm, null)
            val mBuilder = AlertDialog.Builder(requireContext())
                .setView(mDialogView)
            val mAlertDialog = mBuilder.show()

            // Dialog 제목 및 내용 설정
            mDialogView.findViewById<TextView>(R.id.tv_message_dialog_title).setText(R.string.recall)
            mDialogView.findViewById<TextView>(R.id.tv_message_dialog_description).setText(R.string.write_temp_post_recall_dialog_description)

            // Dialog 확인, 취소버튼 설정
            val confirmButton = mDialogView.findViewById<Button>(R.id.btn_dialog_confirm)
            val cancelButton = mDialogView.findViewById<Button>(R.id.btn_dialog_cancel)

            // Dialog 확인 버튼을 클릭 한 경우
            confirmButton.setOnClickListener {
                // TODO : 임시 저장 내용을 현재 글쓰기 화면으로 돌아가면서 삽입하는 코드 작성
                // TODO : 이후 임시 저장글 리스트에서 불러온 저장글 삭제하는 코드 작성
                mAlertDialog.dismiss()
            }
            cancelButton.setOnClickListener {
                mAlertDialog.dismiss()
            }
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