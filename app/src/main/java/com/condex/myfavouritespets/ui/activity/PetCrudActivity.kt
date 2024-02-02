package com.condex.myfavouritespets.ui.activity

import android.Manifest
import android.R
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.condex.myfavouritespets.databinding.ActivityPetCrudBinding
import com.condex.myfavouritespets.model.DataSource
import com.condex.myfavouritespets.model.pet.NivelAmor
import com.condex.myfavouritespets.model.pet.Pet
import com.condex.myfavouritespets.utils.DialogManager
import com.condex.myfavouritespets.utils.FileManager

class PetCrudActivity: AppCompatActivity() {
    private lateinit var binding: ActivityPetCrudBinding

    private val MY_PERMISSIONS_REQUEST_CAPTURE_CAMERA = 1223432
    private val MY_PERMISSIONS_REQUEST_CODE = 123456

    private var photo: String? = null
    private var petId: Int? = null
    private var createNew: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPetCrudBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configView()
        setListener()

    }

    private fun configView() {
        setSupportActionBar(binding.myToolbar)

        val adapter = ArrayAdapter(
            this,
            R.layout.simple_spinner_dropdown_item,
            NivelAmor.values().map { it.name }
        )
        binding.spinner.setAdapter(adapter)

    }

    private fun setListener(){
        binding.imgButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.CAMERA),
                    MY_PERMISSIONS_REQUEST_CAPTURE_CAMERA
                )
            } else {
                // Si tiene condedido el permiso previamente
                val intent = Intent(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
                startActivityForResult(intent, MY_PERMISSIONS_REQUEST_CODE)
            }
        }
        binding.button.setOnClickListener {
            val address = binding.Name.text.toString()
            val Pelaje = binding.Pelaje.editText?.text.toString()
            val TipoAmor = binding.editType.editText?.text.toString()
            val comentario = binding.enlaceweb.editText?.text.toString()
            val clase = binding.Clase.editText?.text.toString()
            val favorito= binding.button.isOrWillBeHidden

            savePet(photo, address, Pelaje, TipoAmor, comentario, clase, favorito)
        }
    }

    private fun savePet(
        photo: String?,
        address:String,
        pelaje: String,
        TipoAmor: String,
        comentario: String,
        clase: String,
        favorito: Boolean

    ){
        if (controlPet(photo, address, pelaje, TipoAmor, comentario, clase, favorito)){
            DataSource.petDataSource().addPet(
                this,
                Pet(
                    0,
                    address,
                    photo!!,
                    pelaje,
                    comentario,
                    NivelAmor.valueOf(TipoAmor),
                    clase,
                    favorito,
                )
            )
            finish()
        }
    }

    private fun controlPet(
        photo: String?,
        address: String,
        pelaje: String,
        TipoAmor: String,
        comentario: String,
        clase: String,
        favorito: Boolean
    ): Boolean{
        var accepted = true
        if(photo == null){
            DialogManager.alertDialog(
                this,
                "Faltan datos",
                "El campo foto no puede estar vacio."
            )
            accepted = false
        }
        if(address == null){
            DialogManager.alertDialog(this, "Faltan datos", "El campo nombre no puede estar vacio.")
            accepted = false
        }
        if(pelaje == null) {
            DialogManager.alertDialog(
                this,
                "Faltan datos",
                "El campo pelaje no puede estar vacio."
            )
            accepted = false
        }
        if (TipoAmor.isEmpty()) {
            DialogManager.alertDialog(
                this,
                "Faltan datos",
                "El campo Nivel de amor no puede estar vacio."
            )
            accepted = false
        }
        return accepted
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_CAPTURE_CAMERA -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Log.d("DEBUG", "Permiso concedido!!")
                    val intent = Intent(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
                    startActivityForResult(intent, MY_PERMISSIONS_REQUEST_CODE)
                } else {
                    Log.d("DEBUG", "Permiso rechazado!!")
                }
                return
            }

            else -> {
                Log.d("DEBUG", "Se pasa de los permisos.")
            }
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int, data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === MY_PERMISSIONS_REQUEST_CODE && resultCode === RESULT_OK) {
            val thumbnail: Bitmap = data?.getParcelableExtra("data")!!
            binding.imgButton.setImageBitmap(thumbnail)
            photo = FileManager.saveBitmapToFile(this, thumbnail,"photo.png")
        }
    }

    companion object {
        val EXTRA_ID: String="id"
    }
}