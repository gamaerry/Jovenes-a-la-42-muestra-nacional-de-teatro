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

// conjunto mutable de los profesionales guardados
val Activity.guardados: MutableSet<String>?
    get() = myPrefs.getStringSet("guardados", emptySet())

// variable string que guarda unicamente el nombre del usuario
val Activity.usuario: String?
    get() = myPrefs.getString("usuario", null)

// el metodo mutador de la varible usuario
fun Activity.setUsuario(usuarioBuscado: String?) {
    myPrefs.edit().putString("usuario", usuarioBuscado).apply()
}

// agrega el mismo conjunto con un nuevo emento id
// (notese que el conjunto guardados guarda solo los id)
fun Activity.addGuardado(id: String) {
    myPrefs.edit().putStringSet(
        "guardados",
        guardados!!.toMutableSet().apply { add(id) }
    ).apply()
}

// agrega el mismo conjunto eliminando el id pasado
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
