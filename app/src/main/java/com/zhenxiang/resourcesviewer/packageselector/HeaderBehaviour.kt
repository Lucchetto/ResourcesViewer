package com.zhenxiang.resourcesviewer.packageselector

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout
import com.zhenxiang.resourcesviewer.R

class HeaderBehaviour(
    private val mContext: Context,
    attrs: AttributeSet?
) : CoordinatorLayout.Behavior<Toolbar>(mContext, attrs) {
    private var mStartMarginBottom = 0
    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: Toolbar,
        dependency: View
    ): Boolean {
        return dependency is AppBarLayout
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: Toolbar,
        dependency: View
    ): Boolean {
        shouldInitProperties()
        val maxScroll = (dependency as AppBarLayout).totalScrollRange
        val percentage =
            Math.abs(dependency.getY()) / maxScroll.toFloat()
        var childPosition = ((dependency.getHeight()
                + dependency.getY()) - child.height
                - (getToolbarHeight(mContext) - child.height) * percentage / 2)
        childPosition = childPosition - mStartMarginBottom * (1f - percentage)
        child.y = childPosition
        return true
    }

    private fun getTranslationOffset(
        expandedOffset: Float,
        collapsedOffset: Float,
        ratio: Float
    ): Float {
        return expandedOffset + ratio * (collapsedOffset - expandedOffset)
    }

    private fun shouldInitProperties() {
        if (mStartMarginBottom == 0) {
            mStartMarginBottom = mContext.resources
                .getDimensionPixelOffset(R.dimen.header_view_start_margin_bottom)
        }
    }

    companion object {
        fun getToolbarHeight(context: Context): Int {
            var result = 0
            val tv = TypedValue()
            if (context.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                result = TypedValue.complexToDimensionPixelSize(
                    tv.data,
                    context.resources.displayMetrics
                )
            }
            return result
        }
    }

}