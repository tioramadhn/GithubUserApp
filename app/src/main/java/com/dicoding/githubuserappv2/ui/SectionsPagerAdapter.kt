package com.dicoding.githubuserappv2.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    var user: String? = null
    private val mBundle = Bundle()
    override fun getItemCount(): Int = 2
    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> {
                mBundle.putString(FollowersFragment.USER, user)
                fragment = FollowersFragment()
                fragment.arguments = mBundle
            }
            1 -> {
                mBundle.putString(FollowingFragment.USER, user)
                fragment = FollowingFragment()
                fragment.arguments = mBundle
            }
        }
        return fragment as Fragment
    }

}