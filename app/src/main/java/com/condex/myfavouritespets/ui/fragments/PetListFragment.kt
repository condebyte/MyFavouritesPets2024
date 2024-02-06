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
import com.condex.myfavouritespets.model.pet.SQLitePetDataSource
import com.condex.myfavouritespets.ui.NavigationManager
import com.condex.myfavouritespets.ui.adapters.PetListAdapter

class PetListFragment : Fragment() {

    private lateinit var binding: FragmentPetListBinding
    private lateinit var ctx: Context
    private lateinit var dataSource: SQLitePetDataSource
    private lateinit var adapter: PetListAdapter

    private var filter: NivelAmor? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ctx = context
        dataSource = DataSource.petDataSource(ctx) // Asegúrate de que este método devuelve correctamente SQLitePetDataSource
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPetListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        configView()
        setListener()
    }

    private fun setListener() {
        binding.listview.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val petId = adapter.getItemId(position)
            NavigationManager.openPetDetail(ctx, petId.toInt())
        }

        adapter.onDeleteClickListener = object : PetListAdapter.OnDeleteClickListener {
            override fun onChucheDeleteClick(pet: Pet) {
                dataSource.deletePet(pet.id) // Usando el método correcto
                filter()
            }
        }
    }

    private fun configView() {
        filter()
    }

    private fun filter() {
        val pets = if (filter == null) {
            dataSource.listPets() // Usando el método correcto
        } else {
            dataSource.listPets().filter { it.nivelAmorosidad == filter }
        }
        adapter = PetListAdapter(ctx, pets)
        binding.listview.adapter = adapter
        binding.listview.emptyView = binding.txtEmptyview
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