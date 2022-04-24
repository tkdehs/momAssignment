package com.pnx.momassignment.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.UiThread
import androidx.fragment.app.activityViewModels
import com.pnx.momassignment.databinding.FragmentSearchUserBinding

class SearchUserFragment:BaseFragment() {

    private val mainViewModel: MainViewModel by activityViewModels { MainVMFactory(activityViewModel) }
    lateinit var binding: FragmentSearchUserBinding

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

        binding.testUserSearch.setOnClickListener {
            mainViewModel.getUserList("tkdehs")
        }
        binding.saveLocal.setOnClickListener {
            mainViewModel.saveUser(requireContext())
            mainViewModel.getUser(requireContext()) { users ->
                var userNames = ""
                users.map {
                    userNames += "${it.login}, "
                }
                requireActivity().runOnUiThread {
                    binding.savedids.text = userNames
                }
            }

        }
    }
}