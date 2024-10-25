package com.example.lemmingcli

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView


class DictListAdapter(context: Context, objects: ArrayList<DictLookupResult>) :
    ArrayAdapter<DictLookupResult>(context, R.layout.dict_item, objects) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val lookupResult: DictLookupResult? = getItem(position)
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.dict_item, parent, false)
        }
        val word_text = view!!.findViewById<TextView>(R.id.dict_word)
        val senseLinearView = view.findViewById<LinearLayout>(R.id.sense_linear)

        for(sense in lookupResult!!.senses){
            val senseView = LayoutInflater.from(context).inflate(R.layout.sense_item, null)
            val posText = senseView.findViewById<TextView>(R.id.dict_pos)
            val meaningText = senseView.findViewById<TextView>(R.id.dict_meaning)
            posText.text = sense.pos
            meaningText.text = sense.meaning
            senseLinearView.addView(senseView)
        }

        word_text.text = lookupResult.word
        return view
    }
}