package how.about.it.view.comment

import android.animation.Animator
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
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
import how.about.it.databinding.FragmentCommentBinding
import how.about.it.network.RequestToServer
import how.about.it.network.comment.CommentServiceImpl
import how.about.it.util.FloatingAnimationUtil
import how.about.it.view.comment.adapter.ReCommentAdapter
import how.about.it.view.comment.repository.CommentRepository
import how.about.it.view.comment.viewmodel.CommentViewModel
import how.about.it.view.comment.viewmodel.CommentViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CommentFragment : Fragment(), PopupMenu.OnMenuItemClickListener {
    private var _binding: FragmentCommentBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val commentViewModel by viewModels<CommentViewModel>() {
        CommentViewModelFactory(
            CommentRepository(
                CommentServiceImpl(RequestToServer.commentInterface)
            )
        )
    }
    private val args by navArgs<CommentFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommentBinding.inflate(inflater, container, false)
        setCommentBackClickListener()
        setRvReCommentAdapter()
        setReplyListCollect()
        setEmojiListCollect()
        setEmptyReactCollect()
        setBtnEmptyEmojiClickListener()
        setFabCommentReactClickListener()
        setOpenReactCollect()
        setLayoutCommentClickListener(getLayoutReactionList())
        setLottieAnimationListener()
        setBtnCommentMoreClickListener()
        setEtReplyEditorActionListener()
        setIsPostedCollect()
        commentViewModel.initOpenEmoji(args.openEmoji)
        commentViewModel.requestGetComments(args.id)
        return binding.root
    }

    private fun setCommentBackClickListener() {
        binding.btnCommentBack.setOnClickListener {
            requireView().findNavController()
                .popBackStack()
        }
    }

    private fun setRvReCommentAdapter() {
        binding.rvReComment.adapter = ReCommentAdapter(commentViewModel)
    }

    private fun setReplyListCollect() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                commentViewModel.replyList.collect { replyList ->
                    replyList?.let {
                        if (replyList.isNotEmpty()) {
                            submitCommentList(replyList)
                            setLayoutCommentBinding(replyList[0])
                            initEmojiList(replyList[0])
                        }
                    }
                }
            }
        }
    }

    private fun submitCommentList(commentList: List<Comment>) {
        with(binding.rvReComment.adapter as ReCommentAdapter) {
            submitList(commentList.subList(1, commentList.size)) {
                binding.rvReComment.scrollToPosition(itemCount - 1)
            }
        }
    }

    private fun setLayoutCommentBinding(comment: Comment) {
        binding.layoutComment.comment = comment
    }

    private fun initEmojiList(comment: Comment) {
        commentViewModel.initEmojiList(comment.emojiList)
    }

    private fun setEmojiListCollect() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                commentViewModel.emojiList.collect { emojiList ->
                    commentViewModel.setEmptyCommentReactVisibility()
                    emojiList.indices.forEach { index ->
                        with(emojiList[index]) {
                            getLayoutEmoji(emojiId).apply {
                                setTvCommentReactVisibility(emojiCount)
                                setTvCommentReactCount(emojiCount)
                                setTvCommentReactClickListener(index)
                                setTvCommentReactionBackground(checked)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setEmptyReactCollect() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                commentViewModel.emptyEmoji.collect { isEmpty ->
                    setBtnReactionEmptyVisibility(isEmpty)
                }
            }
        }
    }

    private fun setBtnReactionEmptyVisibility(isEmpty: Int) {
        binding.layoutComment.btnReactionEmpty.visibility = when (isEmpty) {
            0 -> View.VISIBLE
            else -> View.GONE
        }
    }

    private fun setBtnEmptyEmojiClickListener() {
        binding.layoutComment.btnReactionEmpty.setOnClickListener {
            commentViewModel.setOpenOrNotOpenReact()
        }
    }

    private fun getLayoutEmoji(emojiId: Int): TextView {
        with(binding.layoutComment) {
            return when (emojiId) {
                1 -> tvCommentReactionBrown
                2 -> tvCommentReactionBlue
                3 -> tvCommentReactionGreen
                4 -> tvCommentReactionRed
                5 -> tvCommentReactionYellow
                else -> throw IndexOutOfBoundsException()
            }
        }
    }

    private fun TextView.setTvCommentReactVisibility(emojiCount: Int) {
        this.visibility = when (emojiCount) {
            0 -> View.GONE
            else -> View.VISIBLE
        }
    }

    private fun TextView.setTvCommentReactCount(emojiCount: Int) {
        this.text = emojiCount.toString()
    }

    private fun TextView.setTvCommentReactClickListener(index: Int) {
        this.setOnClickListener {
            commentViewModel.requestEmoji(index, args.id)
        }
    }

    private fun TextView.setTvCommentReactionBackground(checked: Boolean) {
        when (checked) {
            true -> this.setBackgroundResource(R.drawable.background_small_emoji_voted_round_20)
            false -> this.setBackgroundResource(R.drawable.background_small_emoji_round_20)
        }
    }

    private fun setFabCommentReactClickListener() {
        binding.fabCommentReaction.setOnClickListener {
            commentViewModel.setOpenOrNotOpenReact()
        }
    }

    private fun setOpenReactCollect() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
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
                commentViewModel.requestEmoji(index, args.id)
                setLayoutCommentSelect(index, list)
            }
        }
    }

    private fun setLayoutCommentSelect(selected: Int, list: List<ConstraintLayout>) {
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

    override fun onMenuItemClick(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_comment_update -> {
                requireView().findNavController()
                    .navigate(
                        CommentFragmentDirections.actionCommentFragmentToCommentUpdateFragment(
                            args.id, binding.layoutComment.tvCommentContent.text.toString()
                        )
                    )
                true
            }
            R.id.menu_comment_delete -> {
                requestCommentDeleteDialog()
                true
            }
            else -> false
        }
    }

    private fun requestCommentDeleteDialog() {
        val mDialogView =
            LayoutInflater.from(requireContext()).inflate(R.layout.dialog_default_confirm, null)
        val mBuilder = AlertDialog.Builder(requireContext())
            .setView(mDialogView)
        val mAlertDialog = mBuilder.show()

        mDialogView.findViewById<TextView>(R.id.tv_message_dialog_title)
            .setText(R.string.delete)
        mDialogView.findViewById<TextView>(R.id.tv_message_dialog_description)
            .setText(R.string.comment_delete_dialog)

        mDialogView.findViewById<Button>(R.id.btn_dialog_confirm).setOnClickListener {
            commentViewModel.requestDeleteComment(args.id)
            mAlertDialog.dismiss()
        }
        mDialogView.findViewById<Button>(R.id.btn_dialog_cancel).setOnClickListener {
            mAlertDialog.dismiss()
        }
    }

    private fun setEtReplyEditorActionListener() {
        binding.etReply.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    if (binding.etReply.text.toString().isNotEmpty()) {
                        setEtReplyClearFocus()
                        commentViewModel.requestPostReply(
                            args.id,
                            binding.etReply.text.toString()
                        )
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun setEtReplyClearFocus() {
        binding.etReply.clearFocus()
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.etReply.windowToken, 0)
    }

    private fun setIsPostedCollect() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                with(commentViewModel) {
                    isPosted.collect { isPosted ->
                        if (isPosted) {
                            resetIsPosted()
                            requestGetComments(args.id)
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
