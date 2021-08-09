package how.about.it.view.comment

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.appcompat.view.ContextThemeWrapper
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import how.about.it.R
import how.about.it.databinding.FragmentCommentBinding
import how.about.it.util.TimeChangerUtil
import how.about.it.view.comment.viewmodel.CommentViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect

class CommentFragment : Fragment(), PopupMenu.OnMenuItemClickListener {
    private var _binding: FragmentCommentBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val commentViewModel by viewModels<CommentViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommentBinding.inflate(inflater, container, false)
        setCommentBackClickListener()
        setTvCommentTimeText()
        setBtnCommentMoreClickListener()
        setFabCommentReactClickListener()
        setOpenReactCollect()
        setLayoutCommentClickListener(layoutReactionList())
        return binding.root
    }

    private fun setCommentBackClickListener() {
        binding.btnCommentBack.setOnClickListener {
            requireView().findNavController()
                .popBackStack()
        }
    }

    private fun setTvCommentTimeText() {
        binding.tvCommentTime.text =
            TimeChangerUtil.timeChange(requireContext(), "2021-08-09T15:35:00")
    }

    private fun setBtnCommentMoreClickListener() {
        binding.btnCommentMore.setOnClickListener {
            PopupMenu(
                ContextThemeWrapper(
                    requireContext(),
                    R.style.feed_toggle_popup_menu
                ),
                binding.btnCommentMore
            ).apply {
                setOnMenuItemClickListener(this@CommentFragment)
                inflate(R.menu.menu_comment)
                show()
            }
        }
    }

    override fun onMenuItemClick(item: MenuItem) =
        when (item.itemId) {
            R.id.menu_comment_update -> {
                true
            }
            R.id.menu_comment_delete -> {
                true
            }
            else -> false
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
                        setLayoutAlpha(1f)
                        setFabCommentReactionCloseBackground()
                        setLayoutCommentCloseAnimation(layoutReactionList())
                        delay(300)
                        setLayoutCommentCloseInvisible(layoutReactionList())
                    }
                    1 -> {
                        setLayoutAlpha(0.2f)
                        setFabCommentReactionOpenBackground()
                        setLayoutCommentColorVisible(layoutReactionList())
                    }
                }
            }
        }
    }

    private fun setLayoutAlpha(alpha: Float) {
        binding.layoutCommentBackground.alpha = alpha
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
                setAnimation(this, dpToPx(-(76 * (index + 1)).toFloat()))
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
            setAnimation(layout, 0f)
        }
    }

    private fun setLayoutCommentCloseInvisible(list: List<ConstraintLayout>) {
        list.forEach { layout ->
            layout.visibility = View.INVISIBLE
        }
    }

    private fun setLayoutCommentClickListener(list: List<ConstraintLayout>) {
        list.indices.forEach { index ->
            list[index].setOnClickListener {
                setLayoutCommentSelectClose(index, list)
            }
        }
    }

    private fun setLayoutCommentSelectClose(selected: Int, list: List<ConstraintLayout>) {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            list.indices.forEach { index ->
                if (selected != index) {
                    setAnimation(list[index], 0f)
                    list[index].visibility = View.INVISIBLE
                } else {
                    setAnimation(list[index], dpToPx(-76f))
                }
            }
            //TODO LOTTIE
            delay(500)
            commentViewModel.setOpenReact()
        }
    }

    private fun setAnimation(layout: ConstraintLayout, value: Float) {
        ObjectAnimator.ofFloat(layout, "translationY", value).start()
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
