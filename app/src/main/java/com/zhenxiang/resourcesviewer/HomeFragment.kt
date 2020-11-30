package com.zhenxiang.resourcesviewer

import androidx.fragment.app.Fragment

class HomeFragment(fragment : Fragment, title : String, menuId : Int) {
    private val fragment: Fragment = fragment
    private val title : String = title
    private val menuId : Int = menuId

    fun getFragment() : Fragment {
        return fragment
    }

    fun getTitle() : String {
        return title
    }

    fun getMenuId() : Int {
        return menuId
    }
}