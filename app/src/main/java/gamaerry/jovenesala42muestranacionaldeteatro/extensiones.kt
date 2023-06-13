package gamaerry.jovenesala42muestranacionaldeteatro

// funcion de extension para obtener la lista de
// especialidades de cada entidad ProfesionalDelTeatro,
// que usaran los botones del DetallesProfesionalesFragment
fun String.extraerLista(): List<String>{
    return split(",", "y").map { string ->
        string.trim().replaceFirstChar { it.uppercaseChar() }
    }
}