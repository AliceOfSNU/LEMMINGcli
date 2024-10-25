package com.example.lemmingcli.Fragments

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.lemmingcli.Card
import com.example.lemmingcli.Dictform
import com.example.lemmingcli.Furigana
import com.example.lemmingcli.FuriganaReplacementSpan
import com.example.lemmingcli.R
import org.json.JSONArray
import org.json.JSONObject


class SentenceFragment : Fragment() {

    //    private lateinit var btnGenerate: Button
    private lateinit var btnPrev: Button
    private lateinit var btnNext: Button
    private lateinit var wordText: TextView
    private lateinit var sentText: TextView

    private var wordList = arrayListOf<String>("手放す", "締める", "閉じる", "当てる")
    private var cardList = arrayListOf<Card>()
    private var cardidx = -1
    private var wordidx = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_sentence, container, false)
        wordText = view.findViewById<TextView>(R.id.word_text)
        sentText = view.findViewById<TextView>(R.id.sent_text)
        btnNext = view.findViewById<Button>(R.id.next)
        Log.d("MAIN", cardList.size.toString())
        Log.d("MAIN", wordList.size.toString())

        // first word!
        getSentence(wordList[0])
        btnNext.setOnClickListener(
            View.OnClickListener {
                if (cardidx >= cardList.size - 1 && wordidx + 1 < wordList.size) {
                    // create more on reaching end of already created cards
                    getSentence(wordList[++wordidx])
                }
                else if(cardidx + 1 < cardList.size){
                    showCard(cardList[++cardidx])
                }
            }
        )
        return view
    }

    private fun showCard(card: Card){
        wordText.text = card.word
        val spantext = SpannableString(card.sentence)
        val furicolor = ContextCompat.getColor(this.requireContext(), android.R.color.holo_red_dark)
        for (idx in 0 until card.furiganas.size){
            val f: Furigana = card.furiganas[idx]
            val replacementSpan = FuriganaReplacementSpan(f.yomi, furicolor)
            spantext.setSpan(replacementSpan, f.start, f.end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        sentText.text = spantext
    }


    private fun getSentence(word: String){
        val generationEp = "http://86.127.246.87:22513/generate_sentences"
        val queue = Volley.newRequestQueue(this.requireContext())
        val reqObj = JSONObject()
        reqObj.put("word", word)
        val request =
            JsonObjectRequest(
                Request.Method.POST, generationEp,
                reqObj,
                {
                        response ->
                    val sentences = response.getJSONArray("sentences")
                    val furiganas: JSONArray = response.getJSONArray("furiganas")
                    val dictforms: JSONArray = response.getJSONArray("dictforms")
                    for(i in 0 until sentences.length()){
                        Log.d("MAIN", sentences.getString(i))
                        val fl: JSONArray = furiganas.getJSONArray(i)
                        val dl: JSONArray = dictforms.getJSONArray(i)
                        val furiganaList = ArrayList<Furigana>()
                        val dictformList = ArrayList<Dictform>()

                        for(fidx in 0 until fl.length()) {
                            val f: JSONArray = fl.getJSONArray(fidx)
                            val w: String = f.getString(0)
                            furiganaList.add(Furigana(word=w, yomi=f.getString(1), start=f.getInt(2), end=f.getInt(2)+w.length))
                        }

                        for(didx in 0 until dl.length()){
                            val d: JSONArray = dl.getJSONArray(didx)
                            val w:String = d.getString(0)
                            dictformList.add(Dictform(word=w, dictform = d.getString(1), start=d.getInt(2), end=d.getInt(2)+w.length))
                        }
                        cardList.add(Card(word, sentences.getString(i), furiganaList, dictformList))
                    }
                    if(cardidx+1 < cardList.size){
                        showCard(cardList[++cardidx])
                    }
                },
                {
                        error -> sentText.text = error.toString()
                }
            )
        queue.add(request)
    }
}