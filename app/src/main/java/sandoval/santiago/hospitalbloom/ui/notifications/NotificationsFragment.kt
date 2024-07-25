package sandoval.santiago.hospitalbloom.ui.notifications

import Modelo.ClaseConexion
import Modelo.dataClass_Enfermedades
import Modelo.dataClass_Medicamentos
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sandoval.santiago.hospitalbloom.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root


        //Aqui todo

        val txtNombre = binding.txtNombre
        val txtApellidos = binding.txtApellido
        val txtEdad = binding.txtEdad
        val spEnfermedad = binding.spEnfermedad
        val txtNumHabitacion = binding.txtNumHabitacion
        val txtNumCama = binding.txtNumCama
        val spMedicamentos = binding.spMedicamentos
        val txtMedicacionHora = binding.txtMedicacionHora
        val btnRegistrarPaciente = binding.btnRegistrarPaciente


        fun selectEnfermedades(): List<dataClass_Enfermedades>{
            try {
                val objConexion =ClaseConexion().CadenaConexion()
                val statement =objConexion?.createStatement()!!
                val resultSet = statement.executeQuery("select * from tbEnfermedades order by idEnfermedad")
                val lista = mutableListOf<dataClass_Enfermedades>()
                while(resultSet.next()){
                    val idEnfermedad =resultSet.getInt("idEnfermedad")
                    val nombre =resultSet.getString("nombre")
                    val descripcion = resultSet.getString("descripcion")

                    val Enfermedad = dataClass_Enfermedades(idEnfermedad,nombre,descripcion)
                    lista.add(Enfermedad)
                }
                return lista
            } catch (e: Exception){
                Log.w("Error al hacer selectEnfermedades","Error: $e")
                return emptyList()
            }
        }

        fun selectMedicamentos(): List<dataClass_Medicamentos>{
            try {
                val objConexion =ClaseConexion().CadenaConexion()
                val statement =objConexion?.createStatement()!!
                val resultSet = statement.executeQuery("select * from tbMedicamentos order by idMedicamento")
                val lista = mutableListOf<dataClass_Medicamentos>()
                while(resultSet.next()){
                    val idMedicamento =resultSet.getInt("idMedicamento")
                    val nombre =resultSet.getString("nombre")
                    val descripcion = resultSet.getString("descripcion")

                    val Medicamento= dataClass_Medicamentos(idMedicamento,nombre,descripcion)
                    lista.add(Medicamento)
                }
                return lista
            } catch (e: Exception){
                Log.w("Error al hacer selectEnfermedades","Error: $e")
                return emptyList()
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            val listaEnfermedades = selectEnfermedades()
            val nombresEnfermedades =listaEnfermedades.map { it.nombre }
            val listaMedicamentos = selectMedicamentos()
            val nombresMedicamentos = listaMedicamentos.map { it.nombre }
            withContext(Dispatchers.Main){
                val adaptadorEnfermedades =ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, nombresEnfermedades)
                val adaptadorMedicamentos =ArrayAdapter(requireContext(),android.R.layout.simple_spinner_dropdown_item, nombresMedicamentos)
                spEnfermedad.adapter = adaptadorEnfermedades
                spMedicamentos.adapter = adaptadorMedicamentos

            }
        }

        btnRegistrarPaciente.setOnClickListener {
            if(txtNombre.text.isEmpty() || txtApellidos.text.isEmpty() || txtEdad.text.isEmpty() || txtNumHabitacion.text.isEmpty() || txtNumCama.text.isEmpty() || txtMedicacionHora.text.isEmpty()){
                Toast.makeText(requireContext(), "Completa todos los datos", Toast.LENGTH_SHORT).show()
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    val enfermedad = selectEnfermedades()
                    val medicamento = selectMedicamentos()
                    val objConexion = ClaseConexion().CadenaConexion()
                    val registrarPaciente = objConexion?.prepareStatement("insert into tbPacientes (nombres, apellidos, edad, idEnfermedad, numeroHabitacion,numeroCama,idMedicamento,horaAplicacion) values(?,?,?,?,?,?,?,?)")!!
                    registrarPaciente.setString(1, txtNombre.text.toString())
                    registrarPaciente.setString(2,txtApellidos.text.toString())
                    registrarPaciente.setInt(3, txtEdad.text.toString().toInt())
                    registrarPaciente.setInt(4, enfermedad[spEnfermedad.selectedItemPosition].idEnfermedad)
                    registrarPaciente.setInt(5,txtNumHabitacion.text.toString().toInt())
                    registrarPaciente.setInt(6, txtNumCama.text.toString().toInt())
                    registrarPaciente.setInt(7, medicamento[spMedicamentos.selectedItemPosition].idMedicamento)
                    registrarPaciente.setString(8, txtMedicacionHora.text.toString())
                    registrarPaciente.executeUpdate()
                    val commit = objConexion.prepareStatement("commit")
                    commit.executeUpdate()
                    withContext(Dispatchers.Main){
                        txtNombre.setText("")
                        txtApellidos.setText("")
                        txtEdad.setText("")
                        txtNumHabitacion.setText("")
                        txtNumCama.setText("")
                        txtMedicacionHora.setText("")
                    }
                }
            }

        }

        return root
    }







    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}