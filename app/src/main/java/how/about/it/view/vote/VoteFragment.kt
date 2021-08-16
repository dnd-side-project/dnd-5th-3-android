package how.about.it.view.vote

import android.os.Bundle
import android.os.CountDownTimer
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
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
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect

class VoteFragment : Fragment() {
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

    private fun setFeedDetailCollect() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            voteViewModel.feedDetail.collect { feedDetail ->
                feedDetail?.let {
                    timer = setCountDownTimer(feedDetail.voteDeadline).start()
                    binding.apply {
                        feed = feedDetail
                        remainTime = TimeChangerUtil.getRemainTime(feedDetail.voteDeadline)
                    }
                }
            }
        }
    }

    private fun setVoteCommentAdapter() {
        binding.rvVoteComment.adapter = VoteCommentAdapter()
    }

    private fun setFeedDetailCommentCollect() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            voteViewModel.feedDetailComment.collect { commentList ->
                commentList?.let {
                    with(binding.rvVoteComment.adapter as VoteCommentAdapter) {
                        submitList(commentList)
                    }
                }
            }
        }
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

    private fun setFabVoteClickListener() {
        binding.fabVote.setOnClickListener {
            voteViewModel.setOpenVote()
        }
    }

    private fun setOpenVoteCollect() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
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
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            voteViewModel.requestVote.collect { index ->
                index?.let {
                    setLayoutCommentSelectClose(index, getLayoutVoteList())
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
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            with(binding) {
                with(imgVoteComplete) {
                    setImageResource(getCompleteImage(selected))
                    visibility = View.VISIBLE
                    delay(1500)
                    visibility = View.INVISIBLE
                }
                fabVote.visibility = View.INVISIBLE
                setVoteSelectedVisibility(selected)
                setLayoutAlpha(1f)
            }
            this.cancel()
        }
    }

    private fun setVoteSelectedVisibility(selected: Int) {
        with(binding) {
            tvVoteSelected.visibility = View.VISIBLE
            with(imgVoteSelected) {
                visibility = View.VISIBLE
                setImageResource(getCompleteImage(selected))
            }
        }
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
