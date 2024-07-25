package Modelo

data class dataClass_Pacientes(
    val idPaciente: Int,
    var Nombres: String,
    var Apellidos: String,
    var Edad: Int,
    var idEnfermedad: Int,
    var numeroHabitacion: Int,
    var numeroCama: Int,
    var idMedicamento: Int,
    var horaAplicacion: String
    )
