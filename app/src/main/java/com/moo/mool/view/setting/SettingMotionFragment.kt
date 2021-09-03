package com.moo.mool.view.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.lifecycle.ViewModelProvider
import com.moo.mool.R
import com.moo.mool.databinding.FragmentSettingMotionBinding
import com.moo.mool.repository.SettingRepository
import com.moo.mool.view.main.MainActivity
import com.moo.mool.viewmodel.SettingViewModel
import com.moo.mool.viewmodel.SettingViewModelFactory

class SettingMotionFragment : Fragment() {
    private var _binding: FragmentSettingMotionBinding? = null
    private val binding get() = requireNotNull(_binding)
    private lateinit var settingViewModel : SettingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingMotionBinding.inflate(layoutInflater)
        settingViewModel = ViewModelProvider(this, SettingViewModelFactory(SettingRepository(requireContext()))).get(SettingViewModel::class.java)
        setToolbarDetail()
        
        binding.switchSettingMotion.isChecked = settingViewModel.isHideEmojiMotion=="true"
        binding.switchSettingMotion.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if(isChecked){
                    settingViewModel.setHideEmojiMotion()
                } else {
                    settingViewModel.setShowEmojiMotion()
                }
            }
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