package how.about.it.view.mypage

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import how.about.it.R
import how.about.it.databinding.FragmentMyPageBinding
import how.about.it.view.mypage.viewmodel.MyPageViewModel
import kotlinx.coroutines.flow.collect

class MyPageFragment : Fragment() {
    private var _binding: FragmentMyPageBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val myPageViewModel by viewModels<MyPageViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyPageBinding.inflate(inflater, container, false)
        setSettingClickListener()
        setTvProfileUpdateClickListener()
        setBtnMyWriteClickListener()
        setBtnMyParticipateClickListener()
        setCategoryCollect()
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
            }
        }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
