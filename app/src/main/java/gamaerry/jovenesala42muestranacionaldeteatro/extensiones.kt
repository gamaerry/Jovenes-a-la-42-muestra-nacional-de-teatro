package gamaerry.jovenesala42muestranacionaldeteatro

fun String.extraerLista(): List<String>{
    return split(",", "y").map { string ->
        string.trim().replaceFirstChar { it.uppercaseChar() }
    }
}