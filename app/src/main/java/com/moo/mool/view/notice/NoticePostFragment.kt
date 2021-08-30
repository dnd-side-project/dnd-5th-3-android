package com.moo.mool.view.notice

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.moo.mool.R
import com.moo.mool.databinding.FragmentNoticePostBinding
import com.moo.mool.model.Notice
import com.moo.mool.view.main.MainActivity
import com.moo.mool.viewmodel.NoticeViewModel

class NoticePostFragment : Fragment() {

    private var _binding: FragmentNoticePostBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val noticeViewModel by activityViewModels<NoticeViewModel>()
    private lateinit var currentNoticeID : String
    private lateinit var currentNoticePost : Notice

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoticePostBinding.inflate(layoutInflater)
        setToolbarDetail()
        currentNoticeID = arguments?.getString("noticeId").toString()

        noticeViewModel.requestNoticePost(currentNoticeID)
        noticeViewModel.requestNoticePost(currentNoticeID).observe(viewLifecycleOwner, Observer {
            // 원하는 형식으로 보여주기 위하여 문자열 자른후, -문자 .로 변환
            var createdDate = it.createdDate.toString().substring(0, 10)
            it.createdDate = createdDate.replace("-", ".")
            currentNoticePost = it
            binding.notice = currentNoticePost
        })

        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        (activity as MainActivity).onBackPressed()
        return true
    }
    private fun showBackButton() {
        (activity as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back)
        this.setHasOptionsMenu(true)
    }
    private fun setToolbarDetail() {
        binding.toolbarNoticePostBoard.tvToolbarTitle.setText(R.string.notice)
        (activity as MainActivity).setSupportActionBar(binding.toolbarNoticePostBoard.toolbarBoard)
        (activity as MainActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        showBackButton()
    }
}