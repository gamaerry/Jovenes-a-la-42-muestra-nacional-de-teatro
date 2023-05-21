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
            1 -> especialidades[0].principal()
            2 -> "${especialidades[0].principal()} y ${especialidades[1]}"
            else ->
                "${especialidades[0].principal()}, ${
                    especialidades.drop(1).dropLast(1).joinToString()
                } y ${especialidades[especialidades.lastIndex]}"
        }
    }
}
