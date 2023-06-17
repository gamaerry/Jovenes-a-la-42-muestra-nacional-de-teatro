package gamaerry.jovenesala42muestranacionaldeteatro

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.view.View
import android.view.inputmethod.InputMethodManager

// se crea una "variable de extension" para tener
// siempre una referencia al SharedPreferences principal
val Activity.myPrefs: SharedPreferences
    get() = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)

val Activity.guardados: MutableSet<String>?
    get() = myPrefs.getStringSet("guardados", emptySet())


fun Activity.getGuardados(): MutableSet<String> {
    return guardados!!
}

fun Activity.setGuardado(id: Int) {
    myPrefs.edit().putStringSet(
        "guardados",
        guardados?.apply { add(id.toString()) }
    ).apply()
}

// funcion de extension para obtener la lista de
// especialidades de cada entidad ProfesionalDelTeatro,
// que usaran los botones del DetallesProfesionalesFragment
fun String.extraerLista(): List<String> {
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
