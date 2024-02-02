package com.condex.myfavouritespets.model

import com.condex.myfavouritespets.model.pet.FilePetDataSource
import com.condex.myfavouritespets.model.pet.IPetDataSource

object DataSource {

    fun petDataSource(): IPetDataSource{
        return FilePetDataSource("pet.json")
    }
}