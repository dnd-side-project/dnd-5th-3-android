package how.about.it.view.comment

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import how.about.it.R
import how.about.it.databinding.FragmentCommentBinding
import how.about.it.view.comment.viewmodel.CommentViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect

class CommentFragment : Fragment() {
    private var _binding: FragmentCommentBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val commentViewModel by viewModels<CommentViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommentBinding.inflate(inflater, container, false)
        setFabCommentReactClickListener()
        setOpenReactCollect()
        return binding.root
    }

    private fun setFabCommentReactClickListener() {
        binding.fabCommentReaction.setOnClickListener {
            commentViewModel.setOpenReact()
        }
    }

    private fun setOpenReactCollect() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            commentViewModel.openReact.collect { isOpen ->
                when (isOpen) {
                    0 -> {
                        setFabCommentReactionCloseBackground()
                        setLayoutCommentCloseAnimation(layoutReactionList())
                        delay(300)
                        setLayoutCommentCloseInvisible(layoutReactionList())
                    }
                    1 -> {
                        setFabCommentReactionOpenBackground()
                        setLayoutCommentColorVisible(layoutReactionList())
                    }
                }
            }
        }
    }

    private fun setFabCommentReactionCloseBackground() {
        with(binding.fabCommentReaction) {
            backgroundTintList = requireContext().getColorStateList(R.color.bluegray200_E1E3E8)
            setImageResource(R.drawable.ic_feed_logo)
        }
    }

    private fun setFabCommentReactionOpenBackground() {
        with(binding.fabCommentReaction) {
            backgroundTintList = requireContext().getColorStateList(R.color.bluegray700_4D535E)
            setImageResource(R.drawable.ic_x_sign)
        }
    }

    private fun setLayoutCommentColorVisible(list: List<ConstraintLayout>) {
        list.indices.forEach { index ->
            list[index].apply {
                visibility = View.VISIBLE
                ObjectAnimator.ofFloat(
                    this,
                    "translationY",
                    dpToPx(-(76 * (index + 1)).toFloat())
                ).start()
            }
        }
    }

    private fun dpToPx(dp: Float) =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            requireContext().resources.displayMetrics
        )

    private fun setLayoutCommentCloseAnimation(list: List<ConstraintLayout>) {
        list.forEach { layout ->
            ObjectAnimator.ofFloat(layout, "translationY", 0f).start()
        }
    }

    private fun setLayoutCommentCloseInvisible(list: List<ConstraintLayout>) {
        list.forEach { layout ->
            layout.visibility = View.INVISIBLE
        }
    }

    private fun layoutReactionList() =
        with(binding) {
            listOf(
                layoutCommentBrown,
                layoutCommentBlue,
                layoutCommentGreen,
                layoutCommentRed,
                layoutCommentYellow,
            )
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
