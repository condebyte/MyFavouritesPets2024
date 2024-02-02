package com.condex.myfavouritespets.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import com.condex.myfavouritespets.databinding.FragmentPetListBinding
import com.condex.myfavouritespets.model.DataSource
import com.condex.myfavouritespets.model.pet.NivelAmor
import com.condex.myfavouritespets.model.pet.Pet
import com.condex.myfavouritespets.ui.NavigationManager
import com.condex.myfavouritespets.ui.adapters.PetListAdapter
import java.util.Locale.filter

class PetListFragment: Fragment() {

    private lateinit var binding: FragmentPetListBinding
    private lateinit var ctx: Context
    private lateinit var adapter: PetListAdapter

    private var filter: NivelAmor? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ctx= context
    }

    override fun onCreateView(
        inflater : LayoutInflater,
        container: ViewGroup?,
        savedInstanceState:Bundle?
    ): View? {
        binding = FragmentPetListBinding.inflate(inflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        configView()
        setListener()
    }

    private fun setListener() {
        binding.listview.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val item = adapter.getItem(position)
                if (item is Pet) {
                    val pet: Pet = item
                    NavigationManager.openPetDetail(ctx, pet.id)
                }
            }
        adapter.onDeleteClickListener= object :PetListAdapter.OnDeleteClickListener{
            override fun onChucheDeleteClick(pet: Pet) {
                DataSource.petDataSource().borrarPet(ctx, pet.id)
                filter()
            }
        }

    }

    private fun configView() {
        val pets = ArrayList<Pet>()
        adapter = PetListAdapter(ctx, pets)
        binding.listview.adapter = adapter
        binding.listview.emptyView = binding.txtEmptyview
        filter()
    }

    private fun filter() {
        var accidentes = DataSource.petDataSource().listPet(ctx)
        if (filter != null) {
            accidentes = accidentes.filter { it.nivelAmorosidad == filter }
        }
        adapter.setData(accidentes)
    }
    fun filterGrave() {
        filter = NivelAmor.Mucho
        filter()
    }

    fun filterMedium() {
        filter = NivelAmor.Medio
        filter()
    }

    fun filterLight() {
        filter = NivelAmor.Poco
        filter()
    }

    fun filterAll() {
        filter = null
        filter()
    }


}