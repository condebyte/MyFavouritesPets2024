package com.condex.myfavouritespets.ui.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.condex.myfavouritespets.R
import com.condex.myfavouritespets.databinding.ActivityHomeBinding
import com.condex.myfavouritespets.ui.NavigationManager
import com.condex.myfavouritespets.ui.adapters.FragmentAdapter
import com.condex.myfavouritespets.ui.fragments.FavouritePetsFragment
import com.condex.myfavouritespets.ui.fragments.PetListFragment
import com.google.android.material.tabs.TabLayoutMediator

class HomeActivity: AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var adapter: FragmentAdapter
    private var petListFragment: PetListFragment? = null
    private lateinit var favouritePetsFragment: FavouritePetsFragment

    private var activeFragment = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configView()
        setListener()
    }

    private fun configView() {
        setSupportActionBar(findViewById(R.id.my_toolbar))
        adapter = FragmentAdapter(supportFragmentManager, lifecycle)
        petListFragment = PetListFragment()
        favouritePetsFragment = FavouritePetsFragment()
        adapter.addFragment(petListFragment!!,"Mascotas")


        binding.viewPager2.adapter = adapter


        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            when (position) {
                0 -> tab.text = "Mascotas"
                1 -> tab.text = "Favoritas" // El nombre de la pestaña para el nuevo fragmento
            }
        }.attach()
        binding.viewPager2.currentItem = 0

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_filter, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.filter_all -> {
                petListFragment?.filterAll()
                return true
            }
            R.id.filter_grave -> {
                petListFragment?.filterGrave()
                return true
            }

            R.id.filter_medium -> {
                petListFragment?.filterMedium()
                return true
            }

            R.id.filter_light -> {
                petListFragment?.filterLight()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setListener() {

        binding.button.setOnClickListener {
            when (activeFragment) {
                0 -> {
                    NavigationManager.openPetCreate(this)
                }
            }
        }
        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                activeFragment = position
            }
        })
    }
}