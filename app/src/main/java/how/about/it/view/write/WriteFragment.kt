package how.about.it.view.write

import android.app.Application
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import how.about.it.R
import how.about.it.database.TempPost
import how.about.it.database.TempPostDatabase
import how.about.it.databinding.FragmentWriteBinding
import how.about.it.view.ToastDefaultBlack
import how.about.it.view.main.MainActivity
import how.about.it.viewmodel.WriteViewModel
import java.util.*

class WriteFragment : Fragment() {
    private var _binding: FragmentWriteBinding? = null
    private val binding get() = requireNotNull(_binding)
    private lateinit var writeViewModel: WriteViewModel
    private lateinit var db : TempPostDatabase

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
        _binding = FragmentWriteBinding.inflate(layoutInflater)
        db = TempPostDatabase.getDatabase(requireContext())!!

        writeViewModel = ViewModelProvider(this, WriteViewModel.Factory(Application())).get(WriteViewModel::class.java)
        binding.writeViewModel = writeViewModel

        binding.toolbarWriteBoard.tvToolbarTitle.text = "글쓰기"
        (activity as MainActivity).setSupportActionBar(binding.toolbarWriteBoard.toolbarBoard)
        (activity as MainActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        (activity as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        showBackButton()

        // 임시 저장한 게시글을 불러온 경우 채워넣음
        val tempPost = arguments?.getParcelable<TempPost>("currentTempPost")
        binding.etWriteTitle.setText(tempPost?.title)
        binding.etWriteContent.setText(tempPost?.content)
        // binding.imgTempSavedWrite = arguments?.getString("image")

        binding.etWriteTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { afterTextChanged(s as Editable?) }
            override fun afterTextChanged(s: Editable?) {
                if(!s.toString().trim().isNullOrBlank() && !binding.etWriteContent.text.toString().trim().isNullOrBlank() ) {
                    activeButtonSave()
                } else {
                    deactiveButtonSave()
                }
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { afterTextChanged(s as Editable?) }
        })

        binding.etWriteContent.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { afterTextChanged(s as Editable?) }
            override fun afterTextChanged(s: Editable?) {
                if(!s.toString().trim().isNullOrBlank() && !binding.etWriteTitle.text.toString().trim().isNullOrBlank() ) {
                    activeButtonSave()
                } else {
                    deactiveButtonSave()
                }
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { afterTextChanged(s as Editable?) }
        })

        binding.btnWriteAddPhoto.setOnClickListener {
            /*
            // TODO : Dialog 띄우기 코드 개선 필요
            val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_default_confirm, null)
            val mBuilder = AlertDialog.Builder(requireContext())
                .setView(mDialogView)
            val mAlertDialog = mBuilder.show()
            // TODO : 파일 검사 조건문 설정
            // Dialog 제목 및 내용 설정
            if ( /** 형식이 올바르지 않은 경우 **/){
                mDialogView.findViewById<TextView>(R.id.tv_message_dialog_title).setText(R.string.write_save_fail_format_dialog_title)
                mDialogView.findViewById<TextView>(R.id.tv_message_dialog_description).setText(R.string.write_save_fail_format_dialog_description)
            } else if (/** 서버와 연동에 실패한 경우 **/) {
                mDialogView.findViewById<TextView>(R.id.tv_message_dialog_title).setText(R.string.write_save_fail_server_error_dialog_title)
                mDialogView.findViewById<TextView>(R.id.tv_message_dialog_description).setText(R.string.write_save_fail_server_error_dialog_description)
            }
            // Dialog 확인, 취소버튼 설정
            val confirmButton = mDialogView.findViewById<Button>(R.id.btn_dialog_confirm)
            mDialogView.findViewById<ConstraintLayout>(R.id.layout_dialog_cancel).visibility = View.GONE

            // Dialog 확인 버튼을 클릭 한 경우
            confirmButton.setOnClickListener {
                mAlertDialog.dismiss()
            }
             */
        }

        binding.fabWriteToComplete.setOnClickListener {
            ToastDefaultBlack.createToast(requireContext(), "저장 버튼 클릭")?.show()
        }

        binding.btnWriteTempPostsArchive.setOnClickListener {
            requireView().findNavController().navigate(R.id.action_writeFragment_to_tempListWriteFragment)
        }
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_write_toolbar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if (item.itemId == android.R.id.home) {
            (activity as MainActivity).onBackPressed()
        } else if(item.itemId == R.id.action_save_temp_post) {
            if(binding.etWriteTitle.text.toString().trim().isNullOrBlank() || binding.etWriteContent.text.toString().trim().isNullOrBlank()) {
                ToastDefaultBlack.createToast(requireContext(), getString(R.string.write_temp_save_fail_empty_message))?.show()
            } else {
                val tempPost = TempPost(binding.etWriteTitle.text.toString(), "TestProductName", binding.etWriteContent.text.toString(), "testProductImg", Date(System.currentTimeMillis()).toString())
                writeViewModel.addTempPost(tempPost)
                ToastDefaultBlack.createToast(requireContext(), getString(R.string.write_temp_save_success_meesage))?.show()
            }
        }
        return true
    }

    private fun showBackButton() {
        (activity as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        this.setHasOptionsMenu(true)
    }

    private fun activeButtonSave() {
        binding.fabWriteToComplete.isEnabled = true
        binding.fabWriteToComplete.backgroundTintList = requireContext().resources.getColorStateList(R.color.moomool_pink_ff227c, context?.theme)
    }
    private fun deactiveButtonSave() {
        binding.fabWriteToComplete.isEnabled = false
        binding.fabWriteToComplete.backgroundTintList = requireContext().resources.getColorStateList(R.color.bluegray500_878C96, context?.theme)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}