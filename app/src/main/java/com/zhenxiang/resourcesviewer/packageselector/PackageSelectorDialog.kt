package com.zhenxiang.resourcesviewer.packageselector

import android.app.Dialog
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.revengeos.revengeui.utils.NavigationModeUtils
import com.zhenxiang.resourcesviewer.R
import kotlinx.coroutines.*
import java.util.*
import kotlin.Comparator

class PackageSelectorDialog : Dialog {

    val TAG = this.javaClass.name
    private val packageManager : PackageManager
    private val nameComparator : Comparator<PackageInfo>
    var job : Job? = null

    private lateinit var packagesRecyclerView : RecyclerView
    private lateinit var toolbar : Toolbar
    private lateinit var searchMenu : MenuItem
    private lateinit var searchView : SearchView

    constructor(context : Context) : this(context, 0) {
    }

    constructor(context : Context, themeResId : Int) : super(context, themeResId) {
        packageManager = context.packageManager
        nameComparator = Comparator { arg0, arg1 ->
            if (arg0.packageName == "android") {
                -1
            } else if (arg1.packageName == "android") {
                1
            } else {
                val name0 =
                    arg0?.applicationInfo?.loadLabel(packageManager).toString()
                val name1 =
                    arg1?.applicationInfo?.loadLabel(packageManager).toString()
                name0.compareTo(name1, ignoreCase = true)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_package_selector)

        packagesRecyclerView = findViewById(R.id.packages_list)
        toolbar = findViewById(R.id.package_selector_menu_toolbar)
        searchMenu = toolbar.menu.findItem(R.id.package_search)
        searchView = searchMenu.actionView as SearchView
        
        val collapsingToolbar = findViewById<CollapsingToolbarLayout>(R.id.collapsing_toolbar_layout)
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

        if (NavigationModeUtils.isFullGestures(context)) {
            window?.let { WindowCompat.setDecorFitsSystemWindows(it, false) }
            val titleToolbar = findViewById<Toolbar>(R.id.title_toolbar)
            ViewCompat.setOnApplyWindowInsetsListener(window!!.decorView!!) { view, inset ->
                val topInset = WindowInsetsCompat(inset).getInsets(WindowInsetsCompat.Type.systemBars()).top
                (titleToolbar.layoutParams as ViewGroup.MarginLayoutParams).topMargin = topInset
                (collapsingToolbar.layoutParams as ViewGroup.MarginLayoutParams).topMargin = topInset
                return@setOnApplyWindowInsetsListener inset
            }
        }
    }

    override fun show() {
        super.show()
        job?.cancel()
        job = CoroutineScope(Dispatchers.Default).launch() {
            val packagesList = packageManager.getInstalledPackages(0)
            Collections.sort(packagesList, nameComparator)
            try {
                val packagesAdapter = PackageItemAdapter(
                    packageManager,
                    packagesList,
                    this@PackageSelectorDialog
                )
                withContext(Dispatchers.Main) {
                    packagesAdapter.setupSearchFilter()
                    packagesRecyclerView.layoutManager = LinearLayoutManager(context)
                    packagesRecyclerView.adapter = packagesAdapter
                    findViewById<View>(R.id.loading_spinner).visibility = View.GONE
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
    }

    override fun dismiss() {
        super.dismiss()
        job?.cancel()
    }
}