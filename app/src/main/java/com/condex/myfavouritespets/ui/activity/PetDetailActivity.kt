package com.condex.myfavouritespets.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.condex.myfavouritespets.R

class PetDetailActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actiity_pet_detail)
        configView()
    }

    companion object {
        val EXTRA_ID: String="ID"
    }

    private fun configView() {
        setSupportActionBar(findViewById(R.id.my_toolbar))
    }
}