package com.zhenxiang.resourcesviewer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * A simple [Fragment] subclass.
 * Use the [ResourcesExplorerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ResourcesExplorerFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val contentView = inflater.inflate(R.layout.fragment_resources_explorer, container, false)
        return contentView
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ResourcesExplorerFragment().apply {
            }
    }
}