package com.example.try2zdie


import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class MyListAdapter(private val context: Activity, private val type: Array<String>,
                    private val id: Array<String>, private val qN: Array<String>,
                    private val qH: Array<String>, private val cId: Array<String>)
    : ArrayAdapter<String>(context, R.layout.custom_list, id) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.custom_list, null, true)

        val itemType = rowView.findViewById(R.id.itemType) as TextView
        val itemId = rowView.findViewById(R.id.itemId) as TextView
        val quantityNeed = rowView.findViewById(R.id.quantityNeed) as TextView
        val quantityHave = rowView.findViewById(R.id.quantityHave) as TextView
        val colorId = rowView.findViewById(R.id.colorId) as TextView

        itemId.text = "type: ${type[position]}"
        itemType.text = "Id: ${id[position]}"
        quantityNeed.text = "Need: ${qN[position]}"
        quantityHave.text = "Have: ${qH[position]}"
        colorId.text = "Color: ${cId[position]}"
        return rowView
    }
}