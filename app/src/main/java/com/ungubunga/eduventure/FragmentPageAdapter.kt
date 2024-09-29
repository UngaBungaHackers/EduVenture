package com.ungubunga.eduventure

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

private lateinit var adapter: FragmentPageAdapter

class FragmentPageAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return if (position == 0)
            fragment_one()
        else if (position == 1)
            fragment_two()
        else if (position == 2)
            fragment_three()
        else
            fragment_one()
    }

}