package gamaerry.jovenesala42muestranacionaldeteatro.datastructure

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import gamaerry.jovenesala42muestranacionaldeteatro.datastructure.BaseDeDatosPrincipal.Companion.NOMBRE_BASE_DE_DATOS
import gamaerry.jovenesala42muestranacionaldeteatro.getEspecialidades
import gamaerry.jovenesala42muestranacionaldeteatro.getEstados
import gamaerry.jovenesala42muestranacionaldeteatro.getMuestras
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
    // la instancia de base de datos se crea exclusivamente
    // con una funcion propia de la librería de Room
    @Provides
    @Singleton
    fun proveerBaseDeDatosPrincipal(@ApplicationContext contexto: Context): BaseDeDatosPrincipal =
        Room.databaseBuilder(
            contexto,
            BaseDeDatosPrincipal::class.java,
            NOMBRE_BASE_DE_DATOS
        ).createFromAsset("$NOMBRE_BASE_DE_DATOS.db").build()

    // la instancia del dao se crea a partir de la base de datos ya proveida
    @Provides
    @Singleton
    fun proveerDaoPrincipal(baseDeDatos: BaseDeDatosPrincipal) = baseDeDatos.getDaoPrincipal()

    // la instancia del dao se crea a partir de la base de datos ya proveida
    @Provides
    @Singleton
    fun proveerItemsFiltros() = listOf(getEstados(), getEspecialidades(), getMuestras())

    // la instancia del dao se crea a partir de la base de datos ya proveida
    @Provides
    @Singleton
    fun proveerEstadosCheckBox(itemsFiltros: @JvmSuppressWildcards List<List<String>>) =
        itemsFiltros.map { it.map { true }.toMutableList() }
}