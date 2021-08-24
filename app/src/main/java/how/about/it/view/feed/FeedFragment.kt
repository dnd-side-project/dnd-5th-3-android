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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import how.about.it.R
import how.about.it.databinding.FragmentFeedBinding
import how.about.it.network.RequestToServer
import how.about.it.network.feed.FeedServiceImpl
import how.about.it.view.feed.adapter.FeedBottomAdapter
import how.about.it.view.feed.adapter.FeedTopAdapter
import how.about.it.view.feed.repository.FeedRepository
import how.about.it.view.feed.viewmodel.FeedViewModel
import how.about.it.view.feed.viewmodel.FeedViewModelFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FeedFragment : Fragment(), PopupMenu.OnMenuItemClickListener {
    private var _binding: FragmentFeedBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val feedViewModel by viewModels<FeedViewModel> {
        FeedViewModelFactory(
            FeedRepository(
                FeedServiceImpl(RequestToServer.feedInterface)
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedBinding.inflate(inflater, container, false)
        RequestToServer.initAccessToken(requireContext())
        setSettingClickListener()
        setNotificationClickListener()
        setFabWriteClickListener()
        setSwipeRefreshListener()
        setTvFeedToggleClickListener()
        setToggleCategoryCollect()
        setRvFeedTopAdapter()
        setRvFeedTopSnapHelper()
        setRvFeedBottomAdapter()
        setFeedTopListCollect()
        setFeedBottomListCollect()
        feedViewModel.requestTopFeedList()
        return binding.root
    }

    private fun setSettingClickListener() {
        binding.btnFeedSetting.setOnClickListener {
            requireView().findNavController()
                .navigate(R.id.action_feedFragment_to_settingFragment)
        }
    }

    private fun setNotificationClickListener() {
        binding.btnFeedNotification.setOnClickListener {
            requireView().findNavController()
                .navigate(R.id.action_feedFragment_to_notificationFragment)
        }
    }

    private fun setFabWriteClickListener() {
        binding.fabFeedToWrite.setOnClickListener {
            requireView().findNavController()
                .navigate(R.id.action_feedFragment_to_writeFragment)
        }
    }

    private fun setSwipeRefreshListener() {
        with(binding.layoutSwipeFeed) {
            setOnRefreshListener {
                feedViewModel.requestTopFeedList()
                isRefreshing = false
            }
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

    private fun setToggleCategoryCollect() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                feedViewModel.toggleCategory.collect { category ->
                    setTvToggleText(category)
                    feedViewModel.requestBottomFeedList(getToggleText(category))
                }
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

    private fun getToggleText(category: Int) = when (category) {
        0 -> getString(R.string.feed_request_new)
        1 -> getString(R.string.feed_request_popular)
        2 -> getString(R.string.feed_request_coming_end)
        3 -> getString(R.string.feed_request_end)
        else -> throw IndexOutOfBoundsException()
    }

    private fun setRvFeedTopAdapter() {
        binding.rvFeedTop.adapter = FeedTopAdapter()
    }

    private fun setRvFeedTopSnapHelper() {
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.rvFeedTop)

        binding.rvFeedTop.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            val layoutManager = requireNotNull(binding.rvFeedTop.layoutManager)
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val view = requireNotNull(snapHelper.findSnapView(layoutManager)).apply {
                    alpha = 1f
                }
                with(layoutManager) {
                    val position = getPosition(view)
                    findViewByPosition(position - 1)?.alpha = 0.5f
                    findViewByPosition(position + 1)?.alpha = 0.5f
                }
            }
        })
    }

    private fun setRvFeedBottomAdapter() {
        binding.rvFeedBottom.adapter = FeedBottomAdapter()
    }

    private fun setFeedTopListCollect() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                feedViewModel.feedTopList.collect { feedList ->
                    feedList?.let {
                        with(binding.rvFeedTop.adapter as FeedTopAdapter) {
                            submitList(feedList) {
                                binding.rvFeedTop.scrollToPosition(0)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setFeedBottomListCollect() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                feedViewModel.feedBottomList.collect { feedList ->
                    feedList?.let {
                        with(binding.rvFeedBottom.adapter as FeedBottomAdapter) {
                            submitList(feedList)
                        }
                    }
                }
            }
        }
    }

    private fun setNetworkErrorCollect() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                feedViewModel.networkError.collect {
                    //TODO networkerrorpage
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
