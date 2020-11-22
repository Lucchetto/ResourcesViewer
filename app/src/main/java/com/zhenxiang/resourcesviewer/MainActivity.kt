package com.zhenxiang.resourcesviewer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.zhenxiang.resourcesviewer.util.ResourcesUtils

class MainActivity : AppCompatActivity() {
    private lateinit var searchButton : Button
    private lateinit var resNameView : EditText
    private lateinit var resIdView : TextView
    private lateinit var resTypeView : TextView
    private lateinit var resValueView : TextView
    private lateinit var resTypeSpinner : Spinner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        searchButton = findViewById(R.id.search_button)
        resNameView = findViewById(R.id.resource_name)
        resIdView = findViewById(R.id.resource_id)
        resTypeView = findViewById(R.id.resource_type)
        resValueView = findViewById(R.id.resource_value)
        resTypeSpinner = findViewById(R.id.resource_type_picker)

        val spinnerArray : MutableList<String> = ArrayList()
        spinnerArray.add("Auto detect")
        spinnerArray.addAll(ResourcesUtils.resTypes)
        val resTypesAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerArray)
        resTypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        resTypeSpinner.adapter = resTypesAdapter

        searchButton.setOnClickListener { view ->
            val resName = resNameView.text.toString()
            val resource : Resource?
            if (resTypeSpinner.selectedItemPosition == 0) {
                resource = ResourcesUtils.getAndroidResourceByName(resName, this)
            } else {
                resource = ResourcesUtils.getAndroidResourceByName(resName, resTypeSpinner.selectedItem.toString(), this)
            }
            if (resource == null) {
                resIdView.text = "Resource not found"
                resTypeView.text = ""
                resValueView.text = ""
            } else {
                resIdView.text = "Id: ${resource.resId}";
                resTypeView.text = "Type: ${resource.resType}"
                resValueView.text = "Value: ${ResourcesUtils.getResourceValue(resource.resId, resource.resType, this)}"
            }
        }
    }
}