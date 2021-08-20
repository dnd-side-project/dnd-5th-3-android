package how.about.it.view.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import how.about.it.R
import how.about.it.databinding.FragmentSettingNotificationBinding
import how.about.it.view.main.MainActivity

class SettingNotificationFragment : Fragment() {
    private var _binding: FragmentSettingNotificationBinding? = null
    private val binding get() = requireNotNull(_binding)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingNotificationBinding.inflate(layoutInflater)
        setToolbarDetail()

        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        (activity as MainActivity).onBackPressed()
        return true
    }
    private fun showBackButton() {
        (activity as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        this.setHasOptionsMenu(true)
    }
    private fun setToolbarDetail() {
        binding.toolbarSettingNotificationBoard.tvToolbarTitle.setText(R.string.setting_notification)
        (activity as MainActivity).setSupportActionBar(binding.toolbarSettingNotificationBoard.toolbarBoard)
        (activity as MainActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        showBackButton()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}