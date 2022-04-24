package com.pnx.momassignment.activity

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import com.pnx.momassignment.Define
import com.pnx.momassignment.R
import com.pnx.momassignment.databinding.ActivityMainBinding

class MainActivity:BaseActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewModel = activityViewModel

        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.nav_graph)

        var startFragmentId = R.id.mainFragment
        graph.setStartDestination(startFragmentId)

        Log.d("TEST","onCreate main activity")
        val navController = navHostFragment.navController
        val bundle = intent.getBundleExtra(Define.BUNDLE_KEY)
        if (bundle is Bundle) {
            bundle.putAll(intent.extras)
            navController.setGraph(graph, bundle)
        } else {
            navController.setGraph(graph, intent.extras)
        }

    }
}