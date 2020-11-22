package com.zhenxiang.resourcesviewer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.zhenxiang.resourcesviewer.util.ResourcesUtils

/**
 * A simple [Fragment] subclass.
 * Use the [ResourcesValueViewerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ResourcesValueViewerFragment : Fragment() {
    private lateinit var searchButton : Button
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
        val contentView = inflater.inflate(R.layout.fragment_resources_value_viewer, container, false)
        searchButton = contentView.findViewById(R.id.search_button)
        resNameView = contentView.findViewById(R.id.resource_name)
        packageNameView = contentView.findViewById(R.id.package_name)
        resIdView = contentView.findViewById(R.id.resource_id)
        resTypeView = contentView.findViewById(R.id.resource_type)
        resValueView = contentView.findViewById(R.id.resource_value)
        resTypeSpinner = contentView.findViewById(R.id.resource_type_picker)

        val spinnerArray : MutableList<String> = ArrayList()
        spinnerArray.add("Auto detect")
        spinnerArray.addAll(ResourcesUtils.resTypes)
        val resTypesAdapter = ArrayAdapter(this.requireContext(), android.R.layout.simple_spinner_item, spinnerArray)
        resTypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        resTypeSpinner.adapter = resTypesAdapter

        searchButton.setOnClickListener { view ->
            val resName = resNameView.text.toString()
            val resource : Resource?
            resource = if (resTypeSpinner.selectedItemPosition == 0) {
                ResourcesUtils.getResourceByName(resName, "android", this.requireContext())
            } else {
                ResourcesUtils.getResourceByName(resName, resTypeSpinner.selectedItem.toString(), "android", this.requireContext())
            }
            if (resource == null) {
                packageNameView.text = ""
                resIdView.text = "Resource not found"
                resTypeView.text = ""
                resValueView.text = ""
            } else {
                packageNameView.text = "Package name: ${resource.packageName}"
                resIdView.text = "Id: 0x${Integer.toHexString(resource.resId)}";
                resTypeView.text = "Type: ${resource.resType}"
                resValueView.text = "Value: ${ResourcesUtils.getResourceValue(resource.resId, resource.resType, this.requireContext())}"
            }
        }
        return contentView
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ResourcesValueViewerFragment().apply {
            }
    }
}