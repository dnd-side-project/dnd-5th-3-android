package how.about.it.view.vote

import android.animation.Animator
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.os.CountDownTimer
import android.util.TypedValue
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.view.ContextThemeWrapper
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import how.about.it.R
import how.about.it.databinding.FragmentVoteBinding
import how.about.it.network.RequestToServer
import how.about.it.network.vote.VoteServiceImpl
import how.about.it.util.FloatingAnimationUtil
import how.about.it.util.TimeChangerUtil
import how.about.it.view.vote.adapter.VoteCommentAdapter
import how.about.it.view.vote.repository.VoteRepository
import how.about.it.view.vote.viewmodel.VoteViewModel
import how.about.it.view.vote.viewmodel.VoteViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class VoteFragment : Fragment(), PopupMenu.OnMenuItemClickListener {
    private var _binding: FragmentVoteBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val voteViewModel by viewModels<VoteViewModel> {
        VoteViewModelFactory(
            VoteRepository(
                VoteServiceImpl(RequestToServer.voteInterface)
            )
        )
    }
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
        setProgressTouchListener()
        setLottieAnimationListener()
        setImageVoteItemClipToOutLine()
        setEtVoteCommentFocusListener()
        setEtVoteCommentEditorActionListener()
        setRequestPostCommentCollect()
        setRequestDeleteCollect()
        setFeedDetailCollect()
        setVoteCommentAdapter()
        setFeedDetailCommentCollect()
        setFabVoteClickListener()
        setOpenVoteCollect()
        setLayoutVoteClickListener(getLayoutVoteList())
        setRequestVoteCollect()
        voteViewModel.requestVoteFeedDetail(args.id)
        voteViewModel.requestVoteFeedComment(args.id)
        return binding.root
    }

    private fun setVoteBackClickListener() {
        binding.btnVoteBack.setOnClickListener {
            requireView().findNavController()
                .popBackStack()
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

    private fun setImageVoteItemClipToOutLine() {
        binding.imgVoteItem.clipToOutline = true
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setProgressTouchListener() {
        binding.progressVoteItem.setOnTouchListener { _, _ -> true }
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.feed_delete -> {
                requestFeedDeleteDialog()
                true
            }
            else -> false
        }
    }

    private fun requestFeedDeleteDialog() {
        val mDialogView =
            LayoutInflater.from(requireContext()).inflate(R.layout.dialog_default_confirm, null)
        val mBuilder = AlertDialog.Builder(requireContext())
            .setView(mDialogView)
        val mAlertDialog = mBuilder.show()

        mDialogView.findViewById<TextView>(R.id.tv_message_dialog_title).setText(R.string.delete)
        mDialogView.findViewById<TextView>(R.id.tv_message_dialog_description)
            .setText(R.string.vote_delete_check)

        mDialogView.findViewById<Button>(R.id.btn_dialog_confirm).setOnClickListener {
            voteViewModel.requestVoteDelete(args.id)
            mAlertDialog.dismiss()
        }
        mDialogView.findViewById<Button>(R.id.btn_dialog_cancel).setOnClickListener {
            mAlertDialog.dismiss()
        }
    }

    private fun setRequestDeleteCollect() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                voteViewModel.requestDelete.collect { requestDelete ->
                    if (requestDelete) {
                        requireView().findNavController().popBackStack()
                    }
                }
            }
        }
    }

    private fun setFeedDetailCollect() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                voteViewModel.feedDetail.collect { feedDetail ->
                    feedDetail?.let {
                        timer = setCountDownTimer(feedDetail.voteDeadline).start()
                        binding.apply {
                            feed = feedDetail
                            remainTime = TimeChangerUtil.getRemainTime(feedDetail.voteDeadline)
                            setTvFeedAgreeText(feedDetail)
                            setTvFeedDisAgreeText(feedDetail)
                            setSeekBarProgress(feedDetail)
                            setImageVoteCompleteImage(feedDetail.currentMemberVoteResult)
                        }
                    }
                }
            }
        }
    }

    private fun setImageVoteCompleteImage(currentMemberVoteResult: String) {
        with(binding.imgVoteComplete) {
            when (currentMemberVoteResult) {
                "PERMIT" -> setImageResource(R.drawable.ic_vote_complete_agree)
                "REJECT" -> setImageResource(R.drawable.ic_vote_complete_disagree)
            }
        }
    }

    private fun setTvFeedAgreeText(feed: ResponseFeedDetail) {
        binding.tvFeedAgree.text = when (feed.permitCount + feed.rejectCount) {
            0 -> String.format(getString(R.string.feed_item_percent), 0)
            else -> String.format(
                getString(R.string.feed_item_percent),
                feed.permitCount * 100 / (feed.permitCount + feed.rejectCount)
            )
        }
    }

    private fun setTvFeedDisAgreeText(feed: ResponseFeedDetail) {
        binding.tvFeedDisagree.text = when (feed.permitCount + feed.rejectCount) {
            0 -> String.format(getString(R.string.feed_item_percent), 0)
            else -> String.format(
                getString(R.string.feed_item_percent),
                feed.rejectCount * 100 / (feed.permitCount + feed.rejectCount)
            )
        }
    }

    private fun setSeekBarProgress(feed: ResponseFeedDetail) {
        binding.progressVoteItem.progress = when (feed.permitCount + feed.rejectCount) {
            0 -> 0
            else -> feed.permitCount * 100 / (feed.permitCount + feed.rejectCount)
        }
    }

    private fun setVoteCommentAdapter() {
        binding.rvVoteComment.adapter = VoteCommentAdapter(voteViewModel)
    }

    private fun setFeedDetailCommentCollect() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                voteViewModel.feedDetailComment.collect { commentList ->
                    commentList?.let {
                        with(binding.rvVoteComment.adapter as VoteCommentAdapter) {
                            submitList(commentList) {
                                binding.rvVoteComment.scrollToPosition(0)
                            }
                        }
                        setTvVoteCommentCountText(commentList.size)
                    }
                }
            }
        }
    }

    private fun setTvVoteCommentCountText(size: Int) {
        binding.tvVoteCommentCount.text = String.format(
            getString(R.string.vote_comments_count),
            size
        )
    }

    private fun setCountDownTimer(deadLine: String) =
        object : CountDownTimer(
            (TimeChangerUtil.getDeadLine(deadLine)),
            1000
        ) {
            override fun onTick(millisUntilFinished: Long) {
                binding.tvVoteItemTime.text =
                    TimeChangerUtil.getDeadLineString(millisUntilFinished)
            }

            override fun onFinish() {
                binding.tvVoteItemTime.text = TimeChangerUtil.getDeadLineString(0)
            }
        }

    private fun cancelCountDownTimer() {
        requireNotNull(timer).cancel()
        timer = null
    }

    private fun setEtVoteCommentFocusListener() {
        binding.etVoteComment.setOnFocusChangeListener { _, isFocused ->
            if (isFocused) {
                voteViewModel.openVote()
            }
        }
    }

    private fun setEtVoteCommentEditorActionListener() {
        binding.etVoteComment.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    if (binding.etVoteComment.text.toString().isNotEmpty())
                        voteViewModel.requestPostComment(
                            args.id,
                            binding.etVoteComment.text.toString()
                        )
                    true
                }
                else -> false
            }
        }
    }

    private fun setRequestPostCommentCollect() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                voteViewModel.requestPostComment.collect { isPosted ->
                    if (isPosted) {
                        voteViewModel.requestVoteFeedComment(args.id)
                    }
                }
            }
        }
    }

    private fun setFabVoteClickListener() {
        binding.fabVote.setOnClickListener {
            voteViewModel.setOpenVote()
        }
    }

    private fun setOpenVoteCollect() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                voteViewModel.openVote.collect { isOpen ->
                    when (isOpen) {
                        0 -> {
                            setLayoutAlpha(1f)
                            setFabVoteCloseBackground()
                            setLayoutVoteCloseAnimation(getLayoutVoteList())
                            delay(300)
                            setLayoutVoteCloseInvisible(getLayoutVoteList())
                        }
                        1 -> {
                            setLayoutAlpha(0.2f)
                            setFabVoteOpenBackground()
                            setLayoutVoteColorVisible(getLayoutVoteList())
                        }
                    }
                }
            }
        }
    }

    private fun setLayoutAlpha(alpha: Float) {
        binding.layoutVote.alpha = alpha
    }

    private fun setFabVoteCloseBackground() {
        with(binding.fabVote) {
            backgroundTintList = requireContext().getColorStateList(R.color.bluegray50_F9FAFC)
            setImageResource(R.drawable.ic_judge)
        }
    }

    private fun setFabVoteOpenBackground() {
        with(binding.fabVote) {
            backgroundTintList = requireContext().getColorStateList(R.color.bluegray700_4D535E)
            setImageResource(R.drawable.ic_x_sign)
        }
    }

    private fun setLayoutVoteColorVisible(list: List<ConstraintLayout>) {
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

    private fun setLayoutVoteClickListener(list: List<ConstraintLayout>) {
        list.indices.forEach { index ->
            list[index].setOnClickListener {
                voteViewModel.requestVote(index, args.id, getVoteResponse(index))
            }
        }
    }

    private fun getVoteResponse(index: Int) = when (index) {
        0 -> "REJECT"
        1 -> "PERMIT"
        else -> throw IndexOutOfBoundsException()
    }

    private fun setRequestVoteCollect() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                voteViewModel.requestVote.collect { index ->
                    index?.let {
                        setLayoutCommentSelectClose(index, getLayoutVoteList())
                    }
                }
            }
        }
    }

    private fun setLayoutCommentSelectClose(selected: Int, list: List<ConstraintLayout>) {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            list.indices.forEach { index ->
                FloatingAnimationUtil.setAnimation(list[index], 0f)
                list[index].visibility = View.INVISIBLE
                setVoteCompleteAction(selected)
            }
        }
    }

    private fun setVoteCompleteAction(selected: Int) {
        binding.imgVoteComplete.apply {
            setAnimation(getLottieRaw(selected))
            visibility = View.VISIBLE
            playAnimation()
            setImgVoteSelectedImage(selected)
        }
    }

    private fun setLottieAnimationListener() {
        binding.imgVoteComplete.apply {
            addAnimatorListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(p0: Animator?) {
                }

                override fun onAnimationEnd(p0: Animator?) {
                    visibility = View.INVISIBLE
                    binding.fabVote.visibility = View.INVISIBLE
                    setVoteSelectedVisibility()
                    setLayoutAlpha(1f)
                }

                override fun onAnimationCancel(p0: Animator?) {
                }

                override fun onAnimationStart(p0: Animator?) {
                }
            })
        }
    }

    private fun setImgVoteSelectedImage(selected: Int) {
        binding.imgVoteSelected.setImageResource(getCompleteImage(selected))
    }

    private fun setVoteSelectedVisibility() {
        with(binding) {
            tvVoteSelected.visibility = View.VISIBLE
            imgVoteSelected.visibility = View.VISIBLE
        }
    }

    private fun getLottieRaw(selected: Int) = when (selected) {
        0 -> R.raw.lottie_disagree
        1 -> R.raw.lottie_agree
        else -> throw IndexOutOfBoundsException()
    }

    private fun getCompleteImage(selected: Int) = when (selected) {
        0 -> R.drawable.ic_vote_complete_disagree
        1 -> R.drawable.ic_vote_complete_agree
        else -> throw IndexOutOfBoundsException()
    }

    private fun getLayoutVoteList() =
        with(binding) {
            listOf(
                layoutVoteDisagree,
                layoutVoteAgree
            )
        }

    override fun onDestroyView() {
        super.onDestroyView()
        cancelCountDownTimer()
        _binding = null
    }
}
