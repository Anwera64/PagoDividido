package com.anwera64.pagodividido.utils.viewPager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter

class SimplePagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm),
    TabLayoutHelper.IconPagerAdapter {

    private var fragmentList: MutableList<Fragment>?
    private var titleList: MutableList<String>?
    private var iconList: MutableList<Int>?

    init {
        fragmentList = ArrayList()
        titleList = ArrayList()
        iconList = ArrayList()
    }

    fun addFragment(fragment: Fragment, title: String, drawable: Int) {
        fragmentList!!.add(fragment)
        titleList!!.add(title)
        iconList!!.add(drawable)
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList!![position]
    }

    override fun getCount(): Int {
        return fragmentList!!.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titleList!![position]
    }

    override fun getTabIconRes(position: Int): Int {
        return iconList!![position]
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    fun removeAllFragments() {
        fragmentList = ArrayList()
        titleList = ArrayList()
        iconList = ArrayList()
        notifyDataSetChanged()
    }
}