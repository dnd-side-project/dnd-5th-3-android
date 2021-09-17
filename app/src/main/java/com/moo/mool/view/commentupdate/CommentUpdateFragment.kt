package com.moo.mool.view.commentupdate

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.moo.mool.R
import com.moo.mool.databinding.FragmentCommentUpdateBinding
import com.moo.mool.util.HideKeyBoardUtil
import com.moo.mool.util.autoCleared
import com.moo.mool.util.popBackStack
import com.moo.mool.util.repeatOnLifecycle
import com.moo.mool.view.ToastDefaultBlack
import com.moo.mool.view.commentupdate.viewmodel.CommentUpdateViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class CommentUpdateFragment : Fragment() {
    private var binding by autoCleared<FragmentCommentUpdateBinding>()
    private val commentUpdateViewModel by viewModels<CommentUpdateViewModel>()
    private val args by navArgs<CommentUpdateFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCommentUpdateBinding.inflate(inflater, container, false)
        setCommentUpdateBackClickListener()
        setRootClickListener()
        setEtCommentUpdateListener()
        setEtCommentUpdateText()
        setTvUpdateRequestClickListener()
        setIsUpdatedCollect()
        return binding.root
    }

    private fun setCommentUpdateBackClickListener() {
        binding.btnCommentUpdateBack.setOnClickListener {
            popBackStack()
        }
    }

    private fun setRootClickListener() {
        with(binding) {
            root.setOnClickListener {
                HideKeyBoardUtil.hide(requireContext(), etCommentUpdate)
            }
        }
    }

    private fun setEtCommentUpdateText() {
        binding.etCommentUpdate.setText(args.content)
    }

    private fun setEtCommentUpdateListener() {
        with(binding.etCommentUpdate) {
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    setTvUpdateRequestColor(text.length)
                    setTvUpdateRequestClickable(text.length)
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

    private fun setTvUpdateRequestColor(length: Int) {
        with(binding.tvCommentUpdateRequest) {
            when (length) {
                0 -> setTextColor(requireContext().getColor(R.color.bluegray50_F9FAFC))
                else -> setTextColor(requireContext().getColor(R.color.moomool_pink_ff227c))
            }
        }
    }

    private fun setTvUpdateRequestClickable(length: Int) {
        binding.tvCommentUpdateRequest.isClickable = when (length) {
            0 -> false
            else -> true
        }
    }

    private fun setTvUpdateRequestClickListener() {
        binding.tvCommentUpdateRequest.setOnClickListener {
            commentUpdateViewModel.requestCommentUpdate(
                args.id,
                binding.etCommentUpdate.text.toString()
            )
        }
    }

    private fun setIsUpdatedCollect() {
        repeatOnLifecycle {
            commentUpdateViewModel.isUpdated.collect { isUpdated ->
                when (isUpdated) {
                    true -> popBackStack()
                }
            }
        }
    }
}
