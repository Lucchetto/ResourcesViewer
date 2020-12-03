package com.zhenxiang.resourcesviewer

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zhenxiang.resourcesviewer.packageselector.PackageSelectorFragment
import com.zhenxiang.resourcesviewer.packageselector.PackageSelectorView

/**
 * A simple [Fragment] subclass.
 * Use the [ResourcesExplorerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ResourcesExplorerFragment : Fragment(), PackageSelectorFragment.PackageSelectorListener {

    private lateinit var packageSelector : PackageSelectorView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val contentView = inflater.inflate(R.layout.fragment_resources_explorer, container, false)
        packageSelector = contentView.findViewById<PackageSelectorView>(R.id.package_selector)
        val packageSelectorFragment = PackageSelectorFragment()
        packageSelectorFragment.setTargetFragment(this, 0)
        packageSelector.setupPackageSelector(packageSelectorFragment, fragmentManager!!)
        return contentView
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ResourcesExplorerFragment().apply {
            }
    }

    override fun onPackageSelected(icon: Drawable, label: String, name: String) {
        packageSelector.setPackageInfo(icon, label, name)
    }
}