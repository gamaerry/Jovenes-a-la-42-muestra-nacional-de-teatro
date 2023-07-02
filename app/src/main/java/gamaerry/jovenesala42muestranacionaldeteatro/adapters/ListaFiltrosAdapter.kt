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

@Singleton
class ListaFiltrosAdapter
@Inject
constructor(
    private val itemsFiltros: @JvmSuppressWildcards List<List<String>>,
    private val estadosCheckBox: @JvmSuppressWildcards List<MutableList<Boolean>>
) : BaseExpandableListAdapter() {
    private val filtros: List<String> = getFiltros()

    override fun getGroupCount(): Int {
        return filtros.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return itemsFiltros[groupPosition].size
    }

    override fun getGroup(groupPosition: Int): Any {
        return filtros[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return itemsFiltros[groupPosition][childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        val itemMenuFiltro: View = convertView ?: LayoutInflater.from(parent?.context).inflate(
            R.layout.item_menu_filtro,
            parent,
            false
        )
        val etiqueta: TextView = itemMenuFiltro.findViewById(android.R.id.text1)
        etiqueta.text = getGroup(groupPosition).toString()
        return itemMenuFiltro
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        val itemFiltro = (convertView ?: LayoutInflater.from(parent?.context).inflate(
            R.layout.item_filtro,
            parent,
            false
        )) as CheckBox
        itemFiltro.apply {
            setOnCheckedChangeListener(null)
            text = getChild(groupPosition, childPosition).toString()
            isChecked = estadosCheckBox[groupPosition][childPosition]
            setOnCheckedChangeListener { _, check ->
                estadosCheckBox[groupPosition][childPosition] = check
            }
        }
        return itemFiltro
    }
}