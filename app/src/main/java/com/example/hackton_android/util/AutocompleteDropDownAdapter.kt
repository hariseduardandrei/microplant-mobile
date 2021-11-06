package com.example.hackton_android.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import com.example.hackton_android.R
import com.example.hackton_android.model.GenericModel


class AutocompleteDropDownAdapter(
    context: Context,
    callbackGetDepartments: (searchTerm: String) -> Unit,
    val values: MutableList<String>
) :
    ArrayAdapter<String>(context, 0, values) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.custom_dropdown_item, parent, false)
        val textViewName = view.findViewById<TextView>(R.id.tv_data)
        val currentItem: String? = getItem(position)

        if (currentItem != null) {
            textViewName.text = currentItem
        }
        return view
    }

    override fun getCount(): Int {
        if (values.size > 5) {
            return 5
        }
        return values.size
    }

    override fun getFilter(): Filter {
        return nameFilter
    }

    private var nameFilter: Filter = object : Filter() {
        override fun convertResultToString(resultValue: Any): String {
            return (resultValue as String)
        }

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            return if (constraint != null && constraint.isNotEmpty()) {
                callbackGetDepartments(constraint.toString())
                val filterResults = FilterResults()
                filterResults.values = values
                filterResults.count = values.size
                filterResults
            } else {
                val filterResults = FilterResults()
                filterResults.values =
                    listOf("Please enter 1 or more characters")
                filterResults.count = 1
                filterResults
            }

        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            if (results != null && results.count > 0) {
                clear()
                addAll(results.values as MutableList<String>)
                notifyDataSetChanged()
            }
        }
    }
}
