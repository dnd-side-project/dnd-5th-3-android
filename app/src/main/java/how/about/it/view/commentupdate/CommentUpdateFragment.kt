package how.about.it.view.commentupdate

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import how.about.it.R
import how.about.it.databinding.FragmentCommentUpdateBinding
import how.about.it.network.RequestToServer
import how.about.it.network.comment.CommentServiceImpl
import how.about.it.view.comment.repository.CommentRepository
import how.about.it.view.comment.viewmodel.CommentViewModel
import how.about.it.view.comment.viewmodel.CommentViewModelFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CommentUpdateFragment : Fragment() {
    private var _binding: FragmentCommentUpdateBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val commentViewModel by viewModels<CommentViewModel>() {
        CommentViewModelFactory(
            CommentRepository(
                CommentServiceImpl(RequestToServer.commentInterface)
            )
        )
    }
    private val args by navArgs<CommentUpdateFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommentUpdateBinding.inflate(inflater, container, false)
        setCommentUpdateBackClickListener()
        setEtTextChangedListener()
        setEtCommentUpdateText()
        setTvUpdateRequestClickListener()
        setIsUpdatedCollect()
        return binding.root
    }

    private fun setCommentUpdateBackClickListener() {
        binding.btnCommentUpdateBack.setOnClickListener {
            requireView().findNavController()
                .popBackStack()
        }
    }

    private fun setEtCommentUpdateText() {
        binding.etCommentUpdate.setText(args.content)
    }

    private fun setEtTextChangedListener() {
        with(binding.etCommentUpdate) {
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    setTvUpdateRequestColor(text.length)
                    setTvUpdateRequestClickable(text.length)
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })
        }
    }

    private fun setTvUpdateRequestColor(length: Int) {
        with(binding.tvCommentUpdateRequest) {
            when (length) {
                0 -> setTextColor(requireContext().getColor(R.color.bluegray50_F9FAFC))
                else -> setTextColor(requireContext().getColor(R.color.moomool_pink_ff227c))
            }
        }
    }

    private fun setTvUpdateRequestClickable(length: Int) {
        binding.tvCommentUpdateRequest.isClickable = when (length) {
            0 -> false
            else -> true
        }
    }

    private fun setTvUpdateRequestClickListener() {
        binding.tvCommentUpdateRequest.setOnClickListener {
            commentViewModel.requestCommentUpdate(args.id, binding.etCommentUpdate.text.toString())
        }
    }

    private fun setIsUpdatedCollect() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                commentViewModel.isUpdated.collect { isUpdated ->
                    if (isUpdated) {
                        requireView().findNavController()
                            .popBackStack()
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