package com.condex.myfavouritespets.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.condex.myfavouritespets.databinding.FragmentFavouritePetsBinding
import com.condex.myfavouritespets.model.DataSource
import com.condex.myfavouritespets.model.pet.Pet
import com.condex.myfavouritespets.ui.adapters.PetListAdapter

class FavouritePetsFragment: Fragment() {

    private lateinit var binding: FragmentFavouritePetsBinding
    private lateinit var ctx: Context
    private lateinit var adapter: PetListAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ctx = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavouritePetsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        loadFavouritePets()
    }

    private fun loadFavouritePets() {
        val favouritePets = DataSource.petDataSource(ctx).listPets().filter { it.favotiro }
        adapter = PetListAdapter(ctx, favouritePets)
        binding.listview.adapter = adapter
        // Configurando la vista que se muestra cuando la lista está vacía
        binding.listview.emptyView = binding.txtEmptyview
    }
}