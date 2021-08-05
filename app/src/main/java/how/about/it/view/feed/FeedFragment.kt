package how.about.it.view.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import how.about.it.R
import how.about.it.databinding.FragmentFeedBinding
import how.about.it.view.feed.viewmodel.FeedViewModel
import kotlinx.coroutines.flow.collect

class FeedFragment : Fragment(), PopupMenu.OnMenuItemClickListener {
    private var _binding: FragmentFeedBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val feedViewModel by viewModels<FeedViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedBinding.inflate(inflater, container, false)
        setNotificationClickListener()
        setFabWriteClickListener()
        setTvFeedToggleClickListener()
        setToggleCategoryObserve()
        return binding.root
    }

    private fun setNotificationClickListener() {
        binding.btnFeedNotification.setOnClickListener {
            requireView().findNavController()
                .navigate(R.id.action_feedFragment_to_notificationFragment)
        }
    }

    private fun setFabWriteClickListener() {
        binding.fabFeedToWrite.setOnClickListener {
            requireView().findNavController().navigate(R.id.action_feedFragment_to_writeFragment)
        }
    }

    private fun setTvFeedToggleClickListener() {
        binding.tvFeedToggle.setOnClickListener {
            PopupMenu(
                ContextThemeWrapper(
                    requireContext(),
                    R.style.feed_toggle_popup_menu
                ),
                binding.tvFeedToggle
            ).apply {
                setOnMenuItemClickListener(this@FeedFragment)
                inflate(R.menu.menu_feed_toggle)
                show()
            }
        }
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_feed_toggle_new -> {
                feedViewModel.setToggleCategory(0)
                true
            }
            R.id.menu_feed_toggle_popular -> {
                feedViewModel.setToggleCategory(1)
                true
            }
            R.id.menu_feed_toggle_coming_end -> {
                feedViewModel.setToggleCategory(2)
                true
            }
            R.id.menu_feed_toggle_end -> {
                feedViewModel.setToggleCategory(3)
                true
            }
            else -> false
        }
    }

    private fun setToggleCategoryObserve() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            feedViewModel.toggleCategory.collect { category ->
                setTvToggleText(category)
            }
        }
    }

    private fun setTvToggleText(category: Int) {
        binding.tvFeedToggle.text = when (category) {
            0 -> getString(R.string.feed_category_new)
            1 -> getString(R.string.feed_category_popular)
            2 -> getString(R.string.feed_category_coming_end)
            3 -> getString(R.string.feed_category_end)
            else -> throw IndexOutOfBoundsException()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
