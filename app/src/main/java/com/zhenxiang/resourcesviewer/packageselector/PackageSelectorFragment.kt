package com.zhenxiang.resourcesviewer.packageselector

import android.content.pm.PackageInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
class PackageSelectorFragment : Fragment() {

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

        val nameComparator = object : Comparator<PackageInfo?> {
            override fun compare(arg0: PackageInfo?, arg1: PackageInfo?): Int {
                val name0 =
                    arg0?.applicationInfo?.loadLabel(requireContext().packageManager).toString()
                val name1 =
                    arg1?.applicationInfo?.loadLabel(requireContext().packageManager).toString()
                if (name0 == null && name1 == null) {
                    return 0
                }
                if (name0 == null) {
                    return -1
                }
                return if (name1 == null) {
                    1
                } else name0.compareTo(name1, ignoreCase = true)
            }
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