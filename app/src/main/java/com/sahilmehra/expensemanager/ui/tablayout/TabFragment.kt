package com.sahilmehra.expensemanager.ui.tablayout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.sahilmehra.expensemanager.R
import com.sahilmehra.expensemanager.ui.adapter.ViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_tab.*

class TabFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //set up view pager
        val viewPagerAdapter = ViewPagerAdapter(this, 3)
        val viewPager: ViewPager2 = view.findViewById(R.id.viewPager)
        viewPager.adapter = viewPagerAdapter

        //instantiate tab layout
        val tabLayout: TabLayout = view.findViewById(R.id.tabLayout)

        //attach the tab layout to view pager
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "All"
                1 -> tab.text = "Personal"
                2 -> tab.text = "Business"
            }
        }.attach()

        fabAddTransaction.setOnClickListener {
            //move to add transaction fragment
            findNavController().navigate(TabFragmentDirections.actionTabToAddTransaction())
        }
    }
}