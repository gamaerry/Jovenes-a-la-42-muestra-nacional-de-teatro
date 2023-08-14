package gamaerry.jovenesala42muestranacionaldeteatro.datastructure

import android.content.Context
import android.util.Log
import android.util.Patterns
import androidx.core.content.ContextCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import gamaerry.jovenesala42muestranacionaldeteatro.R
import gamaerry.jovenesala42muestranacionaldeteatro.getEspecialidades
import gamaerry.jovenesala42muestranacionaldeteatro.getEstados
import gamaerry.jovenesala42muestranacionaldeteatro.getMuestras
import gamaerry.jovenesala42muestranacionaldeteatro.model.ProfesionalDelTeatro
import javax.inject.Singleton

// el objeto modulo nos sirve para proveer las
// implementaciones especificas de cada tipo
// (clase o interfaz) que se requieran inyectar
// (notese que se instala en el componente
// Singleton porque el viewModelPrincipal es
// el que terminara manejando estos datos y su
// scope es mas general que el del Activity)
@Module
@InstallIn(SingletonComponent::class)
object ModuloPrincipal {
    // la instancia del dao se crea a partir de la base de datos ya proveida
    @Provides
    @Singleton
    fun proveerRepositorio() = RepositorioPrincipal().apply {
        Firebase.database.apply {
            setPersistenceEnabled(true)
            getReference("profesionales")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        dataSnapshot.getValue<HashMap<String, ProfesionalDelTeatro>>()?.let {
                            listaCompleta.value = it.values.toSet()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.d("midebug", "Algo ha fallado :(")
                    }
                })
        }
    }

    @Provides
    @Singleton
    fun proveerItemsFiltros() = listOf(getEstados(), getEspecialidades(), getMuestras())

    @Provides
    @Singleton
    fun proveerEstadosCheckBox(itemsFiltros: @JvmSuppressWildcards List<List<String>>) =
        itemsFiltros.map { it.map { true }.toMutableList() }

    @Provides
    @Singleton
    fun proveerValidaciones() = listOf<(String) -> Boolean>(
        { it.contains("facebook.com/") },
        { Patterns.EMAIL_ADDRESS.matcher(it).matches() },
        { it.contains("instagram.com/") },
        { it.contains("tiktok.com/") },
        { Patterns.PHONE.matcher(it).matches() })

    @Provides
    @Singleton
    fun proveerIconos(@ApplicationContext contexto: Context) = listOf(
        ContextCompat.getDrawable(contexto, R.drawable.ic_face),
        ContextCompat.getDrawable(contexto, R.drawable.ic_mail),
        ContextCompat.getDrawable(contexto, R.drawable.ic_ins),
        ContextCompat.getDrawable(contexto, R.drawable.ic_tik),
        ContextCompat.getDrawable(contexto, R.drawable.ic_wp)
    )
}