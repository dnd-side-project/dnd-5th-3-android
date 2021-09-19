package com.moo.mool.view.comment

import android.animation.Animator
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.appcompat.view.ContextThemeWrapper
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.moo.mool.R
import com.moo.mool.databinding.FragmentCommentBinding
import com.moo.mool.util.*
import com.moo.mool.view.ToastDefaultBlack
import com.moo.mool.view.comment.adapter.ReplyAdapter
import com.moo.mool.view.comment.model.Comment
import com.moo.mool.view.comment.viewmodel.CommentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class CommentFragment : Fragment(), PopupMenu.OnMenuItemClickListener {
    private var binding by autoCleared<FragmentCommentBinding>()
    private val commentViewModel by viewModels<CommentViewModel>()
    private val args by navArgs<CommentFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCommentBinding.inflate(inflater, container, false)
        binding.commentViewModel = commentViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        setCommentBackClickListener()
        setLayoutCommentClickListener()
        setRvReCommentAdapter()
        setReplyListCollect()
        setEmojiListCollect()
        setEmptyEmojiCollect()
        setOpenEmojiCollect()
        setLayoutCommentClickListener(getLayoutReactionList())
        setLottieAnimationListener()
        setBtnCommentMoreClickListener()
        setEtReplyTextChangedListener()
        setTvReplyPostClickListener()
        setIsPostedCollect()
        commentViewModel.initOpenEmoji(args.openEmoji)
        commentViewModel.requestGetReply(args.id)
        return binding.root
    }

    private fun setCommentBackClickListener() {
        binding.btnCommentBack.setOnClickListener {
            popBackStack()
        }
    }

    private fun setLayoutCommentClickListener() {
        with(binding) {
            layoutCommentBackground.setOnClickListener {
                HideKeyBoardUtil.hide(requireContext(), etReply)
            }
        }
    }

    private fun setRvReCommentAdapter() {
        binding.rvReComment.adapter = ReplyAdapter(commentViewModel)
    }

    private fun setReplyListCollect() {
        repeatOnLifecycle {
            commentViewModel.replyList.collect { replyList ->
                replyList?.let {
                    setLayoutCommentBinding(replyList[0])
                    submitCommentList(replyList.subList(1, replyList.size))
                }
            }
        }
    }

    private fun setLayoutCommentBinding(mainComment: Comment) {
        with(binding) {
            comment = mainComment
            isMine = getIsMineUtil(requireContext(), mainComment.writerName)
            executePendingBindings()
        }
    }

    private fun submitCommentList(replyList: List<Comment>) {
        with(binding.rvReComment.adapter as ReplyAdapter) {
            submitList(replyList) {
                binding.rvReComment.scrollToPosition(itemCount - 1)
            }
        }
    }

    private fun setEmojiListCollect() {
        repeatOnLifecycle {
            commentViewModel.emojiList.collect { emojiList ->
                binding.emojiList = emojiList
                commentViewModel.setEmptyCommentEmojiVisibility()
            }
        }
    }

    private fun setEmptyEmojiCollect() {
        repeatOnLifecycle {
            commentViewModel.emptyEmoji.collect { isEmpty ->
                setBtnReactionEmptyVisibility(isEmpty)
            }
        }
    }

    private fun setBtnReactionEmptyVisibility(isEmpty: Int) {
        binding.btnEmojiEmpty.visibility = when (isEmpty) {
            0 -> View.VISIBLE
            else -> View.GONE
        }
    }

    private fun setOpenEmojiCollect() {
        repeatOnLifecycle {
            commentViewModel.openEmoji.collect { isOpen ->
                binding.isOpen = isOpen
                when (isOpen) {
                    "CLOSE" -> {
                        setLottieCancelAnimation()
                        setLayoutCommentEmojiClose(getLayoutReactionList())
                        delay(300)
                        setLayoutCommentEmojiInvisible(getLayoutReactionList())
                    }
                    "OPEN" -> {
                        setLayoutCommentEmojiVisible(getLayoutReactionList())
                    }
                }
            }
        }
    }

    private fun setLayoutCommentEmojiVisible(list: List<ConstraintLayout>) {
        list.indices.forEach { index ->
            with(list[index]) {
                visibility = View.VISIBLE
                FloatingAnimationUtil.setAnimation(this, -(76 * (index + 1)).toFloat())
            }
        }
    }

    private fun setLayoutCommentEmojiClose(list: List<ConstraintLayout>) {
        list.forEach { layout ->
            FloatingAnimationUtil.setAnimation(layout, 0f)
        }
    }

    private fun setLayoutCommentEmojiInvisible(list: List<ConstraintLayout>) {
        list.forEach { layout ->
            layout.visibility = View.INVISIBLE
        }
    }

    private fun setLayoutCommentClickListener(list: List<ConstraintLayout>) {
        list.indices.forEach { index ->
            list[index].setOnClickListener {
                commentViewModel.requestEmoji(index, args.id)
                setLayoutCommentSelect(index, list)
            }
        }
    }

    private fun setLayoutCommentSelect(selected: Int, list: List<ConstraintLayout>) {
        list.indices.forEach { index ->
            when (selected) {
                index -> {
                    FloatingAnimationUtil.setAnimation(list[index], -76f)
                }
                else -> {
                    FloatingAnimationUtil.setAnimation(list[index], 0f)
                    list[index].visibility = View.INVISIBLE
                }
            }
        }
        setLottiePlayAnimation(selected)
    }

    private fun setLottieAnimationListener() {
        with(binding.imgEmojiLottie) {
            addAnimatorListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(p0: Animator?) {
                }

                override fun onAnimationEnd(p0: Animator?) {
                    visibility = View.INVISIBLE
                    commentViewModel.setOpenOrNotOpenReact()
                }

                override fun onAnimationCancel(p0: Animator?) {
                }

                override fun onAnimationStart(p0: Animator?) {
                }
            })
        }
    }

    private fun setLottiePlayAnimation(selected: Int) {
        with(binding.imgEmojiLottie) {
            setAnimation(getLottieRaw(selected))
            visibility = View.VISIBLE
            playAnimation()
        }
    }

    private fun setLottieCancelAnimation() {
        with(binding.imgEmojiLottie) {
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

    override fun onMenuItemClick(item: MenuItem) = when (item.itemId) {
        R.id.menu_comment_update -> {
            navigateWithData(
                CommentFragmentDirections.actionCommentFragmentToCommentUpdateFragment(
                    args.id,
                    binding.tvCommentContent.text.toString()
                )
            )
            true
        }
        R.id.menu_comment_delete -> {
            DeleteDialogUtil.showDeleteDialog(requireContext(), true) {
                commentViewModel.requestDeleteComment(args.id)
            }
            true
        }
        else -> false
    }

    private fun setEtReplyTextChangedListener() {
        with(binding.etReply) {
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    if (EdittextCount.getGraphemeCount(text.toString()) > 500) {
                        text.delete(text.length - 1, text.length)
                        ToastDefaultBlack.createToast(
                            requireContext(),
                            getString(R.string.comment_length_warning)
                        )?.show()
                    }
                }
            })
        }
    }

    private fun setTvReplyPostClickListener() {
        binding.tvReplyPost.setOnClickListener {
            if (binding.etReply.text.toString().isNotBlank()) {
                HideKeyBoardUtil.hide(requireContext(), binding.etReply)
                commentViewModel.requestPostReply(
                    args.id,
                    binding.etReply.text.toString()
                )
            }
        }
    }

    private fun setIsPostedCollect() {
        repeatOnLifecycle {
            with(commentViewModel) {
                isPosted.collect { isPosted ->
                    if (isPosted) {
                        requestGetReply(args.id)
                        resetEtReplyText()
                    }
                }
            }
        }
    }

    private fun resetEtReplyText() {
        binding.etReply.setText("")
    }
}
