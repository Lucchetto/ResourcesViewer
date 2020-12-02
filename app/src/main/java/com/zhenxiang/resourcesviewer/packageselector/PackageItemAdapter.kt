package com.zhenxiang.resourcesviewer.packageselector

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.zhenxiang.resourcesviewer.R

class PackageItemAdapter(
    packageManager: PackageManager,
    installedPackages: MutableList<PackageInfo>,
    dialog : PackageSelectorDialog
) : RecyclerView.Adapter<PackageItemAdapter.ViewHolder>(), Filterable {

    private val packageManager : PackageManager
    private val mInstalledPackages: MutableList<PackageInfo>
    private val mInstalledPackagesAll : List<PackageInfo>
    private val dialog : PackageSelectorDialog
    private lateinit var packageFilter : Filter

    init {
        this.packageManager = packageManager
        mInstalledPackages = installedPackages
        mInstalledPackagesAll = mInstalledPackages.toList()
        this.dialog = dialog
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val packageInfo = mInstalledPackages[position]
        holder.bind(packageInfo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(
            layoutInflater.inflate(R.layout.package_item, parent, false), dialog)
    }

    override fun getItemCount(): Int {
        return mInstalledPackages.size
    }

    fun setupSearchFilter() {
        packageFilter = object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                var filteredList = mutableListOf<PackageInfo>()
                if (charSequence.toString().isEmpty()) {
                    filteredList = mInstalledPackagesAll.toMutableList()
                } else {
                    for (packageInfo in mInstalledPackagesAll) {
                        if (packageInfo.packageName.contains(charSequence, true)
                            or packageInfo.applicationInfo.loadLabel(packageManager).contains(charSequence, true)) {
                            filteredList.add(packageInfo)
                        }
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(charSequence : CharSequence, filteredResults : FilterResults) {
                mInstalledPackages.clear()
                mInstalledPackages.addAll(filteredResults.values as Collection<PackageInfo>)
                notifyDataSetChanged()
            }

        }
    }

    override fun getFilter(): Filter {
        return packageFilter
    }

    class ViewHolder(
        private val view: View,
        private val dialog : PackageSelectorDialog
    ) : RecyclerView.ViewHolder(view) {
        val packageManager = view.context.packageManager
        fun bind(packageInfo: PackageInfo) {
            (view as PackageView).setPackageInfo(packageInfo.applicationInfo.loadIcon(packageManager), packageInfo.applicationInfo.loadLabel(packageManager).toString(), packageInfo.packageName)
            view.setOnClickListener { view ->
                dialog.dismiss()
            }
        }
    }
}

