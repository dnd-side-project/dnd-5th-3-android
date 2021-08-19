package how.about.it.view.commentupdate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import how.about.it.databinding.FragmentCommentUpdateBinding
import how.about.it.network.RequestToServer
import how.about.it.network.comment.CommentServiceImpl
import how.about.it.view.comment.repository.CommentRepository
import how.about.it.view.comment.viewmodel.CommentViewModel
import how.about.it.view.comment.viewmodel.CommentViewModelFactory

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
        return binding.root
    }

    private fun setCommentUpdateBackClickListener() {
        binding.btnCommentUpdateBack.setOnClickListener {
            requireView().findNavController()
                .popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}