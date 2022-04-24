package com.pnx.momassignment.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.*
import com.pnx.momassignment.databinding.FragmentSearchUserBinding
import com.pnx.momassignment.fragment.adapter.UserListAdapter
import com.pnx.momassignment.util.UIUtil
import kotlinx.coroutines.launch


class SearchUserFragment:BaseFragment() {

    private val mainViewModel: MainViewModel by activityViewModels { MainVMFactory(activityViewModel) }
    lateinit var binding: FragmentSearchUserBinding

    private lateinit var adapter: UserListAdapter
    private var goFirst = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchUserBinding.inflate(LayoutInflater.from(requireActivity()))
        binding.mainViewModel = mainViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        initViews()
    }

    private fun initViews(){
        binding.ivSserchBtn.setOnClickListener {
            searchUser()
        }
        binding.etSerchText.setOnKeyListener { view, keyCode, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN) {
                when(keyCode) {
                    KeyEvent.KEYCODE_SEARCH, KeyEvent.KEYCODE_ENTER -> {
                        searchUser()
                    }
                }
            }
            true
        }
    }

    private fun initAdapter() {
        adapter = UserListAdapter(mainViewModel,this)
        binding.rvSearchUser.layoutManager = GridLayoutManager(requireActivity(), 1, GridLayoutManager.VERTICAL, false)
        binding.rvSearchUser.adapter = adapter
        adapter.addLoadStateListener { loadState ->
            if (loadState.source.refresh is LoadState.Loading) {
                goFirst = true
                showProgress()
            } else {
                if (goFirst) {
                    goFirst = false
                    (binding.rvSearchUser.layoutManager as GridLayoutManager).scrollToPositionWithOffset(0, 0)
                }
                hideProgress()
            }
            if (adapter.itemCount == 0) {
                binding.tvNoData.visibility = View.VISIBLE
            } else {
                binding.tvNoData.visibility = View.GONE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }

    private fun searchUser(){
        val searchText = binding.etSerchText.text.toString()
        if (searchText.isNotEmpty()) {
            mainViewModel.apiSearchName = searchText
            mainViewModel.getLocalUserListApi(requireContext())
            binding.etSerchText.setText("")
            UIUtil.hideKeyboard(requireActivity())
        }
    }

    override fun setObserve() {
        super.setObserve()
        mainViewModel.searchUserList.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                adapter.submitData(it)
            }
        }
    }
}