package com.moo.mool.view.mypage

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import com.moo.mool.R
import com.moo.mool.database.SharedManager
import com.moo.mool.databinding.FragmentMyPageBinding
import com.moo.mool.view.feed.Feed
import com.moo.mool.view.mypage.adapter.MyPageFeedAdapter
import com.moo.mool.view.mypage.viewmodel.MyPageViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyPageFragment : Fragment() {
    private var _binding: FragmentMyPageBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val myPageViewModel by viewModels<MyPageViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyPageBinding.inflate(inflater, container, false)
        setSettingClickListener()
        setTvProfileUpdateClickListener()
        setMyPageNickNameEmailText()
        setBtnMyWriteClickListener()
        setBtnMyParticipateClickListener()
        setCategoryCollect()
        setRvMyPageAdapter()
        setMyPageFeedListCollect()
        //setNetworkErrorCollect()
        return binding.root
    }

    private fun setSettingClickListener() {
        binding.btnMyPageSetting.setOnClickListener {
            requireView().findNavController()
                .navigate(R.id.action_myPageFragment_to_settingFragment)
        }
    }

    private fun setTvProfileUpdateClickListener() {
        binding.tvMyPageProfileUpdate.setOnClickListener {
            requireView().findNavController()
                .navigate(R.id.action_myPageFragment_to_profileFragment)
        }
    }

    private fun setMyPageNickNameEmailText() {
        with(SharedManager(requireContext()).getCurrentUser()) {
            with(binding) {
                tvMyPageNickname.text = nickname
                tvMyPageEmail.text = email
            }
        }
    }

    private fun setBtnMyWriteClickListener() {
        binding.btnMyPageMyWrite.setOnClickListener {
            myPageViewModel.setCategory(0)
        }
    }

    private fun setBtnMyParticipateClickListener() {
        binding.btnMyPageMyParticipate.setOnClickListener {
            myPageViewModel.setCategory(1)
        }
    }

    private fun setCategoryCollect() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            myPageViewModel.category.collect { category ->
                setCategoryIfSelected(category)
                setTvContentEmptyText(category)
                myPageViewModel.requestMyPageFeedList(getRequestSorted(category))
            }
        }
    }

    private fun getRequestSorted(category: Int) = when (category) {
        0 -> getString(R.string.my_page_request_written)
        1 -> getString(R.string.my_page_request_voted)
        else -> throw IndexOutOfBoundsException()
    }

    private fun setCategoryIfSelected(category: Int) {
        with(binding) {
            when (category) {
                0 -> {
                    btnMyPageMyWrite.selectCategory()
                    btnMyPageMyParticipate.unSelectCategory()

                }
                1 -> {
                    btnMyPageMyWrite.unSelectCategory()
                    btnMyPageMyParticipate.selectCategory()
                }
            }
        }
    }

    private fun Button.selectCategory() {
        this.apply {
            backgroundTintList = ColorStateList.valueOf(getColor(R.color.bluegray100_F0F2F5))
            setTextColor(getColor(R.color.bluegray800_303540))
        }
    }

    private fun Button.unSelectCategory() {
        this.apply {
            backgroundTintList = ColorStateList.valueOf(getColor(R.color.bluegray700_4D535E))
            setTextColor(getColor(R.color.bluegray500_878C96))
        }
    }

    private fun getColor(color: Int) = requireContext().getColor(color)

    private fun setTvContentEmptyText(category: Int) {
        binding.tvMyPageContentEmpty.text = when (category) {
            0 -> getString(R.string.my_page_my_write_empty)
            1 -> getString(R.string.my_page_my_participate_empty)
            else -> throw IndexOutOfBoundsException()
        }
    }

    private fun setRvMyPageAdapter() {
        binding.rvMyPageContent.adapter = MyPageFeedAdapter()
    }

    private fun setMyPageFeedListCollect() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                myPageViewModel.myPageFeedList.collect { feedList ->
                    feedList?.let {
                        submitFeedList(feedList)
                    }
                }
            }
        }
    }

    private fun submitFeedList(feedList: List<Feed>) {
        with(binding.rvMyPageContent.adapter as MyPageFeedAdapter) {
            submitList(feedList) {
                binding.rvMyPageContent.scrollToPosition(0)
                setContentVisibility(feedList.size)
            }
        }
    }

    private fun setContentVisibility(size: Int) {
        with(binding) {
            rvMyPageContent.visibility = when (size) {
                0 -> View.INVISIBLE
                else -> View.VISIBLE
            }
            tvMyPageContentEmpty.visibility = when (size) {
                0 -> View.VISIBLE
                else -> View.INVISIBLE
            }
        }
    }

    private fun setNetworkErrorCollect() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                with(myPageViewModel) {
                    networkError.collect { networkError ->
                        if (networkError) {
                            requireView().findNavController()
                                .navigate(R.id.action_myPageFragment_to_networkErrorFragment)
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
