package com.zhenxiang.resourcesviewer.packageselector

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.fragment.app.FragmentManager
import com.zhenxiang.resourcesviewer.R

class PackageSelectorView : FrameLayout {

    private val hintText : TextView
    private val packageView : PackageView

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
    }

    fun setupPackageSelector(packageSelectorFragment : PackageSelectorFragment, fragmentManager : FragmentManager) {
        packageView.setOnClickListener { view ->
            if (fragmentManager.findFragmentByTag("Selector") == null) {
                packageSelectorFragment.show(fragmentManager, "Selector")
            }
        }
    }

    fun setPackageInfo(icon : Drawable, label : String, name : String) {
        hintText.visibility = GONE
        packageView.setPackageInfo(icon, label, name)
    }

    fun getPackageName() : String {
        return packageView.getPackageName()
    }
}