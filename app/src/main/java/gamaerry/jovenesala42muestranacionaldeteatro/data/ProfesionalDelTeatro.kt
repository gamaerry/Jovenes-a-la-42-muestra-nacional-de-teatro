package gamaerry.jovenesala42muestranacionaldeteatro.data

data class ProfesionalDelTeatro(
    val nombre: String,
    val especialidades: List<Especialidad>,
    val descripcion: String,
    val id: Long,
    val urlImagen: String
) {
    fun getStringDeEspecialidades(): String {
        return when (especialidades.size) {
            1 -> especialidades.first().capitalize()
            2 -> "${especialidades.first().capitalize()} y ${especialidades.last()}"
            else ->
                "${especialidades.first().capitalize()}, ${
                    especialidades.drop(1).dropLast(1).joinToString()
                } y ${especialidades.last()}"
        }
    }
}
