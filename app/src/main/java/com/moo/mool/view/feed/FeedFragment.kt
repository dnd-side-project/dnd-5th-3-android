package com.moo.mool.view.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.moo.mool.R
import com.moo.mool.databinding.FragmentFeedBinding
import com.moo.mool.network.RequestToServer
import com.moo.mool.view.feed.adapter.FeedBottomAdapter
import com.moo.mool.view.feed.adapter.FeedTopAdapter
import com.moo.mool.view.feed.viewmodel.FeedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FeedFragment : Fragment(), PopupMenu.OnMenuItemClickListener {
    private var _binding: FragmentFeedBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val feedViewModel by activityViewModels<FeedViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedBinding.inflate(inflater, container, false)
        RequestToServer.initAccessToken(requireContext())
        setFabWriteClickListener()
        setSwipeRefreshListener()
        setTvFeedToggleClickListener()
        setToggleCategoryCollect()
        setRvFeedTopAdapter()
        setRvFeedTopSnapHelper()
        setRvFeedBottomAdapter()
        setFeedTopListCollect()
        setFeedBottomListCollect()
        //setNetworkErrorCollect()
        return binding.root
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
                with(feedViewModel) {
                    networkError.collect { networkError ->
                        if (networkError) {
                            requireView().findNavController()
                                .navigate(R.id.action_feedFragment_to_networkErrorFragment)
                            resetNetworkError()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
