package com.pnx.momassignment.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.pnx.momassignment.databinding.FragmentFavoritesUserBinding

class FavoritesUserFragment:BaseFragment() {

    private val mainViewModel: MainViewModel by activityViewModels { MainVMFactory(activityViewModel) }
    lateinit var binding: FragmentFavoritesUserBinding

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
    }
}