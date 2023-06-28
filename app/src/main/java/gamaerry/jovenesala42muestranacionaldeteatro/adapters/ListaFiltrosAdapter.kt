package gamaerry.jovenesala42muestranacionaldeteatro.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.CheckBox
import android.widget.TextView
import gamaerry.jovenesala42muestranacionaldeteatro.R

class ListaFiltrosAdapter (
    private val context: Context,
    private val groups: List<String>,
    private val childItems: List<List<String>>
) : BaseExpandableListAdapter() {
    private val estadosCheckBox = childItems.map { it.map { true }.toMutableList() }

    override fun getGroupCount(): Int {
        return groups.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return childItems[groupPosition].size
    }

    override fun getGroup(groupPosition: Int): Any {
        return groups[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return childItems[groupPosition][childPosition]
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
        val itemMenuFiltro: View = convertView ?: LayoutInflater.from(context).inflate(
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
        val itemFiltro: View = convertView ?: LayoutInflater.from(context).inflate(
            R.layout.item_filtro,
            parent,
            false
        )
        itemFiltro.findViewById<CheckBox>(R.id.filtro).apply {
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