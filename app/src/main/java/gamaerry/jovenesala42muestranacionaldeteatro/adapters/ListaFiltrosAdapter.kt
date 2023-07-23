package gamaerry.jovenesala42muestranacionaldeteatro.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.CheckBox
import android.widget.TextView
import gamaerry.jovenesala42muestranacionaldeteatro.R
import gamaerry.jovenesala42muestranacionaldeteatro.getFiltros
import javax.inject.Inject
import javax.inject.Singleton

// este es un adapter distinto a los otros dos pues muestra elementos anidados
// y ello conlleva a un mayor numero de metodos para lograr su funcionamiento
// (notese que los dos parametros que recibe estan definidos en el modulo con una
// anotacion que evita el error de la inyeccion de objetos estructuras de datos)
@Singleton
class ListaFiltrosAdapter
@Inject
constructor(
    private val itemsFiltros: @JvmSuppressWildcards List<List<String>>,
    private val estadosCheckBox: @JvmSuppressWildcards List<MutableList<Boolean>>
) : BaseExpandableListAdapter() {
    // obtiene los tres tipos de filtros
    private val filtros: List<String> = getFiltros()

    // aqui se almacena la accion de actualizar las listas
    lateinit var actualizarLista: () -> Unit

    // los siguientes metodos no requiere mayor detalle en su implementación
    // solo se atiende al resultado deseado según la definición de cada uno
    override fun getGroupCount() = filtros.size

    override fun getChildrenCount(groupPosition: Int) = itemsFiltros[groupPosition].size

    override fun getGroup(groupPosition: Int) = filtros[groupPosition]

    override fun getChild(groupPosition: Int, childPosition: Int) =
        itemsFiltros[groupPosition][childPosition]

    override fun getGroupId(groupPosition: Int) = groupPosition.toLong()

    override fun getChildId(groupPosition: Int, childPosition: Int) = childPosition.toLong()

    override fun hasStableIds() = false

    override fun isChildSelectable(groupPosition: Int, childPosition: Int) = true

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        // esta funcion es continuamente llamada, pero el parametro convertView es guardado de la
        // instancia anterior por lo que puede reutilizarse si es distinto de null y de esta manera
        // evitar el inflado de la vista innecesariamente cada vez que se visiliza el submenu
        val itemMenuFiltro: View = convertView ?: LayoutInflater.from(parent?.context).inflate(
            R.layout.item_menu_filtro,
            parent,
            false
        )
        // se le asigna el nombre correspondiente al item submenu
        (itemMenuFiltro as TextView).text = getGroup(groupPosition)
        // finalmente regresa dicho item submenu para su utilizacion
        return itemMenuFiltro
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        // esta funcion es continuamente llamada, pero el parametro convertView es guardado de la
        // instancia anterior por lo que puede reutilizarse si es distinto de null y de esta manera
        // evitar el inflado de la vista innecesariamente cada vez que se despliega el submenu
        val itemFiltro = (convertView ?: LayoutInflater.from(parent?.context).inflate(
            R.layout.item_filtro,
            parent,
            false
        )) as CheckBox
        // una vez obtenido el checkbox:
        itemFiltro.apply {
            // se inhabilita el listener del estado check
            // (pues por la reutilizacion los estados check
            // de otro submenu puede afectar al actual)
            setOnCheckedChangeListener(null)
            // se le asigna el texto correspondiente
            text = getChild(groupPosition, childPosition)
            // se le asigna su etado check correspondiente
            isChecked = estadosCheckBox[groupPosition][childPosition]
            // de nueva cuenta se establece el listener que
            // cambia ahora en direccion opuesta el estado
            // y actualiza las listas correspondientes
            setOnCheckedChangeListener { _, check ->
                estadosCheckBox[groupPosition][childPosition] = check
                actualizarLista()
            }
        }
        // finalmente regresa dicho item checkbox para su utilizacion
        return itemFiltro
    }
}