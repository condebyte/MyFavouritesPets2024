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

    override fun getItemId(position: Int): Long {
        return list[position].id.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding = if (convertView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            PetAdapterItemBinding.inflate(inflater, parent, false)
        } else {
            PetAdapterItemBinding.bind(convertView)
        }
        val item = list[position]

        binding.txtModel.text = item.nivelAmorosidad.name


        val image = FileManager.loadBitmapFromFilePath(item.foto)
        if (image != null) {
            binding.imageView.setImageBitmap(FileManager.loadBitmapFromFilePath(item.foto))
        } else {
            binding.imageView.setImageResource(R.drawable.ic_launcher_foreground)
        }

        if (item.favotiro) {
            binding.imgButtonFav.setImageResource(R.drawable.star_filled)
        } else {
            binding.imgButtonFav.setImageResource(R.drawable.star_unfilled)
        }


        binding.txtLicense.text= item.Nombre
        binding.txtComments.text = item.clase
        binding.btnEdit.setOnClickListener {
            NavigationManager.openPetUpdate(context, item.id)
        }
        binding.btnDelete.setOnClickListener {
            onDeleteClickListener?.onChucheDeleteClick(item)
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