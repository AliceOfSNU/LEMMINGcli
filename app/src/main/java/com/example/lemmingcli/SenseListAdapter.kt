package com.example.lemmingcli

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class SenseListAdapter(context: Context, objects: ArrayList<Sense>) :
    ArrayAdapter<Sense>(context, R.layout.sense_item, objects) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val sense: Sense? = getItem(position)
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.sense_item, parent, false)
        }
        val pos_text = view!!.findViewById<TextView>(R.id.dict_pos)
        val meaning_text = view.findViewById<TextView>(R.id.dict_meaning)

        pos_text.text = sense!!.pos
        meaning_text.text = sense.meaning

        return view
    }
}