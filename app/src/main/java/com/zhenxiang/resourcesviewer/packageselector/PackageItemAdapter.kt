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
    fragmentManager: FragmentManager,
    fragment: Fragment
) : RecyclerView.Adapter<PackageItemAdapter.ViewHolder>(), Filterable {

    private val packageManager : PackageManager
    private val mInstalledPackages: MutableList<PackageInfo>
    private val mInstalledPackagesAll : List<PackageInfo>
    private val fragmentManager: FragmentManager
    private val fragment: Fragment
    private lateinit var packageFilter : Filter

    init {
        this.packageManager = packageManager
        mInstalledPackages = installedPackages
        mInstalledPackagesAll = mInstalledPackages.toList()
        this.fragmentManager = fragmentManager
        this.fragment = fragment
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val packageInfo = mInstalledPackages[position]
        holder.bind(packageInfo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(
            layoutInflater.inflate(R.layout.package_item, parent, false),
            fragmentManager,
            fragment
        )
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
        private val fragmentManager: FragmentManager,
        private val fragment: Fragment
    ) : RecyclerView.ViewHolder(view) {
        val packageManager = view.context.packageManager
        val packageIcon = view.findViewById<ImageView>(R.id.package_icon)
        val packageLabel = view.findViewById<TextView>(R.id.package_label)
        val packageName = view.findViewById<TextView>(R.id.package_name)
        fun bind(packageInfo: PackageInfo) {
            packageIcon.setImageDrawable(packageInfo.applicationInfo.loadIcon(packageManager))
            packageLabel.text = packageInfo.applicationInfo.loadLabel(packageManager)
            packageName.text = packageInfo.packageName
            view.setOnClickListener { view ->
                Log.d("DIOOOOO", packageInfo.packageName)
                fragmentManager.beginTransaction().remove(fragment).commit()

            }
        }
    }
}

