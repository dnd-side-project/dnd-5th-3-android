package how.about.it.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import how.about.it.databinding.DialogDefaultConfirmBinding

class DialogDefaultConfirm : DialogFragment() {
    private var _binding: DialogDefaultConfirmBinding? = null
    private val binding get() = requireNotNull(_binding)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogDefaultConfirmBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnDialogConfirm.setOnClickListener {
            dismiss()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}