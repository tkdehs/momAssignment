package com.pnx.momassignment.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.pnx.momassignment.R
import com.pnx.momassignment.databinding.FragmentDetailUserBinding
import com.pnx.momassignment.util.UIUtil
import kotlinx.coroutines.launch

class DetailUserFragment:BaseFragment() {

    private val mainViewModel: MainViewModel by activityViewModels { MainVMFactory(activityViewModel) }
    lateinit var binding: FragmentDetailUserBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailUserBinding.inflate(LayoutInflater.from(requireActivity()))
        binding.mainViewModel = mainViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDetail()

        binding.etMemoText.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val text = binding.etMemoText.text.toString()
                if (text != mainViewModel.selectUser.value?.memo) {
                    binding.tvSave.visibility = View.VISIBLE
                } else {
                    binding.tvSave.visibility = View.GONE
                }
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    private fun setDetail(){
        mainViewModel.selectUser.value?.let { apiUser ->
            scope.launch {
                val localUser = mainViewModel.getLocalUser(requireContext(),apiUser.id)

                if (localUser != null) {
                    mainViewModel.setUserMemo(localUser.memo)
                    binding.etMemoText.setText(localUser.memo)
                }
                binding.tvSave.setOnClickListener {
                    apiUser.memo = binding.etMemoText.text.toString()
                    if (localUser != null) {
                        scope.launch {
                            mainViewModel.updateLocalUser(requireContext(),apiUser)
                        }
                    } else {
                        scope.launch {
                            mainViewModel.insertLocalUser(requireContext(),apiUser)
                        }
                    }
                    UIUtil.hideKeyboard(requireActivity())
                    findNavController().popBackStack()
                    Toast.makeText(requireContext(), getString(R.string.save_success), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}