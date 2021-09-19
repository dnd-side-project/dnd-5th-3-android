package com.moo.mool.view.vote

import android.animation.Animator
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.PopupMenu
import androidx.appcompat.view.ContextThemeWrapper
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.moo.mool.R
import com.moo.mool.databinding.FragmentVoteBinding
import com.moo.mool.network.RequestToServer
import com.moo.mool.util.*
import com.moo.mool.view.ToastDefaultBlack
import com.moo.mool.view.comment.model.Comment
import com.moo.mool.view.vote.adapter.VoteCommentAdapter
import com.moo.mool.view.vote.viewmodel.VoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class VoteFragment : Fragment(), PopupMenu.OnMenuItemClickListener {
    private var _binding: FragmentVoteBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val voteViewModel by viewModels<VoteViewModel>()
    private val args by navArgs<VoteFragmentArgs>()
    private var timer: CountDownTimer? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVoteBinding.inflate(inflater, container, false)
        RequestToServer.initAccessToken(requireContext())
        setVoteBackClickListener()
        setBtnVoteMoreClickListener()
        setImageVoteItemClipToOutLine()
        setProgressTouchListener()
        setRequestDeleteCollect()
        setFeedDetailCollect()
        setVoteCommentAdapter()
        setFeedDetailCommentCollect()
        setEtVoteCommentFocusListener()
        setEtVoteCommentListener()
        setTvVoteCommentPostClickListener()
        setLayoutClickListener()
        setRequestPostCommentCollect()
        setFabVoteClickListener()
        setOpenVoteCollect()
        setLayoutVoteClickListener(getLayoutVoteList())
        setRequestVoteCollect()
        setLottieAnimationListener()
        voteViewModel.closeVote()
        voteViewModel.requestVoteFeedDetail(args.id)
        voteViewModel.requestVoteFeedComment(args.id)
        return binding.root
    }

    private fun setVoteBackClickListener() {
        binding.btnVoteBack.setOnClickListener {
            popBackStack()
        }
    }

    private fun setBtnVoteMoreClickListener() {
        binding.btnVoteMore.setOnClickListener {
            PopupMenu(
                ContextThemeWrapper(
                    requireContext(),
                    R.style.feed_toggle_popup_menu
                ),
                binding.btnVoteMore
            ).apply {
                setOnMenuItemClickListener(this@VoteFragment)
                inflate(R.menu.menu_feed_delete)
                show()
            }
        }
    }

    override fun onMenuItemClick(item: MenuItem) = when (item.itemId) {
        R.id.feed_delete -> {
            DeleteDialogUtil.showDeleteDialog(requireContext(), false) {
                voteViewModel.requestVoteFeedDelete(args.id)
            }
            true
        }
        else -> false
    }

    private fun setImageVoteItemClipToOutLine() {
        binding.imgVoteItem.clipToOutline = true
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setProgressTouchListener() {
        binding.progressVoteItem.setOnTouchListener { _, _ -> true }
    }


    private fun setRequestDeleteCollect() {
        repeatOnLifecycle {
            voteViewModel.requestDelete.collect { requestDelete ->
                when (requestDelete) {
                    true -> popBackStack()
                }
            }
        }
    }

    private fun setFeedDetailCollect() {
        repeatOnLifecycle {
            voteViewModel.feedDetail.collect { feedDetail ->
                feedDetail?.let {
                    timer = setCountDownTimer(feedDetail.voteDeadline).start()
                    with(binding) {
                        feed = feedDetail
                        remainTime = TimeChangerUtil.getRemainTime(feedDetail.voteDeadline)
                        isMine = getIsMineUtil(requireContext(), feedDetail.name)
                        executePendingBindings()
                    }
                    setImageVoteCompleteImage(feedDetail.currentMemberVoteResult)
                }
            }
        }
    }

    private fun setImageVoteCompleteImage(currentMemberVoteResult: String) {
        with(binding.imgVoteSelected) {
            when (currentMemberVoteResult) {
                "PERMIT" -> setImageResource(R.drawable.ic_vote_complete_agree)
                "REJECT" -> setImageResource(R.drawable.ic_vote_complete_disagree)
            }
        }
    }

    private fun setCountDownTimer(deadLine: String) =
        object : CountDownTimer(
            (TimeChangerUtil.getDeadLine(deadLine)),
            1000
        ) {
            override fun onTick(millisUntilFinished: Long) {
                _binding?.let { binding ->
                    binding.tvVoteItemTime.text =
                        TimeChangerUtil.getDeadLineString(millisUntilFinished)
                }
            }

            override fun onFinish() {
                _binding?.let { binding ->
                    binding.tvVoteItemTime.text = TimeChangerUtil.getDeadLineString(0)
                }
            }
        }

    private fun cancelCountDownTimer() {
        timer?.let {
            requireNotNull(timer).cancel()
            timer = null
        }
    }

    private fun setVoteCommentAdapter() {
        binding.rvVoteComment.adapter = VoteCommentAdapter(voteViewModel)
    }

    private fun setFeedDetailCommentCollect() {
        repeatOnLifecycle {
            voteViewModel.feedDetailComments.collect { feedDetailComments ->
                feedDetailComments?.let {
                    submitCommentList(feedDetailComments)
                    setTvVoteCommentCountText(feedDetailComments.size)
                }
            }
        }
    }

    private fun submitCommentList(feedDetailComments: List<Comment>) {
        with(binding.rvVoteComment.adapter as VoteCommentAdapter) {
            submitList(feedDetailComments)
        }
    }

    private fun setTvVoteCommentCountText(size: Int) {
        binding.tvVoteCommentCount.text = String.format(
            getString(R.string.vote_comments_count),
            size
        )
    }

    private fun setEtVoteCommentFocusListener() {
        binding.etVoteComment.setOnFocusChangeListener { _, isFocused ->
            when (isFocused) {
                true -> {
                    if (binding.feed?.isVoted == false) {
                        voteViewModel.openVote()
                    }
                }
                false -> voteViewModel.closeVote()
            }
        }
    }

    private fun setEtVoteCommentListener() {
        with(binding.etVoteComment) {
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    if (text.length > 500) {
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

    private fun setTvVoteCommentPostClickListener() {
        binding.tvVoteCommentPost.setOnClickListener {
            if (binding.etVoteComment.text.toString().isNotBlank()) {
                HideKeyBoardUtil.hide(requireContext(), binding.etVoteComment)
                voteViewModel.requestPostComment(
                    args.id,
                    binding.etVoteComment.text.toString()
                )
            }
        }
    }

    private fun setLayoutClickListener() {
        with(binding) {
            layoutVote.setOnClickListener {
                HideKeyBoardUtil.hide(requireContext(), etVoteComment)
            }
        }
    }

    private fun setRequestPostCommentCollect() {
        repeatOnLifecycle {
            with(voteViewModel) {
                requestPostComment.collect { isPosted ->
                    if (isPosted) {
                        resetIsPosted()
                        requestVoteFeedComment(args.id)
                        delay(500)
                        afterPostedScroll()
                        resetEtVoteCommentText()
                    }
                }
            }
        }
    }

    private fun afterPostedScroll() {
        binding.layoutScrollVote.fullScroll(View.FOCUS_DOWN)
    }

    private fun resetEtVoteCommentText() {
        binding.etVoteComment.setText("")
    }

    private fun setFabVoteClickListener() {
        binding.fabVote.setOnClickListener {
            voteViewModel.setOpenVote()
        }
    }

    private fun setOpenVoteCollect() {
        repeatOnLifecycle {
            voteViewModel.openVote.collect { isOpen ->
                binding.isOpen = isOpen
                when (isOpen) {
                    "CLOSE" -> {
                        setLayoutVoteCloseAnimation(getLayoutVoteList())
                        delay(300)
                        setLayoutVoteCloseInvisible(getLayoutVoteList())
                    }
                    "OPEN" -> {
                        setLayoutVoteColorVisible(getLayoutVoteList())
                    }
                }
            }
        }
    }

    private fun setLayoutVoteCloseAnimation(list: List<ConstraintLayout>) {
        list.forEach { layout ->
            FloatingAnimationUtil.setAnimation(layout, 0f)
        }
    }

    private fun setLayoutVoteCloseInvisible(list: List<ConstraintLayout>) {
        list.forEach { layout ->
            layout.visibility = View.INVISIBLE
        }
    }

    private fun setLayoutVoteColorVisible(list: List<ConstraintLayout>) {
        list.indices.forEach { index ->
            with(list[index]) {
                visibility = View.VISIBLE
                FloatingAnimationUtil.setAnimation(this, (-(76 * (index + 1)).toFloat()))
            }
        }
    }

    private fun setLayoutVoteClickListener(list: List<ConstraintLayout>) {
        list.indices.forEach { index ->
            list[index].setOnClickListener {
                voteViewModel.requestVote(args.id, getVoteResponse(index))
            }
        }
    }

    private fun getVoteResponse(index: Int) = when (index) {
        0 -> "REJECT"
        1 -> "PERMIT"
        else -> throw IndexOutOfBoundsException()
    }

    private fun setRequestVoteCollect() {
        repeatOnLifecycle {
            with(voteViewModel) {
                requestVote.collect { voted ->
                    if (voted.isNotEmpty()) {
                        requestVoteFeedComment(args.id)
                        setLayoutCommentSelectClose(voted, getLayoutVoteList())
                    }
                }
            }
        }
    }

    private fun setLayoutCommentSelectClose(voted: String, list: List<ConstraintLayout>) {
        list.forEach { layout ->
            FloatingAnimationUtil.setAnimation(layout, 0f)
            layout.visibility = View.INVISIBLE
            setVoteCompleteAction(voted)
        }
    }

    private fun setVoteCompleteAction(voted: String) {
        with(binding.imgVoteComplete) {
            setAnimation(getLottieRaw(voted))
            visibility = View.VISIBLE
            playAnimation()
            setImageVoteCompleteImage(voted)
        }
    }

    private fun setLottieAnimationListener() {
        with(binding.imgVoteComplete) {
            addAnimatorListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(p0: Animator?) {
                }

                override fun onAnimationEnd(p0: Animator?) {
                    visibility = View.INVISIBLE
                    binding.fabVote.visibility = View.INVISIBLE
                    setVoteSelectedVisibility()
                    voteViewModel.closeVote()
                }

                override fun onAnimationCancel(p0: Animator?) {
                }

                override fun onAnimationStart(p0: Animator?) {
                }
            })
        }
    }

    private fun setVoteSelectedVisibility() {
        with(binding) {
            tvVoteSelected.visibility = View.VISIBLE
            imgVoteSelected.visibility = View.VISIBLE
        }
    }

    private fun getLottieRaw(voted: String) = when (voted) {
        "REJECT" -> R.raw.lottie_disagree
        "PERMIT" -> R.raw.lottie_agree
        else -> throw IndexOutOfBoundsException()
    }

    private fun getLayoutVoteList() =
        with(binding) {
            listOf(
                layoutVoteDisagree,
                layoutVoteAgree
            )
        }

    override fun onStop() {
        super.onStop()
        cancelCountDownTimer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
