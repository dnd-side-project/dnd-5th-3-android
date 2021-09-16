package com.moo.mool.view.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.moo.mool.R
import com.moo.mool.database.SharedManager
import com.moo.mool.databinding.FragmentMyPageBinding
import com.moo.mool.util.autoCleared
import com.moo.mool.util.navigate
import com.moo.mool.util.repeatOnLifecycle
import com.moo.mool.view.feed.model.Feed
import com.moo.mool.view.mypage.adapter.MyPageFeedAdapter
import com.moo.mool.view.mypage.viewmodel.MyPageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MyPageFragment : Fragment() {
    private var binding by autoCleared<FragmentMyPageBinding>()
    private val myPageViewModel by viewModels<MyPageViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyPageBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        setSettingClickListener()
        setTvProfileUpdateClickListener()
        setMyPageNickNameEmailText()
        setBtnMyWriteClickListener()
        setBtnMyParticipateClickListener()
        setCategoryCollect()
        setRvMyPageAdapter()
        setMyPageFeedListCollect()
        return binding.root
    }

    private fun setSettingClickListener() {
        binding.btnMyPageSetting.setOnClickListener {
            navigate(R.id.action_myPageFragment_to_settingFragment)
        }
    }

    private fun setTvProfileUpdateClickListener() {
        binding.tvMyPageProfileUpdate.setOnClickListener {
            navigate(R.id.action_myPageFragment_to_profileFragment)
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
        repeatOnLifecycle {
            myPageViewModel.category.collect { category ->
                binding.category = category
                myPageViewModel.requestMyPageFeedList(getRequestSorted(category))
            }
        }
    }

    private fun getRequestSorted(category: Int) = when (category) {
        0 -> getString(R.string.my_page_request_written)
        1 -> getString(R.string.my_page_request_voted)
        else -> throw IndexOutOfBoundsException()
    }

    private fun setRvMyPageAdapter() {
        binding.rvMyPageContent.adapter = MyPageFeedAdapter()
    }

    private fun setMyPageFeedListCollect() {
        repeatOnLifecycle {
            myPageViewModel.myPageFeedList.collect { feedList ->
                feedList?.let {
                    submitFeedList(feedList)
                }
            }
        }
    }

    private fun submitFeedList(feedList: List<Feed>) {
        with(binding.rvMyPageContent.adapter as MyPageFeedAdapter) {
            submitList(feedList) {
                with(binding) {
                    feedListSize = feedList.size
                    rvMyPageContent.scrollToPosition(0)
                }
            }
        }
    }
}
