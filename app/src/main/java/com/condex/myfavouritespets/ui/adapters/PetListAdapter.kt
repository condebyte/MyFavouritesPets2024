package com.condex.myfavouritespets.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.condex.myfavouritespets.R
import com.condex.myfavouritespets.databinding.PetAdapterItemBinding
import com.condex.myfavouritespets.model.DataSource
import com.condex.myfavouritespets.model.pet.Pet
import com.condex.myfavouritespets.ui.NavigationManager
import com.condex.myfavouritespets.utils.FileManager

class PetListAdapter(var context: Context, private var list: List<Pet>) : BaseAdapter()  {
    override fun getCount(): Int {
        return this.list.size
    }

    override fun getItem(position: Int): Any {
        return this.list[position]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(position: Int, p1: View?, p2: ViewGroup?): View {
        val item = this.list[position]
        val inflator = context.getSystemService(
            Context.LAYOUT_INFLATER_SERVICE
        ) as LayoutInflater
        val binding = PetAdapterItemBinding.inflate(inflator)

        binding.txtModel.text = item.nivelAmorosidad.name
        val image = FileManager.loadBitmapFromFilePath(item.foto)
        if (image != null) {
            binding.imageView.setImageBitmap(FileManager.loadBitmapFromFilePath(item.foto))
        } else {
            binding.imageView.setImageResource(R.drawable.ic_launcher_foreground)
        }


        binding.txtLicense.text= item.Nombre
        binding.txtComments.text = item.clase
        binding.btnEdit.setOnClickListener {
            NavigationManager.openPetUpdate(context, item.id)
        }
        binding.btnDelete.setOnClickListener {

            val pet = getItem(position) as Pet
            onDeleteClickListener?.onChucheDeleteClick(pet)

        }
        return binding.root
    }
    fun setData(pets: List<Pet>) {
        this.list = pets
        notifyDataSetChanged()
    }
    interface OnDeleteClickListener {
        fun onChucheDeleteClick(pet: Pet)
    }

    var onDeleteClickListener: OnDeleteClickListener? = null
}