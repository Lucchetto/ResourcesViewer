package com.zhenxiang.resourcesviewer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.shape.MaterialShapeDrawable
import com.revengeos.revengeui.utils.NavigationModeUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var collapsingToolbar : CollapsingToolbarLayout
    private lateinit var bottomNav : BottomNavigationView
    private var activeFragment : Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        collapsingToolbar = findViewById(R.id.collapsing_toolbar_layout)

        val finderFragment : Fragment
        val explorerFragment : Fragment
        if (savedInstanceState == null) {
            finderFragment = setupFragment(ResourceFinderFragment(), getString(R.string.resource_finder_title))
            explorerFragment = setupFragment(ResourcesExplorerFragment(), getString(R.string.resources_explorer_title))
            switchActiveFragment(finderFragment)
        } else {
            finderFragment = supportFragmentManager.findFragmentByTag(getString(R.string.resource_finder_title))!!
            explorerFragment = supportFragmentManager.findFragmentByTag(getString(R.string.resources_explorer_title))!!
        }

        bottomNav = findViewById(R.id.home_bottom_nav)
        bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.resource_finder -> switchActiveFragment(finderFragment)
                R.id.resources_explorer -> switchActiveFragment(explorerFragment)
            }
            true
        }

        if (NavigationModeUtils.isFullGestures(applicationContext)) {
            val bottomNavBgColour = (bottomNav.background as MaterialShapeDrawable).fillColor!!.defaultColor
            window.navigationBarColor = bottomNavBgColour
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("selectedTab", bottomNav.selectedItemId)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        bottomNav.selectedItemId = savedInstanceState.getInt("selectedTab")
        super.onRestoreInstanceState(savedInstanceState)
    }

    private fun setupFragment(fragment : Fragment, title : String) : Fragment {
        supportFragmentManager.beginTransaction().add(R.id.main_content, fragment, title).hide(fragment).commit()
        return fragment
    }

    private fun switchActiveFragment(newFragment : Fragment) {
        if (newFragment != activeFragment) {
            val transaction = supportFragmentManager.beginTransaction()
            activeFragment?.let { transaction.hide(it) }
            collapsingToolbar.title = newFragment.tag
            transaction.show(newFragment).commit()
            activeFragment = newFragment
        }
    }
}