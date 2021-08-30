package com.moo.mool.view.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.moo.mool.R
import com.moo.mool.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initBottomNavigation()
        setBottomNaviVisibility()
    }

    private fun initBottomNavigation() {
        binding.bottomNavigationMain.setupWithNavController(findNavController())
    }

    private fun findNavController(): NavController {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        return navHostFragment.navController
    }

    private fun setBottomNaviVisibility() {
        findNavController().addOnDestinationChangedListener { _, destination, _ ->
            binding.bottomNavigationMain.visibility = when (destination.id) {
                R.id.feedFragment, R.id.myPageFragment -> View.VISIBLE
                else -> View.GONE
            }
        }
    }
}