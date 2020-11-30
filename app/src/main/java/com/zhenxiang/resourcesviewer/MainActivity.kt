package com.zhenxiang.resourcesviewer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var activeFragment : HomeFragment? = null
    private lateinit var collapsingToolbar : CollapsingToolbarLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        collapsingToolbar = findViewById<CollapsingToolbarLayout>(R.id.collapsing_toolbar_layout)
        val finderFragment = setupFragment(ResourceFinderFragment(), getString(R.string.resource_finder_title), R.id.resource_finder)
        val explorerFragment = setupFragment(ResourcesExplorerFragment(), getString(R.string.resources_explorer_title), R.id.resources_explorer)
        switchActiveFragment(finderFragment)
        val bottomNav = findViewById<BottomNavigationView>(R.id.home_bottom_nav)
        bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.resource_finder -> switchActiveFragment(finderFragment)
                R.id.resources_explorer -> switchActiveFragment(explorerFragment)
            }
            true
        }
    }

    private fun setupFragment(fragment : Fragment, title : String, menuId : Int) : HomeFragment {
        val newHomeFragment = HomeFragment(fragment, title, menuId)
        supportFragmentManager.beginTransaction().add(R.id.main_content, fragment, title).hide(fragment).commit()
        return newHomeFragment
    }

    private fun switchActiveFragment(homeFragment : HomeFragment) {
        if (homeFragment != activeFragment) {
            val transaction = supportFragmentManager.beginTransaction()
            activeFragment?.let { transaction.hide(it.getFragment()) }
            collapsingToolbar.title = homeFragment.getTitle()
            transaction.show(homeFragment.getFragment()).commit()
            activeFragment = homeFragment
        }
    }
}