package how.about.it.view.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import how.about.it.R
import how.about.it.databinding.FragmentOnBoardingBinding

class   OnBoardingFragment : Fragment() {
    private var _binding: FragmentOnBoardingBinding? = null
    private val binding get() = requireNotNull(_binding)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnBoardingBinding.inflate(inflater, container, false)
        setVpOnBoardingAdapter()
        setTabOnBoardingMediator()
        setTvOnBoardingSkipClickListener()
        return binding.root
    }

    private fun setVpOnBoardingAdapter() {
        binding.vpOnBoarding.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = 4
            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> OnBoardingFirstFragment()
                    1 -> OnBoardingSecondFragment()
                    2 -> OnBoardingThirdFragment()
                    3 -> OnBoardingFourthFragment()
                    else -> throw IndexOutOfBoundsException()
                }
            }
        }
    }

    private fun setTabOnBoardingMediator() {
        TabLayoutMediator(binding.tabOnBoarding, binding.vpOnBoarding) { _, _ -> }.attach()
    }

    private fun setTvOnBoardingSkipClickListener() {
        binding.tvOnBoardingSkip.setOnClickListener {
            requireView().findNavController()
                .navigate(R.id.action_onBoardingFragment_to_feedFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}