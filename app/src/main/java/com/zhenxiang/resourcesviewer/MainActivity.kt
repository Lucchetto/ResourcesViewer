package com.zhenxiang.resourcesviewer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zhenxiang.resourcesviewer.packageselector.PackageSelectorFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.content_root, ResourcesValueViewerFragment())
        fragmentTransaction.commit()
    }
}