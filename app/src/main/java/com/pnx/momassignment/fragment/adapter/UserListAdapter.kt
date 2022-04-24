package com.pnx.momassignment.fragment.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pnx.momassignment.R
import com.pnx.momassignment.databinding.FragmentSearchUserAdapterBinding
import com.pnx.momassignment.fragment.BaseFragment
import com.pnx.momassignment.fragment.MainViewModel
import com.pnx.momassignment.room.data.UserItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserListAdapter(val mainViewModel: MainViewModel,val fragment:BaseFragment):
    PagingDataAdapter<UserItem, UserListAdapter.ViewHolder>(DiffUtilItemCallback) {

    inner class ViewHolder(
        private val binding: FragmentSearchUserAdapterBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: UserItem) {
            with(binding) {
                data = item
                executePendingBindings()
            }
            CoroutineScope(Dispatchers.IO).launch {
                mainViewModel.getLocalChackUserId(fragment.requireContext(),item.id) {
                    setStarImage(it)
                }
            }
            binding.ivStar.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    mainViewModel.getLocalChackUserId(fragment.requireContext(), item.id) {
                        if (!it) {
                            mainViewModel.insertLocalUser(fragment.requireContext(), item)
                            setStarImage(true)
                            mainViewModel.getLocalUserList(fragment.requireContext())
                        } else {
                            mainViewModel.deleteLocalUser(fragment.requireContext(), item)
                            setStarImage(false)
                            mainViewModel.getLocalUserList(fragment.requireContext())
                        }
                    }
                }
            }

            binding.root.setOnClickListener {
                mainViewModel.getUserDetail(item.login){
                    fragment.navFragment(R.id.action_mainFragment_to_detailUserFragment)
                }
            }
        }

        private fun setStarImage(isFavorit:Boolean){
            mainViewModel.viewModelScope.launch {
                if (isFavorit) {
                    binding.ivStar.setImageResource(R.drawable.ic_baseline_star)
                    binding.ivStar.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(fragment.requireContext(),R.color.colorF0BC6F))
                } else {
                    binding.ivStar.setImageResource(R.drawable.ic_baseline_star_empty)
                    binding.ivStar.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(fragment.requireContext(),R.color.colorF0BC6F))
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.fragment_search_user_adapter,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }
}

object DiffUtilItemCallback : DiffUtil.ItemCallback<UserItem>() {
    override fun areItemsTheSame(
        oldItem: UserItem,
        newItem: UserItem
    ) = oldItem == newItem

    override fun areContentsTheSame(
        oldItem: UserItem,
        newItem: UserItem
    ) = true
}