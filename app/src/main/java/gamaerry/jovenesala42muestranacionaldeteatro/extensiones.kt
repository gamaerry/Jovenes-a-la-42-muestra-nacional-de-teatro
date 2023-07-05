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

val Activity.usuario: String?
    get() = myPrefs.getString("usuario", null)

fun Activity.setUsuario(usuarioBuscado: String?) {
    myPrefs.edit().putString("usuario", usuarioBuscado).apply()
}

fun Activity.addGuardado(id: String) {
    myPrefs.edit().putStringSet(
        "guardados",
        guardados!!.toMutableSet().apply { add(id) }
    ).apply()
}

fun Activity.removeGuardado(id: String) {
    myPrefs.edit().putStringSet(
        "guardados",
        guardados!!.toMutableSet().apply { remove(id) }
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
