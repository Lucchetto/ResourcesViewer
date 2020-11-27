package com.zhenxiang.resourcesviewer.packageselector

import android.app.Activity
import android.content.pm.PackageInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AutoCompleteTextView
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.revengeos.revengeui.fragment.FullscreenDialogFragment
import com.zhenxiang.resourcesviewer.R
import kotlinx.android.synthetic.main.fragment_package_selector.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [PackageSelectorFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PackageSelectorFragment : FullscreenDialogFragment() {

    val TAG = this.javaClass.name

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

        val nameComparator = Comparator<PackageInfo> { arg0, arg1 ->
            val name0 =
                arg0?.applicationInfo?.loadLabel(requireContext().packageManager).toString()
            val name1 =
                arg1?.applicationInfo?.loadLabel(requireContext().packageManager).toString()
            name0.compareTo(name1, ignoreCase = true)
        }

        val collapsingToolbar = contentView.findViewById<CollapsingToolbarLayout>(R.id.collapsing_toolbar_layout)
        val toolbar = contentView.findViewById<Toolbar>(R.id.package_selector_menu_toolbar)
        toolbar.setOnMenuItemClickListener {
            collapsingToolbar.isTitleEnabled = false
            if (it.itemId == R.id.app_bar_search) {
                val searchView = it.actionView as SearchView
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

        packagesRecyclerView.layoutManager = LinearLayoutManager(context)
        CoroutineScope(Dispatchers.IO).launch {
            val packagesList = requireContext().packageManager.getInstalledPackages(0)
            Collections.sort(packagesList, nameComparator)
            val packagesAdapter = PackageItemAdapter(requireContext(), packagesList, requireFragmentManager(), this@PackageSelectorFragment)
            withContext(Dispatchers.Main) {
                packagesRecyclerView.adapter = packagesAdapter
                loading_spinner.visibility = View.GONE
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