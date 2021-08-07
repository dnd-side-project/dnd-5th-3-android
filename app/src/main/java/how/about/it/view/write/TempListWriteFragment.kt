package how.about.it.view.write

import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import how.about.it.R
import how.about.it.databinding.FragmentTempListWriteBinding
import how.about.it.view.ToastDefaultBlack
import how.about.it.view.main.MainActivity

class TempListWriteFragment : Fragment() {
    private var _binding: FragmentTempListWriteBinding? = null
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
        _binding = FragmentTempListWriteBinding.inflate(layoutInflater)

        binding.toolbarWriteTempListBoard.tvToolbarTitle.text = "임시 저장"
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
            ToastDefaultBlack.createToast(requireContext(), "선택한 임시 저장글이 삭제되었어요.")?.show()
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