package gamaerry.jovenesala42muestranacionaldeteatro.model

data class ProfesionalDelTeatro(
    val id: Long = 0L,
    val nombre: String = "",
    val especialidades: String = "",
    val descripcion: String = "",
    val estado: String = "",
    val urlImagen: String = "",
    val contacto1: String = "null",
    val contacto2: String = "null",
    val contacto3: String = "null"
)
