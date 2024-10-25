package com.example.lemmingcli

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.lemmingcli.Fragments.JishoFragment
import com.example.lemmingcli.Fragments.SentenceFragment

class SentenceViewAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = when (position){
            0 -> SentenceFragment()
            1 -> JishoFragment()
            else -> SentenceFragment()
        }
        return fragment
    }
}