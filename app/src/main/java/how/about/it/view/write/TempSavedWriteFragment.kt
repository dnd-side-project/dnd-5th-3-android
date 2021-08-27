package how.about.it.view.write

import android.app.AlertDialog
import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import how.about.it.R
import how.about.it.database.TempPost
import how.about.it.databinding.FragmentTempSavedWriteBinding
import how.about.it.view.main.MainActivity
import how.about.it.viewmodel.WriteViewModel
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
        writeViewModel = ViewModelProvider(this, WriteViewModel.Factory(requireActivity().application)).get(WriteViewModel::class.java)
        binding.writeViewModel = writeViewModel

        binding.toolbarWriteTempSavedBoard.tvToolbarTitle.text = "저장된 글"
        (activity as MainActivity).setSupportActionBar(binding.toolbarWriteTempSavedBoard.toolbarBoard)
        (activity as MainActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        (activity as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        showBackButton()

        binding.tvTempSavedWriteTitle.text = currentTempPost.title
        binding.tvTempSavedWriteDetail.text = currentTempPost.content
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.imgTempSavedWrite.setImageBitmap(currentTempPost.productImage?.toBitmap())
        }

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
                val bundle = Bundle()
                bundle.putParcelable("currentTempPost", currentTempPost)
                // 불러온 글을 WriteFragment로 데이터를 넘기면서 해당 임시 저장글 삭제하여 불러온 글이 임시저장 리스트에 남아있지 않도록 함
                writeViewModel.deleteTempPost(currentTempPost)
                requireView().findNavController().navigate(R.id.action_tempSavedWriteFragment_to_writeFragment, bundle)
                mAlertDialog.dismiss()
            }
            cancelButton.setOnClickListener {
                mAlertDialog.dismiss()
            }
        }
        return true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun String.toBitmap(): Bitmap?{
        Base64.getDecoder().decode(this).apply {
            return BitmapFactory.decodeByteArray(this,0,size)
        }
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