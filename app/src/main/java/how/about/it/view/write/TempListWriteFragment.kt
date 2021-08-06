package how.about.it.view.write

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import how.about.it.R
import how.about.it.databinding.FragmentTempListWriteBinding
import how.about.it.view.main.MainActivity

class TempListWriteFragment : Fragment() {
    private var _binding: FragmentTempListWriteBinding? = null
    private val binding get() = requireNotNull(_binding)

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
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_write_toolbar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        when(item.itemId) {
            R.id.action_delete_temp_post -> {
                Toast.makeText(requireContext(), "삭제 버튼 클릭", Toast.LENGTH_LONG).show()
                setHasOptionsMenu(false)

                // TODO : 선택 후 삭제 버튼 클릭하여 삭제한 뒤, 삭제 버튼 다시 나타내기
                setHasOptionsMenu(true)
                Toast.makeText(requireContext(), "선택한 임시 저장글이 삭제되었어요.", Toast.LENGTH_LONG).show()
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