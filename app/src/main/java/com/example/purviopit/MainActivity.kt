package com.example.purviopit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)

        val adapter = ViewPagerAdapter(this)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            // setting the text for each tab based on the their position.
            tab.text = when (position) {
                0 -> "Fridge"
                1 -> "New Product"
                2 -> "Recipes"
                else -> null
            }
        }.attach()
        //establishes the relationship between the tabs in the TabLayout and the pages in the ViewPager.
    }

    class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
        //provide the fragments that will be displayed in each tab of the ViewPager2.
        private val fragments: Array<Fragment> = arrayOf(FragmentOne(), FragmentTwo(), FragmentThree())

        override fun getItemCount(): Int = fragments.size

        override fun createFragment(position: Int): Fragment = fragments[position]
        //Depending on this position, the method returns the corresponding fragment from the fragments array.
    }
}