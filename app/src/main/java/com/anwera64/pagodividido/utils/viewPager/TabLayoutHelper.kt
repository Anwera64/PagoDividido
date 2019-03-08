package com.anwera64.pagodividido.utils.viewPager

import android.support.annotation.DrawableRes
import android.support.design.widget.TabLayout
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.content.res.AppCompatResources

object TabLayoutHelper {

    internal interface IconPagerAdapter {

        @DrawableRes
        fun getTabIconRes(position: Int): Int

    }

    fun setupWithViewPager(tabLayout: TabLayout, viewPager: ViewPager) {
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        setTabsFromPagerAdapter(tabLayout, viewPager.adapter)
        tabLayout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewPager))
    }

    private fun setTabsFromPagerAdapter(tabLayout: TabLayout, pagerAdapter: PagerAdapter?) {
        if (pagerAdapter !is IconPagerAdapter) return
        val tabColors = tabLayout.tabTextColors
        for (i in 0 until pagerAdapter.count) {
            val iconRes = (pagerAdapter as IconPagerAdapter).getTabIconRes(i)
            var icon = AppCompatResources.getDrawable(tabLayout.context, iconRes)
            if (icon != null) {
                icon = DrawableCompat.wrap(icon)
                DrawableCompat.setTintList(icon!!, tabColors)
                tabLayout.addTab(
                        tabLayout.newTab()
                                .setText(pagerAdapter.getPageTitle(i))
                                .setIcon(icon)
                )
            }
        }
    }

}