package com.zhenxiang.resourcesviewer.packageselector

import android.content.pm.PackageInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.revengeos.revengeui.fragment.FullscreenDialogFragment
import com.revengeos.revengeui.utils.NavigationModeUtils
import com.zhenxiang.resourcesviewer.R
import kotlinx.coroutines.*
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [PackageSelectorFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PackageSelectorFragment : FullscreenDialogFragment() {

    val TAG = this.javaClass.name
    private val scope = CoroutineScope(Job() + Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val contentView = inflater.inflate(R.layout.fragment_package_selector, container, false)
        val packagesRecyclerView = contentView.findViewById<RecyclerView>(R.id.packages_list)

        val packageManager = contentView.context.packageManager
        val nameComparator = Comparator<PackageInfo> { arg0, arg1 ->
            val name0 =
                arg0?.applicationInfo?.loadLabel(packageManager).toString()
            val name1 =
                arg1?.applicationInfo?.loadLabel(packageManager).toString()
            name0.compareTo(name1, ignoreCase = true)
        }

        val collapsingToolbar = contentView.findViewById<CollapsingToolbarLayout>(R.id.collapsing_toolbar_layout)
        val toolbar = contentView.findViewById<Toolbar>(R.id.package_selector_menu_toolbar)
        val searchMenu = toolbar.menu.findItem(R.id.package_search)
        val searchView = searchMenu.actionView as SearchView
        toolbar.setOnMenuItemClickListener {
            collapsingToolbar.isTitleEnabled = false
            if (it == searchMenu) {
                val searchTextView = searchView.findViewById<AutoCompleteTextView>(androidx.appcompat.R.id.search_src_text)
                searchTextView.setOnFocusChangeListener { view, hasFocus ->
                    if (!hasFocus) {
                        it.collapseActionView()
                        collapsingToolbar.isTitleEnabled = true
                    }
                }
            } else {
                Log.d(TAG, "Menu item not found !")
            }
            true
        }

        if (NavigationModeUtils.isFullGestures(requireContext())) {
            dialog?.window?.let { WindowCompat.setDecorFitsSystemWindows(it, false) }
            val titleToolbar = contentView.findViewById<Toolbar>(R.id.title_toolbar)
            ViewCompat.setOnApplyWindowInsetsListener(contentView) { view, inset ->
                val topInset = WindowInsetsCompat(inset).getInsets(WindowInsetsCompat.Type.systemBars()).top
                (titleToolbar.layoutParams as ViewGroup.MarginLayoutParams).topMargin = topInset
                (collapsingToolbar.layoutParams as ViewGroup.MarginLayoutParams).topMargin = topInset
                return@setOnApplyWindowInsetsListener inset
            }
        }

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Default) {
            val packagesList = packageManager.getInstalledPackages(0)
            Collections.sort(packagesList, nameComparator)
            try {
                val packagesAdapter = PackageItemAdapter(
                    packageManager,
                    packagesList,
                    requireFragmentManager(),
                    this@PackageSelectorFragment
                )
                withContext(Dispatchers.Main) {
                    packagesAdapter.setupSearchFilter()
                    packagesRecyclerView.layoutManager = LinearLayoutManager(context)
                    packagesRecyclerView.adapter = packagesAdapter
                    contentView.findViewById<View>(R.id.loading_spinner).visibility = View.GONE
                    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            return false
                        }

                        override fun onQueryTextChange(newText: String?): Boolean {
                            packagesAdapter.filter.filter(newText)
                            return false
                        }

                    })
                }
            } catch (e : IllegalStateException) {
                // Do nothing
            }
        }

        return contentView
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            PackageSelectorFragment()
                .apply {
                }
    }
}