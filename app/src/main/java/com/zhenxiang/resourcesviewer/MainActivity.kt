package com.zhenxiang.resourcesviewer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.zhenxiang.resourcesviewer.util.ResourcesUtils

class MainActivity : AppCompatActivity() {
    private lateinit var searchButton : Button
    private lateinit var resNameView : EditText
    private lateinit var resIdView : TextView
    private lateinit var resTypeView : TextView
    private lateinit var resValueView : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        searchButton = findViewById(R.id.search_button)
        resNameView = findViewById(R.id.resource_name)
        resIdView = findViewById(R.id.resource_id)
        resTypeView = findViewById(R.id.resource_type)
        resValueView = findViewById(R.id.resource_value)

        searchButton.setOnClickListener { view ->
            val resName = resNameView.text.toString()
            val resource = ResourcesUtils.getAndroidResourceByName(resName, this)
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