package com.pnx.momassignment.fragment

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pnx.momassignment.R
import com.pnx.momassignment.databinding.FragmentFavoritesUserBinding
import com.pnx.momassignment.databinding.FragmentSearchUserAdapterBinding
import com.pnx.momassignment.fragment.adapter.UserListAdapter
import com.pnx.momassignment.room.RoomDBClient
import com.pnx.momassignment.room.data.UserItem
import com.pnx.momassignment.util.UIUtil
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

class FavoritesUserFragment:BaseFragment() {

    private val mainViewModel: MainViewModel by activityViewModels { MainVMFactory(activityViewModel) }
    lateinit var binding: FragmentFavoritesUserBinding

    private lateinit var adapter: UserListAdapter
    private var goFirst = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesUserBinding.inflate(LayoutInflater.from(requireActivity()))
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
            searchUserLocal()
        }
        binding.etSerchText.setOnKeyListener { view, keyCode, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN) {
                when(keyCode) {
                    KeyEvent.KEYCODE_SEARCH, KeyEvent.KEYCODE_ENTER -> {
                        searchUserLocal()
                    }
                }
            }
            true
        }
        binding.etSerchText.setOnFocusChangeListener { view, b ->
        }

        lifecycleScope.launch {
            adapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.NotLoading }
                .collect { binding.rvSearchUser.scrollToPosition(0) }
        }
    }

    private fun searchUserLocal(){
        mainViewModel.locakSearchName = binding.etSerchText.text.toString()
        mainViewModel.getLocalUserList(requireContext())
        binding.etSerchText.setText("")
        UIUtil.hideKeyboard(requireActivity())
    }

    private fun initAdapter() {
        adapter = UserListAdapter(mainViewModel,this)
        binding.rvSearchUser.layoutManager = GridLayoutManager(requireActivity(), 1, GridLayoutManager.VERTICAL, false)
        binding.rvSearchUser.adapter = adapter
        adapter.addLoadStateListener { loadState ->
            if (loadState.source.refresh is LoadState.Loading) {
                goFirst = true
            } else {
                if (goFirst) {
                    goFirst = false
                    (binding.rvSearchUser.layoutManager as GridLayoutManager).scrollToPositionWithOffset(0, 0)
                }
            }
            if (adapter.itemCount == 0) {
                binding.tvNoData.visibility = View.VISIBLE
            } else {
                binding.tvNoData.visibility = View.GONE
            }
        }
        searchUserLocal()
    }

    override fun setObserve() {
        super.setObserve()
        mainViewModel.localSearchUserList.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                adapter.submitData(it)
            }
        }
    }
}