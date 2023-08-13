package gamaerry.jovenesala42muestranacionaldeteatro

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import java.util.Locale

// se crea una "variable de extension" para tener
// siempre una referencia al SharedPreferences principal
val Activity.myPrefs: SharedPreferences
    get() = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)

// conjunto mutable de los profesionales guardados
val Activity.guardados: MutableSet<String>?
    get() = myPrefs.getStringSet("guardados", emptySet())

// variable string que guarda unicamente el nombre del usuario
val Activity.nombre: String?
    get() = myPrefs.getString("nombre", null)

// el metodo mutador de la varible usuario
fun Activity.setNombre(nombreUsuario: String?) {
    myPrefs.edit().putString("nombre", nombreUsuario).apply()
}

// variable string que guarda unicamente el nombre del usuario
val Activity.id: Int
    get() = myPrefs.getInt("id", 0)

// el metodo mutador de la varible usuario
fun Activity.setId(idUsuario: Int) {
    myPrefs.edit().putInt("id", idUsuario).apply()
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

// agrega el mismo conjunto eliminando el id pasado
fun Activity.clearGuardados() {
    myPrefs.edit().putStringSet("guardados", emptySet()).apply()
}

// funcion de extension para obtener la lista de
// especialidades de cada entidad ProfesionalDelTeatro,
// que usaran los botones del DetallesProfesionalesFragment
fun String.extraerLista(): List<String> {
    return split(",", "y", " e ").map { string ->
        string.trim().replaceFirstChar { it.uppercaseChar() }
    }
}

fun List<String>.extraerString(): String {
    return when (size) {
        1 -> get(0)
        2 -> "${get(0)} ${get(1).agregarUltimo()}"
        else ->
            "${get(0)}, ${
                drop(1)
                    .dropLast(1)
                    .joinToString(", ")
                    .lowercase(Locale.ROOT)
            } ${get(lastIndex).agregarUltimo()}"
    }
}

private fun String.agregarUltimo(): String {
    return if (equals("Iluminación")) "e iluminación" else "y ${this.lowercase(Locale.ROOT)}"
}

fun String.extraerUsuarioDeInstagram(): String? {
    return if (contains("instagram.com"))
        "instagram\\.com/([^?/]+)".toRegex().find(this)?.groupValues?.getOrNull(1)
    else this
}

// ocultarTeclado() se usa a la hora de presionar "buscar" en el fragmento principal
fun View.ocultarTeclado() {
    val manejadorDelInputMethod =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    manejadorDelInputMethod.hideSoftInputFromWindow(windowToken, 0)
    this.clearFocus()
}

val Fragment.mainActivity get() = (requireActivity() as MainActivity)

val Fragment.establecerContactos
    get() = listOf<(String) -> Unit>(
        { mainActivity.abrirPerfilFacebook(it) },
        { mainActivity.enviarCorreo(it) },
        { mainActivity.abrirPerfilInstagram(it) },
        { mainActivity.abrirPerfilTikTok(it) },
        { mainActivity.abrirPerfilWhatsApp(it) })

val Fragment.validaciones
    get() = listOf<(String) -> Boolean>(
        { validarFacebook(it) },
        { validarEmail(it) },
        { validarInstagram(it) },
        { validarTikTok(it) },
        { validarNumero(it) })

private fun Fragment.validarFacebook(url: String): Boolean {
    return url.contains("facebook.com/")
}

private fun Fragment.validarEmail(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

private fun Fragment.validarInstagram(url: String): Boolean {
    return url.contains("instagram.com/")
}

private fun Fragment.validarTikTok(url: String): Boolean {
    return url.contains("tiktok.com/")
}

private fun Fragment.validarNumero(numero: String): Boolean {
    return Patterns.PHONE.matcher(numero).matches()
}