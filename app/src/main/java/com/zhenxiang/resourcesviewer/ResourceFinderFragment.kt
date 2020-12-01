package com.zhenxiang.resourcesviewer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.zhenxiang.resourcesviewer.packageselector.PackageSelectorFragment
import com.zhenxiang.resourcesviewer.packageselector.PackageView
import com.zhenxiang.resourcesviewer.util.PackageUtils
import com.zhenxiang.resourcesviewer.util.ResourcesUtils

/**
 * A simple [Fragment] subclass.
 * Use the [ResourceFinderFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ResourceFinderFragment : Fragment() {
    private lateinit var searchButton : Button
    private lateinit var targetPackageView : PackageView
    private lateinit var errorView: TextView
    private lateinit var resDataView : View
    private lateinit var resNameView : EditText
    private lateinit var packageNameView : TextView
    private lateinit var resIdView : TextView
    private lateinit var resTypeView : TextView
    private lateinit var resValueView : TextView
    private lateinit var resTypeSpinner : Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val contentView = inflater.inflate(R.layout.fragment_resource_finder, container, false)
        targetPackageView = contentView.findViewById(R.id.target_package)
        searchButton = contentView.findViewById(R.id.search_button)
        errorView = contentView.findViewById(R.id.error_message)
        resDataView = contentView.findViewById(R.id.resource_data)
        resNameView = contentView.findViewById(R.id.resource_name)
        packageNameView = contentView.findViewById(R.id.package_name)
        resIdView = contentView.findViewById(R.id.resource_id)
        resTypeView = contentView.findViewById(R.id.resource_type)
        resValueView = contentView.findViewById(R.id.resource_value)
        resTypeSpinner = contentView.findViewById(R.id.resource_type_picker)

        val spinnerArray : MutableList<String> = ArrayList()
        spinnerArray.add("Auto detect")
        spinnerArray.addAll(ResourcesUtils.resTypes)
        val resTypesAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, spinnerArray)
        resTypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        resTypeSpinner.adapter = resTypesAdapter

        searchButton.setOnClickListener { view ->
            val resName = resNameView.text.toString()
            val resource : Resource?
            val searchPackage : String

            searchPackage = if (targetPackageView.getPackageName() == "") {
                "android"
            } else {
                targetPackageView.getPackageName()
            }

            if (PackageUtils.isPackageInstalled(requireContext(), searchPackage)) {
                resource = if (resTypeSpinner.selectedItemPosition == 0) {
                    ResourcesUtils.getResourceByName(resName, searchPackage, requireContext())
                } else {
                    ResourcesUtils.getResourceByName(resName, resTypeSpinner.selectedItem.toString(), searchPackage, requireContext())
                }

                if (resource == null) {
                    showError(true, "Resource not found")
                } else {
                    showError(false, null)
                    packageNameView.text = "Package name: ${resource.packageName}"
                    resIdView.text = "Id: 0x${Integer.toHexString(resource.resId)}";
                    resTypeView.text = "Type: ${resource.resType}"
                    resValueView.text = "Value: ${ResourcesUtils.getResourceValue(resource, requireContext())}"
                }
            } else {
                showError(true, "Package not found")
            }
        }

        targetPackageView.setOnClickListener { view ->
            val packageSelector = PackageSelectorFragment(targetPackageView)
            packageSelector.show(requireFragmentManager(), "tag")
        }
        return contentView
    }

    private fun showError(value : Boolean, errorText : String?) {
        errorView.text = errorText
        errorView.visibility = if (value) View.VISIBLE else View.INVISIBLE
        resDataView.visibility = if (value) View.INVISIBLE else View.VISIBLE
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ResourceFinderFragment().apply {
            }
    }
}