package com.zhenxiang.resourcesviewer.packageselector

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.Nullable
import com.zhenxiang.resourcesviewer.R

class PackageSelectorView : FrameLayout {

    val hintText : TextView
    val packageView : PackageView

    constructor (context: Context) : this(context, null) {
    }

    constructor(context: Context, @Nullable attrs: AttributeSet?) : this(context, attrs, 0) {
    }

    constructor(context: Context, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0) {
    }

    constructor(context: Context, @Nullable attrs: AttributeSet?, defStyleAttr: Int, defStyleRes : Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        LayoutInflater.from(context).inflate(R.layout.package_selector_view, this, true);
        hintText = findViewById<TextView>(R.id.hint_text)
        packageView = findViewById<PackageView>(R.id.target_package)

        val packageSelectorDialog = PackageSelectorDialog(context, this)
        packageView.setOnClickListener { view ->
            packageSelectorDialog.show()
        }
    }

    fun getPackageName() : String {
        return packageView.getPackageName()
    }
}