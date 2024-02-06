package com.condex.myfavouritespets.ui.activity

import android.Manifest
import android.R
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import com.condex.myfavouritespets.model.pet.PetDbHelper
import com.condex.myfavouritespets.model.pet.SQLitePetDataSource

import com.condex.myfavouritespets.utils.FileManager
import com.google.android.material.snackbar.Snackbar
import java.io.File

class PetCrudActivity: AppCompatActivity() {
    private lateinit var binding: ActivityPetCrudBinding
    private lateinit var dataSource: SQLitePetDataSource

    private val MY_PERMISSIONS_REQUEST_CAPTURE_CAMERA = 1223432
    private val MY_PERMISSIONS_REQUEST_CODE = 123456

    private var photo: String? = null
    private var petId: Int? = null
    private var createNew: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPetCrudBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataSource = SQLitePetDataSource(PetDbHelper(this))

        petId = intent?.getIntExtra(EXTRA_ID, -1) ?: -1
        if (petId != -1) {
            loadPetData(petId!!)
        }

        configView()
        setListener()
    }

    private fun loadPetData(id: Int) {
        val pet = dataSource.getPet(id) // Modificación aquí
        // Actualiza la UI con los datos de la mascota
        val imgFile = File(pet.foto)
        if (imgFile.exists()) {
            val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
            binding.imgButton.setImageBitmap(myBitmap)
        }

        binding.Name.setText(pet.Nombre)
        binding.Clase.editText?.setText(pet.clase)
        binding.Pelaje.editText?.setText(pet.tipoPelaje)
        binding.enlaceweb.editText?.setText(pet.enlacePagWeb)
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
            val Nombre = binding.Name.text.toString()
            val Pelaje = binding.Pelaje.editText?.text.toString()
            val Nivelamor = binding.editType.editText?.text.toString()
            val comentario = binding.enlaceweb.editText?.text.toString()
            val clase = binding.Clase.editText?.text.toString()
            val favorito= binding.checkboxFavorito.isChecked
            val web= binding.enlaceweb.editText?.text.toString()

            savePet(photo, Nombre, Pelaje, Nivelamor, comentario, clase, favorito,web)
        }
    }

    private fun savePet(
        photo: String?,
        Nombre: String,
        pelaje: String,
        Nivelamor: String,
        comentario: String,
        clase: String,
        favorito: Boolean,
        web: String
    ) {
        when {
            photo == null -> showSnackbar("El campo foto no puede estar vacío.")
            Nombre.isEmpty() -> showSnackbar("El campo nombre no puede estar vacío.")
            pelaje.isEmpty() -> showSnackbar("El campo pelaje no puede estar vacío.")
            Nivelamor.isEmpty() -> showSnackbar("El campo nivel de amor no puede estar vacío.")
            comentario.isEmpty() -> showSnackbar("El campo comentario no puede estar vacío.")
            clase.isEmpty() -> showSnackbar("El campo clase no puede estar vacío.")
            else -> {
                try {
                    val tipoPet = NivelAmor.valueOf(Nivelamor)
                    val pet = Pet(
                        id = petId ?: 0, // Usar petId si está editando, o 0 si está creando uno nuevo
                        Nombre = Nombre,
                        tipoPelaje = pelaje,
                        clase = clase,
                        foto = photo ?: "",
                        nivelAmorosidad = tipoPet,
                        enlacePagWeb = web,
                        favotiro = favorito
                    )

                    // Determina si es una nueva mascota o una actualización
                    createNew = petId == null || petId == -1

                    if (createNew) {
                        // Añadir nuevo Pet
                        dataSource.addPet(pet)
                    } else {
                        // Actualizar Pet existente
                        dataSource.updatePet(pet.id, pet)
                    }

                    finish()
                } catch (e: IllegalArgumentException) {
                    showSnackbar("Valor no válido para el nivel de amor.")
                }
            }
        }
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MY_PERMISSIONS_REQUEST_CODE && resultCode == RESULT_OK) {
            val thumbnail: Bitmap = data?.extras?.get("data") as Bitmap
            binding.imgButton.setImageBitmap(thumbnail)
            // Guarda la foto y obtiene la ruta al archivo
            photo = FileManager.saveBitmapToFile(this, thumbnail, "photo_pet.png")
        }
    }

    companion object {
        val EXTRA_ID: String="id"
    }
}