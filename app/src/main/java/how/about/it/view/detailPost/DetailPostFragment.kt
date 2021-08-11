package how.about.it.view.detailPost

import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
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

        binding.toolbarDetailPostBoard.tvToolbarTitle.setText(R.string.detail_post_vote)
        (activity as MainActivity).setSupportActionBar(binding.toolbarDetailPostBoard.toolbarBoard)
        (activity as MainActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        showBackButton()

        // TODO : 자신이 작성한 글인 경우 투표버튼 disable하는 조건문 작성
        if(false) {
            disableAction()
        }

        binding.fabDetailPostJudge.setOnClickListener{
            toggleFab()
        }

        binding.fabDetailPostAgree.setOnClickListener {
            voteComplete(R.drawable.ic_vote_complete_agree)
            voteAction(R.drawable.ic_vote_complete_agree)
        }

        binding.fabDetailPostDisagree.setOnClickListener {
            voteComplete(R.drawable.ic_vote_complete_disagree)
            voteAction(R.drawable.ic_vote_complete_disagree)
        }
        return binding.root
    }

    private fun toggleFab() {
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

    // 자신이 작성한 글인 경우 반응 버튼 사용불가
    private fun disableAction(){
        binding.fabDetailPostJudge.isEnabled = false
        binding.fabDetailPostJudge.backgroundTintList = requireContext().resources.getColorStateList(R.color.bluegray500_878C96, context?.theme)
        binding.fabDetailPostJudge.setColorFilter(ContextCompat.getColor(requireContext(), R.color.bluegray50_F9FAFC), android.graphics.PorterDuff.Mode.SRC_IN)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if(true /* TODO : 게시글의 글쓴이 == 현재 로그인 계정 이면 추가 메뉴가 나타나도록 조건문 설정 */) {
            inflater.inflate(R.menu.menu_detail_post_mine_toolbar, menu)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if (item.itemId == android.R.id.home) {
            (activity as MainActivity).onBackPressed()
        } else if(item.itemId == R.id.action_delete_mine_post) {

            // TODO : Dialog 띄우기 코드 개선 필요
            val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_default_confirm, null)
            val mBuilder = AlertDialog.Builder(requireContext())
                .setView(mDialogView)
            val mAlertDialog = mBuilder.show()

            // Dialog 제목 및 내용 설정
            mDialogView.findViewById<TextView>(R.id.tv_message_dialog_title).setText(R.string.delete)
            mDialogView.findViewById<TextView>(R.id.tv_message_dialog_description).setText(R.string.detail_post_message__delete_my_post)

            // Dialog 확인, 취소버튼 설정
            val confirmButton = mDialogView.findViewById<Button>(R.id.btn_dialog_confirm)
            val cancelButton = mDialogView.findViewById<Button>(R.id.btn_dialog_cancel)

            // Dialog 확인 버튼을 클릭 한 경우
            confirmButton.setOnClickListener {
                // TODO : 현재 작성한 게시글 삭제 코드 작성
                mAlertDialog.dismiss()
            }
            cancelButton.setOnClickListener {
                mAlertDialog.dismiss()
            }
        }
        return true
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