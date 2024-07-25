package RecyclerViewHelpers

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import sandoval.santiago.hospitalbloom.R

class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val tvItemNombrePaciente = view.findViewById<TextView>(R.id.tvItemNombrePaciente)
    val tvItemEdadPaciente = view.findViewById<TextView>(R.id.tvItemEdadPaciente)
    val tvItemEnfermedadPaciente = view.findViewById<TextView>(R.id.tvItemEnfermedadPaciente)
    val tvItemMedicacionHora = view.findViewById<TextView>(R.id.tvItemMedicacionHora)
    val tvItemNumHabitacion = view.findViewById<TextView>(R.id.tvItemNumHabitacion)
    val img_borrar = view.findViewById<ImageView>(R.id.img_borrar)
    val img_editar = view.findViewById<ImageView>(R.id.img_editar)
}