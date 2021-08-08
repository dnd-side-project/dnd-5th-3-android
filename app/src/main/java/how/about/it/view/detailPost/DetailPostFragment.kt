package how.about.it.view.detailPost

import android.animation.ObjectAnimator
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import how.about.it.R
import how.about.it.databinding.FragmentDetailPostBinding
import how.about.it.view.main.MainActivity

class DetailPostFragment : Fragment() {
    private var _binding: FragmentDetailPostBinding? = null
    private val binding get() = requireNotNull(_binding)
    private var isFabOpen = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailPostBinding.inflate(inflater, container, false)

        binding.toolbarDetailPostBoard.tvToolbarTitle.text = "투표하기"
        (activity as MainActivity).setSupportActionBar(binding.toolbarDetailPostBoard.toolbarBoard)
        (activity as MainActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        showBackButton()

        binding.fabDetailPostJudge.setOnClickListener{
            toggleFab()
        }

        binding.fabDetailPostAgree.setOnClickListener {
            Toast.makeText(activity, "찬성 버튼 클릭.", Toast.LENGTH_SHORT).show()
            voteComplete(R.drawable.ic_emoji_vote_complete_agree)
            voteAction(R.drawable.ic_emoji_vote_complete_agree)
        }

        binding.fabDetailPostDisagree.setOnClickListener {
            Toast.makeText(activity, "반대 버튼 클릭.", Toast.LENGTH_SHORT).show()
            voteComplete(R.drawable.ic_emoji_vote_complete_disagree)
            voteAction(R.drawable.ic_emoji_vote_complete_disagree)
        }
        return binding.root
    }

    private fun toggleFab() {
        Toast.makeText(activity, "토클 버튼 클릭.", Toast.LENGTH_SHORT).show()
        if (isFabOpen) {
            closeVoteFab()
        } else {
            showVoteFab()
        }
        isFabOpen = !isFabOpen
    }

    private fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

    private fun showVoteFab() {
        binding.tvMessageDetailPostAgree.visibility = View.VISIBLE
        binding.tvMessageDetailPostDisagree.visibility = View.VISIBLE

        // DP 단위를 PX단위로 바꾸는 함수 작성하여 ObjectAnimator에 적용 (BottomToBottom에 의해 Judge버튼 하단에서부터의 거리)
        ObjectAnimator.ofFloat(binding.fabDetailPostAgree, "translationY", -(160.toPx().toFloat()) ).apply { start() }
        ObjectAnimator.ofFloat(binding.tvMessageDetailPostAgree, "translationY", -(160.toPx().toFloat()) ).apply { start() }

        ObjectAnimator.ofFloat(binding.fabDetailPostDisagree, "translationY", -(80.toPx().toFloat())).apply { start() }
        ObjectAnimator.ofFloat(binding.tvMessageDetailPostDisagree, "translationY", -(80.toPx().toFloat())).apply { start() }

        // Background DIM 처리
        binding.ivDetailPostBackgroundDimVoting.visibility = View.VISIBLE

        // 찬성, 반대버튼 표시
        binding.fabDetailPostAgree.visibility = View.VISIBLE
        binding.fabDetailPostDisagree.visibility = View.VISIBLE

        // 판사봉 버튼 -> Close 버튼
        binding.fabDetailPostJudge.setImageResource(R.drawable.ic_x_sign)
        binding.fabDetailPostJudge.backgroundTintList = requireContext().resources.getColorStateList(R.color.bluegray700_4D535E, context?.theme)
    }
    private fun closeVoteFab() {
        binding.tvMessageDetailPostAgree.visibility = View.INVISIBLE
        binding.tvMessageDetailPostDisagree.visibility = View.INVISIBLE

        // 찬성, 반대버튼 표시 아래로 가면서 겹쳐져서 안보임
        ObjectAnimator.ofFloat(binding.fabDetailPostAgree, "translationY", 0f).apply { start() }
        ObjectAnimator.ofFloat(binding.tvMessageDetailPostAgree, "translationY", 0f).apply { start() }

        ObjectAnimator.ofFloat(binding.fabDetailPostDisagree, "translationY", 0f).apply { start() }
        ObjectAnimator.ofFloat(binding.tvMessageDetailPostDisagree, "translationY", 0f).apply { start() }

        // Background DIM 처리 해제
        binding.ivDetailPostBackgroundDimVoting.visibility = View.GONE

        // Close 버튼 ->  판사봉 버튼
        binding.fabDetailPostJudge.setImageResource(R.drawable.ic_judge)
        binding.fabDetailPostJudge.backgroundTintList = requireContext().resources.getColorStateList(R.color.bluegray50_F9FAFC, context?.theme)
    }

    private fun voteComplete(completeImg : Int) {
        // Background DIM 처리 해제
        binding.ivDetailPostBackgroundDimVoting.visibility = View.GONE
        binding.tvMessageDetailPostVoteComplete.visibility = View.GONE

        // 찬성, 반대 버튼 삭제
        binding.fabDetailPostAgree.visibility = View.GONE
        binding.tvMessageDetailPostAgree.visibility =View.GONE
        binding.fabDetailPostDisagree.visibility = View.GONE
        binding.tvMessageDetailPostDisagree.visibility = View.GONE

        // 투표할 수 있는 버튼 삭제
        binding.tvMessageDetailPostVoteComplete.visibility = View.VISIBLE
        binding.fabDetailPostJudge.visibility = View.INVISIBLE
        binding.fabDetailPostJudge.isEnabled = false

        // 투표 완료 도장 나타내기
        binding.fabDetailPostVoteComplete.setImageResource(completeImg)
        binding.fabDetailPostVoteComplete.visibility = View.VISIBLE
        binding.tvMessageDetailPostVoteComplete.visibility = View.VISIBLE
    }

    private fun voteAction(completeImg: Int) {
        // 도장 액션 나타내기
        binding.ivDetailPostBackgroundDimVoting.visibility = View.VISIBLE
        binding.ivDetailPostVoteComplete.setImageResource(completeImg)
        binding.ivDetailPostVoteComplete.visibility = View.VISIBLE
        Handler().postDelayed({
            // 도장 액션 나타낸후 1.5초후 다시 사라지기
            binding.ivDetailPostBackgroundDimVoting.visibility = View.GONE
            binding.ivDetailPostVoteComplete.visibility = View.GONE
        }, 1500)

    }

    private fun showBackButton() {
        (activity as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        this.setHasOptionsMenu(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}