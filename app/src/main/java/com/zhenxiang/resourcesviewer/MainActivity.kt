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
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragment = ResourcesValueViewerFragment()
        fragmentTransaction.add(R.id.content_root, fragment)
        fragmentTransaction.commit()
    }
}