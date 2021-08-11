package how.about.it.view.comment

import android.animation.Animator
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
import how.about.it.util.FloatingAnimationUtil
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
        setLayoutCommentClickListener(getLayoutReactionList())
        setLottieAnimationListener()
        return binding.root
    }

    private fun setCommentBackClickListener() {
        binding.btnCommentBack.setOnClickListener {
            requireView().findNavController()
                .popBackStack()
        }
    }

    private fun setTvCommentTimeText() {
        binding.layoutComment.tvCommentTime.text =
            TimeChangerUtil.timeChange(requireContext(), "2021-08-09T15:35:00")
    }

    private fun setBtnCommentMoreClickListener() {
        binding.layoutComment.btnCommentMore.setOnClickListener {
            PopupMenu(
                ContextThemeWrapper(
                    requireContext(),
                    R.style.feed_toggle_popup_menu
                ),
                binding.layoutComment.btnCommentMore
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
                        setLottieCancelAnimation()
                        setLayoutAlpha(1f)
                        setFabCommentReactionCloseBackground()
                        setLayoutCommentCloseAnimation(getLayoutReactionList())
                        delay(300)
                        setLayoutCommentCloseInvisible(getLayoutReactionList())

                    }
                    1 -> {
                        setLayoutAlpha(0.2f)
                        setFabCommentReactionOpenBackground()
                        setLayoutCommentColorVisible(getLayoutReactionList())
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
                FloatingAnimationUtil.setAnimation(this, dpToPx(-(76 * (index + 1)).toFloat()))
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
            FloatingAnimationUtil.setAnimation(layout, 0f)
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
                    FloatingAnimationUtil.setAnimation(list[index], 0f)
                    list[index].visibility = View.INVISIBLE
                } else {
                    FloatingAnimationUtil.setAnimation(list[index], dpToPx(-76f))
                }
            }
            setLottiePlayAnimation(selected)
        }
    }

    private fun setLottieAnimationListener() {
        binding.imgReactionLottie.apply {
            addAnimatorListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(p0: Animator?) {
                }

                override fun onAnimationEnd(p0: Animator?) {
                    visibility = View.INVISIBLE
                    commentViewModel.setOpenReact()
                }

                override fun onAnimationCancel(p0: Animator?) {
                }

                override fun onAnimationStart(p0: Animator?) {
                }
            })
        }
    }

    private fun setLottiePlayAnimation(selected: Int) {
        binding.imgReactionLottie.apply {
            setAnimation(getLottieRaw(selected))
            visibility = View.VISIBLE
            playAnimation()
        }
    }

    private fun setLottieCancelAnimation() {
        binding.imgReactionLottie.apply {
            visibility = View.INVISIBLE
            cancelAnimation()
        }
    }

    private fun getLottieRaw(selected: Int) = when (selected) {
        0 -> R.raw.lottie_reaction_brown
        1 -> R.raw.lottie_reaction_blue
        2 -> R.raw.lottie_reaction_green
        3 -> R.raw.lottie_reaction_red
        4 -> R.raw.lottie_reaction_yellow
        else -> throw IndexOutOfBoundsException()
    }

    private fun getLayoutReactionList() =
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
