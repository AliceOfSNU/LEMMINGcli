package com.example.lemmingcli.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.example.lemmingcli.DictListAdapter
import com.example.lemmingcli.DictLookupResult
import com.example.lemmingcli.R
import com.example.lemmingcli.Sense

class JishoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_jisho, container, false)
        val dictListView = view.findViewById<ListView>(R.id.dict_list)
        val dictListData = ArrayList<DictLookupResult>()

        var senses = ArrayList<Sense>()
        senses.add(Sense("noun", "cause;motivation;beginning"))
        dictListData.add(DictLookupResult("きっかけ", senses))

        senses = ArrayList<Sense>()
        senses.add(Sense("verb", "to let go;release"))
        senses.add(Sense("verb", "to part with"))
        dictListData.add(DictLookupResult("手放す", senses))

        val dictListAdapter = DictListAdapter(this.requireContext(), dictListData)
        dictListView.adapter = dictListAdapter

        return view
    }

}