package sandoval.santiago.hospitalbloom.ui.home

import Modelo.ClaseConexion
import Modelo.dataClass_Enfermedades
import Modelo.dataClass_Medicamentos
import Modelo.dataClass_Pacientes
import RecyclerViewHelpers.Adaptador
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sandoval.santiago.hospitalbloom.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val rcvPacientes = binding.rcvPacientes
        rcvPacientes.layoutManager = LinearLayoutManager(requireContext())

        fun selectPacientes(): List<dataClass_Pacientes>{
            try {
                val objConexion =ClaseConexion().CadenaConexion()
                val statement =objConexion?.createStatement()!!
                val resultSet = statement.executeQuery("select * from tbPacientes order by idPaciente")
                val lista = mutableListOf<dataClass_Pacientes>()
                while(resultSet.next()){
                    val idPaciente = resultSet.getInt("idPaciente")
                    val nombres = resultSet.getString("nombres")
                    val apellidos = resultSet.getString("apellidos")
                    val edad = resultSet.getInt("edad")
                    val idEnfermedad = resultSet.getInt("idEnfermedad")
                    val numerohabitacion = resultSet.getInt("numerohabitacion")
                    val numerocama = resultSet.getInt("numerocama")
                    val idMedicamento = resultSet.getInt("idMedicamento")
                    val horaaplicacion = resultSet.getString("horaaplicacion")

                    val valoresJuntos = dataClass_Pacientes(idPaciente,nombres,apellidos,edad,idEnfermedad,numerohabitacion,numerocama,idMedicamento,horaaplicacion)
                    lista.add(valoresJuntos)
                }
                return lista
            } catch (e: Exception){
                Log.w("Error al hacer selectEnfermedades","Error: $e")
                return emptyList()
            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            val pacientes = selectPacientes()
            withContext(Dispatchers.Main){
                val adaptador =Adaptador(pacientes)
                rcvPacientes.adapter = adaptador
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}