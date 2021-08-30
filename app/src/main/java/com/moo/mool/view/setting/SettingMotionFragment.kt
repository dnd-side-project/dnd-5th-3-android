package com.moo.mool.view.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.moo.mool.R
import com.moo.mool.databinding.FragmentSettingMotionBinding
import com.moo.mool.view.main.MainActivity

class SettingMotionFragment : Fragment() {
    private var _binding: FragmentSettingMotionBinding? = null
    private val binding get() = requireNotNull(_binding)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingMotionBinding.inflate(layoutInflater)
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
        (activity as MainActivity).supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back)
        this.setHasOptionsMenu(true)
    }
    private fun setToolbarDetail() {
        binding.toolbarSettingMotionBoard.tvToolbarTitle.setText(R.string.setting_motion)
        (activity as MainActivity).setSupportActionBar(binding.toolbarSettingMotionBoard.toolbarBoard)
        (activity as MainActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        showBackButton()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}