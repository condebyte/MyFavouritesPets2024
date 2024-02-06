package com.condex.myfavouritespets.ui

import android.content.Context
import android.content.Intent
import com.condex.myfavouritespets.model.DataSource

import com.condex.myfavouritespets.ui.activity.PetCrudActivity
import com.condex.myfavouritespets.ui.activity.PetDetailActivity

object NavigationManager {

    fun openPetDetail(ctx: Context, id :Int){
        val intent = Intent(ctx, PetDetailActivity::class.java)
        intent.putExtra(PetDetailActivity.EXTRA_ID, id)
        ctx.startActivity(intent)
    }

    fun openPetCreate(ctx: Context) {
        val intent = Intent(ctx, PetCrudActivity::class.java)
        ctx.startActivity(intent)
    }

    fun openPetUpdate(ctx: Context, id :Int) {
        val intent = Intent(ctx, PetCrudActivity::class.java)
        intent.putExtra(PetCrudActivity.EXTRA_ID, id)
        ctx.startActivity(intent)
    }
}