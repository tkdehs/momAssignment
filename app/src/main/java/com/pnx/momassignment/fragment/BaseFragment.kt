package com.pnx.momassignment.fragment

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.ActivityNavigatorExtras
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.pnx.momassignment.Define
import com.pnx.momassignment.R
import com.pnx.momassignment.activity.ActivityViewModel
import com.pnx.momassignment.activity.BaseActivity
import com.pnx.momassignment.util.UIUtil
import kotlinx.coroutines.*

open class BaseFragment: Fragment() {
    val TAG = this::class.simpleName

    val activityViewModel: ActivityViewModel by activityViewModels()  //  Activity와 공유 하는 ViewModel

    val RESULT_KEY = "RESULT_KEY"   // Activity의 Result Key
    // fragmentResult에 값을 전달 하기 위한 시작 지점 Fragment 이름 저장 키

    protected val scope = CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate : ${TAG}")
    }

    /**
     * 옵저버 함수 설정 override 용도
     */
    open fun setObserve(){}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /* 이전 Fragement로 데이터 전달 리스너 설정 */
        setFragmentResultListener(TAG!!) {key, bundle ->
            runBlocking {
                fragmentResultBlock.let {
                    it(bundle)
                    fragmentResultBlock = {}    // block 초기화
                }
            }
        }

        // 백버튼
        view.findViewById<View>(R.id.toolbar_back_btn).let {
            it?.setOnClickListener {
                requireActivity().onBackPressed()
            }
        }
        setObserve()
    }


    /**
     * BackPress 종료 체크 Toast
     */
    private var isFinish = false    // BackPress 두번 클릭 체크 값
    fun finishCheck() {
        CoroutineScope(Dispatchers.Main).launch {
            delay(1500)
            isFinish = false
        }
        if (!isFinish)
            Toast.makeText(requireContext(), getString(R.string.finish_toast), Toast.LENGTH_SHORT).show()
        return if(isFinish) requireActivity().finishAffinity() else isFinish = true
    }

    /**
     * Fragment 이동
     * @param sendBundle : 이동할 Fragment(B)에 전달할 Bundle
     * @param isAnim : 애니메이션 실행 여부
     * @param resultBlock : 이동한 Fragment(B)에서
     *                setFragmentResult(rcvClass: Class<*>, bundle: Bundle = Bundle())에 이전 Fragment(A)에 전달할 Bundle을 넣어준다.
     */
    private var fragmentResultBlock: suspend (Bundle) -> Unit = {}
    fun navFragment(actionId: Int                                       // 이동할 navigation Fragment ID
                    , sendBundle: Bundle = Bundle()                     // 전달할 Bundle
                    , navOption: NavOptions = getAnimationNavOption()   // Fragment 이동 애니메이션 별도 처리가 필요 할 경우
                    , resultBlock: suspend (Bundle) -> Unit = {}        // FragmentResult 결과 받는 Block
    ) {
        Handler(Looper.getMainLooper()).post {
            UIUtil.hideKeyboard(requireActivity())

            fragmentResultBlock = resultBlock
//            val navOption: NavOptions = getAnimationNavOption()

            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(requireActivity())
            val extras = ActivityNavigatorExtras(options)
            try {
                findNavController().navigate(
                    actionId,
                    sendBundle, // Bundle of args
                    navOption, // NavOptions
                    extras)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getColor(resId: Int): Int {
        return ContextCompat.getColor(requireContext(), resId)
    }


    private var activityResultBlock: suspend (ActivityResult) -> Unit = {}
    private val activityResultListener = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        result.data?.let {
            val bundle = it.getBundleExtra(RESULT_KEY)
            if (arguments != null) {
                arguments?.putAll(bundle)
            } else {
                arguments = bundle
            }
        }

        runBlocking {
            activityResultBlock.let {
                it(result)
                activityResultBlock = {}
            }
        }
    }

    /**
     * Fragment 애니메이션 처리
     */
    protected fun getAnimationNavOption(): NavOptions {
        return NavOptions.Builder()
            .setEnterAnim(R.anim.slide_in_right)    // action을 수행할 때 이동할 화면(destination)에 대한 애니메이션입니다.
            .setExitAnim(R.anim.slide_out_left)     // action을 수행할 때 현재 화면에 대한 애니메이션입니다.
            .setPopEnterAnim(R.anim.slide_in_left)  // 지난 화면으로 돌아갈 때(pop) 종료되는 화면에 대한 애니메이션입니다.
            .setPopExitAnim(R.anim.slide_out_right) // 지난 화면으로 돌아갈 때 이동할 화면에 대한 애니메이션입니다.
            .build()
    }


    /**
     * DummyActivity, BaseFragment 번들 공통 전달 받기
     */
    open fun getBundle(block: suspend (bundle: Bundle) -> Unit) {
        arguments?.let {
            val bundle = it.getBundle(Define.BUNDLE_KEY)
            if (bundle is Bundle) {
                runBlocking { block(bundle) }
            } else {
                runBlocking { block(it) }
            }
        }
    }

    fun showProgress() {
        if (activity is BaseActivity) {
            val baseActivity = activity as BaseActivity
            baseActivity.showProgress()
        }
    }

    fun hideProgress() {
        if (activity is BaseActivity) {
            val baseActivity = activity as BaseActivity
            baseActivity.hideProgress()
        }
    }

    override fun onResume() {
        super.onResume()
    }

    /**
     * BackPress Callback
     * Fragment 빽버튼 처리
     * OnAttach에 setOnAttachBackpress(block: suspend() -> Unit) 함수를 호출 하면 BackPress가 backPressedCallback을 타게 된다.
     */
    private lateinit var backPressedCallback: OnBackPressedCallback // BackPress CallBack
    fun setOnAttachBackpress(block: suspend() -> Unit) {
        backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                runBlocking { block() }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, backPressedCallback)
    }
}