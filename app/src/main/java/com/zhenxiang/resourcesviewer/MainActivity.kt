package com.zhenxiang.resourcesviewer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zhenxiang.resourcesviewer.packageselector.PackageSelectorFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(root_toolbar)
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.content_root, ResourcesValueViewerFragment())
        fragmentTransaction.commit()
    }
}