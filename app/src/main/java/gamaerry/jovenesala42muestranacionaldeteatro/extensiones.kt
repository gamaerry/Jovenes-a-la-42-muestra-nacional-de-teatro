package gamaerry.jovenesala42muestranacionaldeteatro

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

// funcion de extension para obtener la lista de
// especialidades de cada entidad ProfesionalDelTeatro,
// que usaran los botones del DetallesProfesionalesFragment
fun String.extraerLista(): List<String>{
    return split(",", "y").map { string ->
        string.trim().replaceFirstChar { it.uppercaseChar() }
    }
}

// ocultarTeclado() se usa a la hora de presionar "buscar" en el fragmento principal
fun View.ocultarTeclado() {
    val manejadorDelInputMethod =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    manejadorDelInputMethod.hideSoftInputFromWindow(windowToken, 0)
    this.clearFocus()
}
