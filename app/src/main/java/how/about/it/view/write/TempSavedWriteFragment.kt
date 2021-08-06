package how.about.it.view.write

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import how.about.it.R
import how.about.it.databinding.FragmentTempListWriteBinding
import how.about.it.databinding.FragmentTempSavedWriteBinding
import how.about.it.view.main.MainActivity

class TempSavedWriteFragment : Fragment() {
    private var _binding: FragmentTempSavedWriteBinding? = null
    private val binding get() = requireNotNull(_binding)

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
        when(item.itemId) {
            R.id.action_delete_temp_post -> {
                Toast.makeText(requireContext(), "불러오기 버튼 클릭", Toast.LENGTH_LONG).show()
                return super.onOptionsItemSelected(item)
            }
            else -> return super.onOptionsItemSelected(item)
        }
        (activity as MainActivity).onBackPressed()
        return true
    }
    private fun showBackButton() {
        (activity as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        this.setHasOptionsMenu(true)
    }
}