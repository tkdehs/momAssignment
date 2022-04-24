package com.pnx.momassignment.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.navigation.NavigationBarView
import com.pnx.momassignment.R
import com.pnx.momassignment.databinding.FragmentMainBinding

/**
 * MAPPDEV-105
 * 메인 Frame Fragment
 */
class MainFragment : BaseFragment() {
    private lateinit var binding: FragmentMainBinding
    private val mainViewModel: MainViewModel by activityViewModels { MainVMFactory(activityViewModel) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mainViewModel = mainViewModel
        binding.lifecycleOwner = viewLifecycleOwner

//        binding.goSalesFragmentButton.setOnClickListener {
//            navFragment(R.id.action_MainFragment_to_SalesFragment)
//        }
        binding.bottomNaviView.setOnItemSelectedListener(navItemSelectListener)

//        Pref.getInstance().savePreferences(PrefKey.FIRST_PUSH_AUTH,false)
//        pushPermissionAlert()
    }

    /**
     * 하단 탭 선택
     */
    fun selectBottomNaviView(tabId: Int) {
        binding.bottomNaviView.selectedItemId = tabId
    }

    /**
     * 하단 네비게이션 화면 선택 처리
     */
    private val navItemSelectListener = NavigationBarView.OnItemSelectedListener {
        selectedContent(it.itemId)
        true
    }

    private fun selectedContent(itemId: Int): Fragment? {
        when (itemId) {
            R.id.main_tab1 -> {
                val fragment = setMainContent(SearchUserFragment::class.java, itemId)
                return fragment
            }
            R.id.main_tab2 -> {
                val fragment = setMainContent(FavoritesUserFragment::class.java, itemId)
                return fragment
            }
        }

        mainViewModel.setSelectedMenuId(itemId)
        return null
    }

    private fun setMainContent(fragmentClass: Class<out Fragment?>, itemId: Int): Fragment? {
        childFragmentManager.fragments.forEach { fragment ->
            childFragmentManager.beginTransaction().hide(fragment).commitAllowingStateLoss()
        }

        val fragmentTag = itemId.toString() // Proguard 적용시 fragment class 값으로 할 경우 오류 발생 가능
        val fragment = childFragmentManager.findFragmentByTag(fragmentTag)
        if (fragment == null) {
            childFragmentManager.beginTransaction()
                .add(binding.mainConentLayout.id, fragmentClass, null, fragmentTag)
                .commitAllowingStateLoss()
        } else {
            childFragmentManager.beginTransaction().show(fragment).commitAllowingStateLoss()
            fragment.onResume()
        }

        return fragment
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setOnAttachBackpress { finishCheck() }
    }

    override fun onResume() {
        super.onResume()
        selectedContent(binding.bottomNaviView.selectedItemId)
    }
}
