package how.about.it.view.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import how.about.it.R
import how.about.it.databinding.FragmentFeedBinding

class FeedFragment : Fragment() {
    private var _binding: FragmentFeedBinding? = null
    private val binding get() = requireNotNull(_binding)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedBinding.inflate(inflater, container, false)
        setFabWriteClickListener()
        return binding.root
    }

    private fun setFabWriteClickListener() {
        binding.fabFeedToWrite.setOnClickListener {
            requireView().findNavController().navigate(R.id.action_feedFragment_to_writeFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}