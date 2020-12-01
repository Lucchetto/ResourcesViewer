package com.zhenxiang.resourcesviewer.packageselector

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.Nullable
import com.zhenxiang.resourcesviewer.R

class PackageView : LinearLayout {

    private val packageIcon : ImageView
    private val packageLabel : TextView
    private val packageName : TextView

    constructor (context: Context) : this(context, null) {
    }

    constructor(context: Context, @Nullable attrs: AttributeSet?) : this(context, attrs, 0) {
    }

    constructor(context: Context, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0) {
    }

    constructor(context: Context, @Nullable attrs: AttributeSet?, defStyleAttr: Int, defStyleRes : Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        LayoutInflater.from(context).inflate(R.layout.package_view, this, true);
        packageIcon = findViewById(R.id.package_icon)
        packageLabel = findViewById(R.id.package_label)
        packageName = findViewById(R.id.package_name)
        packageLabel.isSelected = true
    }

    fun setPackageInfo(icon : Drawable, label : String, name : String) {
        packageIcon.setImageDrawable(icon)
        packageLabel.text = label
        packageName.text = name
    }

    fun getPackageName() : String {
        return packageName.text.toString()
    }
}