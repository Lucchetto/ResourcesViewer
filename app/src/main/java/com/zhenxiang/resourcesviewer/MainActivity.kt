package com.zhenxiang.resourcesviewer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var activeFragment : HomeFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(root_toolbar)
        val finderFragment = setupFragment(ResourcesFinderFragment(), getString(R.string.resource_finder_title), R.id.resource_id)
        switchActiveFragment(finderFragment)
        val bottomNav = findViewById<BottomNavigationView>(R.id.home_bottom_nav)
        bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.resource_finder -> {
                    switchActiveFragment(finderFragment)
                }
            }
            true
        }
    }

    private fun setupFragment(fragment : Fragment, title : String, menuId : Int) : HomeFragment {
        val newHomeFragment = HomeFragment(fragment, title, menuId)
        supportFragmentManager.beginTransaction().add(R.id.main_content, newHomeFragment.getFragment(), title).commit()
        return  newHomeFragment
    }

    private fun switchActiveFragment(homeFragment : HomeFragment) {
        if (homeFragment != activeFragment) {
            val transaction = supportFragmentManager.beginTransaction()
            activeFragment?.let { transaction.hide(it.getFragment()) }
            title = homeFragment.getTitle()
            transaction.show(homeFragment.getFragment()).commit()
            activeFragment = homeFragment
        }
    }
}