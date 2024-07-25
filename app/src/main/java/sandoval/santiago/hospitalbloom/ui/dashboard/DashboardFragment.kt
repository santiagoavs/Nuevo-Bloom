package sandoval.santiago.hospitalbloom.ui.dashboard

import Modelo.ClaseConexion
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sandoval.santiago.hospitalbloom.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root


        dashboardViewModel.text.observe(viewLifecycleOwner) {
        }
        val txtNomMedicamento = binding.txtNomMedicamento
        val txtDescMedicamento = binding.txtDescMedicamento
        val btnRegistrarMedicamento = binding.btnRegistrarMedicamento

        btnRegistrarMedicamento.setOnClickListener {
            if(txtNomMedicamento.text.isEmpty() || txtDescMedicamento.text.isEmpty()){
                Toast.makeText(requireContext(), "Por favor llena todos los campos", Toast.LENGTH_SHORT).show()
            }
            else {
                CoroutineScope(Dispatchers.IO).launch {
                    val objConexion = ClaseConexion().CadenaConexion()
                    val registrarMedicamento = objConexion?.prepareStatement("insert into tbMedicamentos (nombre, descripcion) values(?,?)")!!
                    registrarMedicamento.setString(1, txtNomMedicamento.text.toString())
                    registrarMedicamento.setString(2, txtDescMedicamento.text.toString())
                    registrarMedicamento.executeUpdate()
                    val commit = objConexion.prepareStatement("commit")
                    commit.executeUpdate()
                    withContext(Dispatchers.Main){
                        Toast.makeText(requireContext(), "Medicamento agregado", Toast.LENGTH_SHORT).show()
                        txtNomMedicamento.setText("")
                        txtDescMedicamento.setText("")
                    }
                }
            }
        }
        val txtNomEnfermedad = binding.txtNomEnfermedad
        val txtDescEnfermedad = binding.txtDescEnfermedad
        val btnRegistrarEnfermedad = binding.btnRegistrarEnfermedad

        btnRegistrarEnfermedad.setOnClickListener {
            if(txtNomEnfermedad.text.isEmpty() || txtDescEnfermedad.text.isEmpty()){
                Toast.makeText(requireContext(), "Por favor llena todos los campos", Toast.LENGTH_SHORT).show()
            }
            else {
                CoroutineScope(Dispatchers.IO).launch {
                    val objConexion = ClaseConexion().CadenaConexion()
                    val registrarEnfermedad = objConexion?.prepareStatement("insert into tbEnfermedades (nombre, descripcion) values(?,?)")!!
                    registrarEnfermedad.setString(1, txtNomEnfermedad.text.toString())
                    registrarEnfermedad.setString(2, txtDescEnfermedad.text.toString())
                    registrarEnfermedad.executeUpdate()
                    val commit = objConexion.prepareStatement("commit")
                    commit.executeUpdate()
                    withContext(Dispatchers.Main){
                        Toast.makeText(requireContext(), "Enfermedad agregada", Toast.LENGTH_SHORT).show()
                        txtNomEnfermedad.setText("")
                        txtDescEnfermedad.setText("")
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