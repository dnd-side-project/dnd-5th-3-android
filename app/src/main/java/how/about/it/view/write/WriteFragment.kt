package how.about.it.view.write

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import how.about.it.R
import how.about.it.databinding.FragmentWriteBinding
import how.about.it.view.ToastDefaultBlack
import how.about.it.view.main.MainActivity

class WriteFragment : Fragment() {
    private var _binding: FragmentWriteBinding? = null
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
        _binding = FragmentWriteBinding.inflate(layoutInflater)

        binding.toolbarWriteBoard.tvToolbarTitle.text = "글쓰기"
        (activity as MainActivity).setSupportActionBar(binding.toolbarWriteBoard.toolbarBoard)
        (activity as MainActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        (activity as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        showBackButton()

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
                ToastDefaultBlack.createToast(requireContext(), "내용 작성 후 저장해주세요.")?.show()
            } else {
                ToastDefaultBlack.createToast(requireContext(), "작성 중인 글이 임시 저장되었어요.")?.show()
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