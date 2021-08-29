package how.about.it.view.notice

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import how.about.it.R
import how.about.it.databinding.FragmentNoticeListBinding
import how.about.it.model.Notice
import how.about.it.view.main.MainActivity
import how.about.it.viewmodel.NoticeViewModel

class NoticeListFragment : Fragment() {

    private var _binding: FragmentNoticeListBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val noticeViewModel by activityViewModels<NoticeViewModel>()
    lateinit var noticeList : List<Notice>
    val noticeListAdapter = NoticeListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoticeListBinding.inflate(inflater, container, false)
        setToolbarDetail()
        binding.rvNotice.adapter = noticeListAdapter
        binding.noticeViewModel = noticeViewModel

        noticeViewModel.getAllNoticeList.observe(viewLifecycleOwner, Observer {
            noticeListAdapter.setData(it)
            noticeList = it
        })

        noticeListAdapter.setItemClickListener(object : NoticeListAdapter.ItemClickListener{
            override fun onClick(view: View, position: Int) {
                val bundle = Bundle()
                bundle.putString("noticeId", noticeList[position].noticeId.toString())
                requireView().findNavController().navigate(R.id.action_noticeListFragment_to_noticePostFragment, bundle)
            }
        })
        return binding.root
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        (activity as MainActivity).onBackPressed()
        return true
    }
    private fun showBackButton() {
        (activity as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back)
        this.setHasOptionsMenu(true)
    }
    private fun setToolbarDetail() {
        binding.toolbarNoticeListBoard.tvToolbarTitle.setText(R.string.notice)
        (activity as MainActivity).setSupportActionBar(binding.toolbarNoticeListBoard.toolbarBoard)
        (activity as MainActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        showBackButton()
    }
}