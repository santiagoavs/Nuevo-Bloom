package RecyclerViewHelpers

import Modelo.ClaseConexion
import Modelo.dataClass_Enfermedades
import Modelo.dataClass_Pacientes
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sandoval.santiago.hospitalbloom.R

class Adaptador(var Datos: List<dataClass_Pacientes>): RecyclerView.Adapter<ViewHolder>() {

    fun eliminarPacientes(idPaciente: Int, position: Int){
        val listaPacientes = Datos.toMutableList()
        listaPacientes.removeAt(position)
        CoroutineScope(Dispatchers.IO).launch {
            val objConexion = ClaseConexion().CadenaConexion()
            val deletePaciente = objConexion?.prepareStatement("delete from tbPacientes where idPaciente = ?")!!
            deletePaciente.setInt(1, idPaciente)
            deletePaciente.executeUpdate()
            val commit = objConexion.prepareStatement( "commit")
            commit.executeUpdate()
        }
        Datos=listaPacientes.toList()
        notifyItemRemoved(position)
        notifyDataSetChanged()
    }

    suspend fun obtenerNombreEnfermedad(idEnfermedad: Int): String? {
        return withContext(Dispatchers.IO) {
            var nombreEnfermedad: String? = null
            try {
                val objConexion = ClaseConexion().CadenaConexion()
                val statement = objConexion?.createStatement()
                val query = "SELECT nombre FROM tbEnfermedades WHERE idEnfermedad = $idEnfermedad"
                val resultSet = statement?.executeQuery(query)

                if (resultSet != null && resultSet.next()) {
                    nombreEnfermedad = resultSet.getString("nombre")
                }

                resultSet?.close()
                statement?.close()
                objConexion?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            nombreEnfermedad
        }
    }
    suspend fun obtenerNombreMedicamento(idMedicamento: Int): String? {
        return withContext(Dispatchers.IO) {
            var nombreMedicamento: String? = null
            try {

                val objConexion = ClaseConexion().CadenaConexion()
                val statement = objConexion?.createStatement()
                val query = "SELECT nombre FROM tbMedicamentos WHERE idMedicamento = $idMedicamento"
                val resultSet = statement?.executeQuery(query)

                if (resultSet != null && resultSet.next()) {
                    nombreMedicamento = resultSet.getString("nombre")
                }

                resultSet?.close()
                statement?.close()
                objConexion?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            nombreMedicamento
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista =LayoutInflater.from( parent.context).inflate(R.layout.activity_item_card, parent, false)
        return ViewHolder(vista)
    }

    override fun getItemCount() = Datos.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = Datos[position]
        holder.tvItemNombrePaciente.text = "${item.Nombres} ${item.Apellidos}"
        holder.tvItemEdadPaciente.text = "Edad: ${item.Edad}"
        holder.tvItemNumHabitacion.text = "Hab. ${item.numeroHabitacion} Cama: ${item.numeroCama}"
        CoroutineScope(Dispatchers.Main).launch {
            val enfermedad = obtenerNombreEnfermedad(item.idEnfermedad)
            holder.tvItemEnfermedadPaciente.text = "Padece: $enfermedad"
            val medicamento = obtenerNombreMedicamento(item.idMedicamento)
            holder.tvItemMedicacionHora.text = "$medicamento a las ${item.horaAplicacion}"
        }
        holder.img_borrar.setOnClickListener {
            eliminarPacientes(item.idPaciente, position)
        }
    }
}